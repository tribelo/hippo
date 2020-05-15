package uk.nhs.digital.jobs;

import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import uk.nhs.digital.apispecs.jobs.ApiSpecConversionJob;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.springframework.web.util.UriComponentsBuilder.fromHttpUrl;
import static uk.nhs.digital.test.util.FileUtils.readFileInClassPath;


@Ignore("work in progress")
public class ApiSpecConversionJobIntegrationTest {

    private ApiSpecConversionJob apiSpecConversionJob;

    private RepositoryJobExecutionContext repositoryJobExecutionContext;

    @Rule public WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicPort());

    int wireMockPort;
    private String basicApigeeSpecsUrl;

    private Session session;

    @Before
    public void setUp() throws Exception {

        wireMockPort = wireMock.port();

        basicApigeeSpecsUrl = fromHttpUrl("https://mock.apigee/mock-organization/specs").port(wireMockPort).build().toUriString();

        session = MockJcr.newSession();

        repositoryJobExecutionContext = mock(RepositoryJobExecutionContext.class);
        given(repositoryJobExecutionContext.createSession(isA(SimpleCredentials.class))).willReturn(session);

        apiSpecConversionJob = new ApiSpecConversionJob();
    }

    @Test
    public void downloadsSpecUpdateFromApigeeAndPublishesOnWebsite() throws RepositoryException {

        // given
        apigeeReturnsListOfSpecsIncludingTheOneConfiguredInCms();
        apigeeReturnsDetailsOfTheSpecConfiguredInCms();

        cmsContainsSpecDocMatchingUpdatedSpecInApigee();

        // when
        apiSpecConversionJob.execute(repositoryJobExecutionContext);

        // then
        verifyCmsRetrievedListOfSpecificationsFromApigee();
        // retrieves doc from the repo
        // calls Apigee - to get the spec
        verifyCmsRetrievedSingleSpecificationFromApigee();
        // sets properties on the doc with content matching that from Apigee
        // publishes the doc
    }

    private void apigeeReturnsListOfSpecsIncludingTheOneConfiguredInCms() {
        wireMock.givenThat(
            get(urlMatching("https://mock.apigee/mock-organization/specs"))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(aListOfApigeeSpecifications())
                )
        );
    }

    private void apigeeReturnsDetailsOfTheSpecConfiguredInCms() {
        wireMock.givenThat(
            get(urlMatching("https://mock.apigee/mock-organization/specs/\\d+"))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody(anApigeeSpecification())
                )
        );
    }

    private void verifyCmsRetrievedSingleSpecificationFromApigee() {
        wireMock.verify(
            getRequestedFor(urlEqualTo(basicApigeeSpecsUrl + "/269326"))
                .withBasicAuth(new BasicCredentials("apigeeUserName", "apigeePassword"))
                .withHeader("Accept", equalTo("application/json"))
        );
    }

    private void verifyCmsRetrievedListOfSpecificationsFromApigee() {
        wireMock.verify(
            getRequestedFor(urlEqualTo(basicApigeeSpecsUrl))
                .withBasicAuth(new BasicCredentials("apigeeUserName", "apigeePassword"))
                .withHeader("Accept", equalTo("application/json"))
        );
    }

    private void cmsContainsSpecDocMatchingUpdatedSpecInApigee() {

    }

    private String aListOfApigeeSpecifications() {
        return readFileInClassPath("/test-data/api-specifications/ApiSpecConversionJobIntegrationTest/openapi-specifications.json");
    }

    private String anApigeeSpecification() {
        return readFileInClassPath("/test-data/api-specifications/ApiSpecConversionJobIntegrationTest/openapi-specification.json");
    }
}