package uk.nhs.digital.apispecs;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.dto.Content;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.apispecs.ApiSpecPublicationService.PublicationResult.FAIL;
import static uk.nhs.digital.apispecs.ApiSpecPublicationService.PublicationResult.PASS;

public class ApiSpecPublicationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpecPublicationService.class);

    private ApiSpecHtmlProvider apiSpecHtmlProvider;
    private ApigeeService apigeeService;
    private ApiSpecRepository repository;

    public ApiSpecPublicationService(final ApigeeService apigeeService,
                                     final ApiSpecRepository repository,
                                     final ApiSpecHtmlProvider apiSpecHtmlProvider) {
        this.apigeeService = apigeeService;
        this.repository = repository;
        this.apiSpecHtmlProvider = apiSpecHtmlProvider;
    }

    public void publish() {

        final List<Content> apigeeSpecsStatuses = getApigeeSpecStatuses();

        final List<ApiSpecification> cmsApiSpecifications = findCmsApiSpecifications();

        final List<ApiSpecification> specsToPublish = identifySpecsEligibleForPublication(
            cmsApiSpecifications,
            apigeeSpecsStatuses
        );

        LOGGER.info("Specifications found: in CMS: {}, in Apigee: {}, updated in Apigee and eligible to publish in CMS: {}",
            cmsApiSpecifications.size(),
            apigeeSpecsStatuses.size(),
            specsToPublish.size());

        publish(specsToPublish);
    }

    private List<Content> getApigeeSpecStatuses() {
        return apigeeService.apigeeSpecsStatuses();
    }

    private List<ApiSpecification> findCmsApiSpecifications() {
        return repository.findAllApiSpecifications();
    }

    private List<ApiSpecification> identifySpecsEligibleForPublication(
        final List<ApiSpecification> cmsSpecs,
        final List<Content> apigeeSpecStatuses
    ) {
        final Map<String, Content> apigeeSpecsById = Maps.uniqueIndex(apigeeSpecStatuses, Content::getId);

        return cmsSpecs.stream()
            .filter(specificationsPresentInBothSystems(apigeeSpecsById))
            .filter(specificationsChangedInApigeeAfterPublishedInCms(apigeeSpecsById))
            .collect(toList());
    }

    private Predicate<ApiSpecification> specificationsChangedInApigeeAfterPublishedInCms(
        final Map<String, Content> apigeeSpecsById) {
        return apiSpecification -> {
            final Content apigeeSpec = apigeeSpecsById.get(apiSpecification.getId());

            final Instant cmsSpecificationLastPublicationInstant =
                apiSpecification.getLastPublicationInstant().orElse(Instant.EPOCH);

            return apigeeSpec.getModified().isAfter(cmsSpecificationLastPublicationInstant);
        };
    }

    private Predicate<ApiSpecification> specificationsPresentInBothSystems(
        final Map<String, Content> apigeeSpecsById) {
        return apiSpecification -> apigeeSpecsById.containsKey(apiSpecification.getId());
    }

    private void publish(List<ApiSpecification> specsToPublish) {

        final long failedSpecificationsCount = specsToPublish.stream()
            .map(this::publish)
            .filter(PublicationResult::failed)
            .count();

        reportErrorIfAnySpecificationsFailed(failedSpecificationsCount, specsToPublish.size());
    }

    private void reportErrorIfAnySpecificationsFailed(final long failedSpecificationsCount,
                                                      final long specificationsToPublishCount
    ) {
        if (failedSpecificationsCount > 0) {
            throw new RuntimeException(
                format("Failed to publish %d out of %d specifications; see preceding logs for details",
                    failedSpecificationsCount,
                    specificationsToPublishCount
                ));
        }
    }

    private PublicationResult publish(final ApiSpecification apiSpecificationToPublish) {

        try {
            LOGGER.info("Publishing API specification: {}", apiSpecificationToPublish);

            final String specHtml = getHtmlForSpec(apiSpecificationToPublish);

            apiSpecificationToPublish.setHtml(specHtml);

            apiSpecificationToPublish.saveAndPublish();

            LOGGER.info("API specification has been published: {}", apiSpecificationToPublish.getId());

            return PASS;

        } catch (final Exception e) {
            LOGGER.error("Failed to publish specification: " + apiSpecificationToPublish, e);

            return FAIL;
        }
    }

    private String getHtmlForSpec(final ApiSpecification apiSpecification) {
        return apiSpecHtmlProvider.getHtmlForSpec(apiSpecification);
    }

    enum PublicationResult {
        PASS,
        FAIL;

        public boolean failed() {
            return this == FAIL;
        }

        public boolean passed() {
            return this == PASS;
        }
    }
}
