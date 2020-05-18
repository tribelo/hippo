package uk.nhs.digital.apispecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.apispecs.dto.Content;

import java.util.List;

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
        LOGGER.info("===========================================================");
        LOGGER.info(apigeeService.getSpecification("289897"));
        LOGGER.info("===========================================================");

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

        specsToPublish.forEach(apiSpecification -> {

            final String specHtml = getHtmlForSpec(apiSpecification);

            apiSpecification.setHtml(specHtml);

            apiSpecification.saveAndPublish();
        });
    }

    private String getHtmlForSpec(final ApiSpecification apiSpecification) {
        return apiSpecHtmlProvider.getHtmlForSpec(apiSpecification);
    }
}
