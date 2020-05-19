package uk.nhs.digital.apispecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.dto.Content;

import java.util.List;

import static java.lang.String.format;
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

        List<Content> apigeeSpecsStatuses = getApigeeSpecStatuses();   // apigee specs statuses
        List<ApiSpecification> cmsSpecs = findApiSpecifications();   // cms spec documents

        LOGGER.info("============ content size =============  {}", apigeeSpecsStatuses.size());

        List<ApiSpecification> specsToPublish = findSpecsToPublish(cmsSpecs,apigeeSpecsStatuses);

        publish(specsToPublish);
    }

    private List<Content> getApigeeSpecStatuses() {
        return apigeeService.apigeeSpecsStatuses();
    }

    private List<ApiSpecification> findApiSpecifications() {
        return repository.findAllApiSpecifications();
    }

    private List<ApiSpecification> findSpecsToPublish(List<ApiSpecification> cmsSpecs, List<Content> apigeeSpecStatuses) {
        LOGGER.debug("cms specs size: [{}], apigee specs size: [{}]", cmsSpecs.size(), apigeeSpecStatuses.size());
        LOGGER.info("Compare cms specs and apigee specs to find out specs to publish...");
        return cmsSpecs;
    }

    private void publish(List<ApiSpecification> specsToPublish) {
        LOGGER.info("Publishing the SPEC for specification-id: [{}]", specsToPublish.size());

        final long failedSpecificationsCount = specsToPublish.stream()
            .map(this::publish)
            .filter(publicationResult -> publicationResult.equals(FAIL))
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

    private PublicationResult publish(final ApiSpecification apiSpecification) {

        try {
            final String specHtml = getHtmlForSpec(apiSpecification);

            apiSpecification.setHtml(specHtml);

            apiSpecification.saveAndPublish();

            return PASS;

        } catch (final Exception e) {
            LOGGER.error("Failed to publish specification " + apiSpecification, e);
            return FAIL;
        }
    }

    private String getHtmlForSpec(final ApiSpecification apiSpecification) {
        return apiSpecHtmlProvider.getHtmlForSpec(apiSpecification);
    }

    enum PublicationResult {
        PASS,
        FAIL
    }
}
