package uk.nhs.digital.jobs;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.sling.testing.mock.jcr.MockJcr;
import org.hippoecm.repository.api.Document;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onehippo.forge.content.exim.core.DocumentManager;
import org.onehippo.repository.documentworkflow.DocumentVariant;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriTemplate;
import uk.nhs.digital.JcrDocumentUtils;
import uk.nhs.digital.apispecs.jobs.ApiSpecConversionJob;

import javax.jcr.Node;
import javax.jcr.Session;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.*;
import static uk.nhs.digital.test.util.FileUtils.readFileInClassPath;
import static uk.nhs.digital.test.util.JcrTestUtils.BloomReachJcrDocumentVariantType.DRAFT;
import static uk.nhs.digital.test.util.JcrTestUtils.*;
import static uk.nhs.digital.test.util.MockJcrRepoProvider.initJcrRepoFromYaml;

@RunWith(PowerMockRunner.class)
@PrepareForTest(JcrDocumentUtils.class)
@PowerMockIgnore({"javax.net.ssl.*", "javax.crypto.*"})
public class ApiSpecConversionJobIntegrationTest {

    @Rule public WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicPort());

    private static final String PROPERTY_NAME_WEBSITE_HTML = "website:html";

    // Test data
    private static final String TEST_SPEC_ID = "269326";
    private static final String TEST_DATA_FILES_PATH = "/test-data/api-specifications/ApiSpecConversionJobIntegrationTest";

    // JCR repo access
    private Session session;
    private Node repositoryRootNode;

    private RepositoryJobExecutionContext repositoryJobExecutionContext;

    // Apigee URLs
    private String apigeeAllSpecsUrlPath = "/dapi/api/organizations/nhsd-nonprod/specs/folder/home/specs";
    private String apigeeSingleSpecUrlPathTemplate = "/dapi/api/organizations/test-org/specs/doc/{specificationId}/content";
    private String oauthTokenUrlPath = "/oauth/token";

    private ApiSpecConversionJob apiSpecConversionJob;

    @Before
    public void setUp() throws Exception {

        setUpCmsToApigeeAccessConfig();

        session = spy(MockJcr.newSession());
        doNothing().when(session).logout(); // prevent the job from closing the session - it's needed in assertions

        repositoryJobExecutionContext = mock(RepositoryJobExecutionContext.class);
        given(repositoryJobExecutionContext.createSystemSession()).willReturn(session);

        apiSpecConversionJob = new ApiSpecConversionJob();
    }

    @Test
    public void downloadsSpecUpdateFromApigeeAndPublishesOnWebsite() throws Exception {

        // given
        apigeeReturnsAccessToken();
        apigeeReturnsListOfSpecsIncludingTheOneConfiguredInCms();
        apigeeReturnsDetailsOfTheSpecConfiguredInCms();
        cmsContainsSpecDocMatchingUpdatedSpecInApigee();

        // when
        apiSpecConversionJob.execute(repositoryJobExecutionContext);

        // then
        verifyHtmlGeneratedFromApigeeSpecWasSetOnApiSpecificationDocument();
        verifyApiSpecificationDocumentWasPublished();
    }

    private void setUpCmsToApigeeAccessConfig() {

        final UriComponentsBuilder baseWiremockUrl =
            UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(wireMock.port());

        final String apigeeAllSpecsUrl = baseWiremockUrl.cloneBuilder().path(apigeeAllSpecsUrlPath).toUriString();
        final String apigeeSingleSpecUrlTemplate = baseWiremockUrl.cloneBuilder().toUriString() + apigeeSingleSpecUrlPathTemplate;
        final String oauthTokenUrl = baseWiremockUrl.cloneBuilder().path(oauthTokenUrlPath).toUriString();

        final String oauthUsername = "oauthUsername";
        final String oauthPassword = "oauthPassword";
        final String oauthBasicAuthToken = "ZWRnZWNsaTplZGdlY2xpc2VjcmV0";
        final String oauthOtpKey = "JBSWY3DPEHPK3PXP";

        System.setProperty("devzone.apigee.resources.specs.all.url", apigeeAllSpecsUrl);
        System.setProperty("devzone.apigee.resources.specs.individual.url", apigeeSingleSpecUrlTemplate);
        System.setProperty("devzone.apigee.oauth.token.url", oauthTokenUrl);
        System.setProperty("devzone.apigee.oauth.username", oauthUsername);
        System.setProperty("devzone.apigee.oauth.password", oauthPassword);
        System.setProperty("devzone.apigee.oauth.basicauthtoken", oauthBasicAuthToken);
        System.setProperty("devzone.apigee.oauth.otpkey", oauthOtpKey);
    }

    private void verifyHtmlGeneratedFromApigeeSpecWasSetOnApiSpecificationDocument() {

        final Node specificationHandleNode = getExistingSpecHandleNode();

        final Node documentVariantNodeDraft = getDocumentVariantNode(specificationHandleNode, DRAFT);

        final String actualSpecHtml =
            getStringProperty(documentVariantNodeDraft, PROPERTY_NAME_WEBSITE_HTML);

        final String expectedSpecHtml = codeGenGeneratedSpecificationHtml();

        assertThat(
            "CodeGen-generated specification HTML has been set on the document",
            actualSpecHtml,
            is(expectedSpecHtml)
        );
    }

    private void verifyApiSpecificationDocumentWasPublished() {

        final Node specificationHandleNode = getExistingSpecHandleNode();

        verifyStatic(JcrDocumentUtils.class);
        JcrDocumentUtils.publish(specificationHandleNode);
    }

    private Node getExistingSpecHandleNode() {
        return getRelativeNode(
            repositoryRootNode, "/content/documents/corporate-website/api-specifications-location-a/api-spec-a");
    }

    private void apigeeReturnsAccessToken() {

        // @formatter:off
        final String apigeeAccessTokenResponse = ""
            + "{"
            + "  \"accessToken\":\"eyJhbGciOiJSUzI1NiJ9.eyJqdGkiOiI5YjAzNWVmYS05Yzg1LTQ1ZWItYmU3NC05ZDI1MzQxMWIzZmIiLCJzdWIiOiI2MGExMDA2NC04MTlmLTQ4NDUtOTYwNS1iZjU5MzU0YTdmNTkiLCJzY29wZSI6WyJzY2ltLmVtYWlscy5yZWFkIiwic2NpbS5tZSIsIm9wZW5pZCIsInBhc3N3b3JkLndyaXRlIiwiYXBwcm92YWxzLm1lIiwic2NpbS5pZHMucmVhZCIsIm9hdXRoLmFwcHJvdmFscyJdLCJjbGllbnRfaWQiOiJlZGdlY2xpIiwiY2lkIjoiZWRnZWNsaSIsImF6cCI6ImVkZ2VjbGkiLCJncmFudF90eXBlIjoicGFzc3dvcmQiLCJ1c2VyX2lkIjoiNjBhMTAwNjQtODE5Zi00ODQ1LTk2MDUtYmY1OTM1NGE3ZjU5Iiwib3JpZ2luIjoidXNlcmdyaWQiLCJ1c2VyX25hbWUiOiJyYWZhbC5rb2NpbnNraTFAbmhzLm5ldCIsImVtYWlsIjoicmFmYWwua29jaW5za2kxQG5ocy5uZXQiLCJhdXRoX3RpbWUiOjE1OTA1MTc1ODUsImFsIjoyLCJyZXZfc2lnIjoiNDUxOWY5YzAiLCJpYXQiOjE1OTA1MTc1ODUsImV4cCI6MTU5MDU2MDc4NSwiaXNzIjoiaHR0cHM6Ly9sb2dpbi5hcGlnZWUuY29tIiwiemlkIjoidWFhIiwiYXVkIjpbImVkZ2VjbGkiLCJzY2ltLmVtYWlscyIsInNjaW0iLCJvcGVuaWQiLCJwYXNzd29yZCIsImFwcHJvdmFscyIsInNjaW0uaWRzIiwib2F1dGgiXX0.i1ZuASe2ZvaEnQLlMBoYQ-7jUZv61vCtn-ydYSgKiBuXyGR-jhaF_KzGRnE-pflO474Q2zC74LKUgOVJZioGmjuSFUPxZDAbB2wC78qSYsP1ImTVhxTII3kL9oblDAhrrKIB0a_O9KUnGtBPQcIlca7ogHELVZ6K6ACThhVC6yJfiIaOIZH4PWUA91lWxtexDPMW-9-zMI8SUMGc_FKhtx_a-JLvzPkzGzWHOGsYhTdm_d9pR_qrkIY_R6cjH3SAVwleppuTGrOhkkCOFMJmOiYZ6JXh7cOFN25jwMLepluU7TMp6im2QRyd3fkZwLW4Npgv-PIUnLJv3uL2YCRlGw\""
            + "}";
        // @formatter:on

        wireMock.givenThat(
            post(urlEqualTo(oauthTokenUrlPath))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(apigeeAccessTokenResponse)
                )
        );
    }

    private void apigeeReturnsListOfSpecsIncludingTheOneConfiguredInCms() {
        wireMock.givenThat(
            get(urlMatching(apigeeAllSpecsUrlPath))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(aListOfApigeeSpecifications())
                )
        );
    }

    private void apigeeReturnsDetailsOfTheSpecConfiguredInCms() {

        final String expectedApigeeSingleSpecUrl =
            new UriTemplate(apigeeSingleSpecUrlPathTemplate).expand(TEST_SPEC_ID).toASCIIString();

        wireMock.givenThat(
            get(urlMatching(expectedApigeeSingleSpecUrl))
                .willReturn(
                    ok()
                        .withHeader("Content-Type", "application/json;charset=UTF-8")
                        .withBody(apigeeApiSpecificationJson())
                )
        );
    }

    private void cmsContainsSpecDocMatchingUpdatedSpecInApigee() throws Exception {

        initJcrRepoFromYaml(session, TEST_DATA_FILES_PATH + "/specification-documents-in-cms.yml");

        repositoryRootNode = getRootNode(session);

        final Node specDocHandleNode = getExistingSpecHandleNode();

        MockJcr.setQueryResult(session, asList(
            specDocHandleNode
        ));

        // Low-level JCR-related components mocked where it was prohibitively hard to avoid:

        final DocumentManager documentManager = mock(DocumentManager.class);

        mockStatic(JcrDocumentUtils.class);
        given(JcrDocumentUtils.documentManagerFor(session)).willReturn(documentManager);

        final Node documentVariantNodeDraft = getDocumentVariantNode(specDocHandleNode, DRAFT);

        final Document documentVariantDraft = new DocumentVariant(documentVariantNodeDraft);
        given(documentManager.obtainEditableDocument(specDocHandleNode)).willReturn(documentVariantDraft);
    }

    private String aListOfApigeeSpecifications() {
        return readFileInClassPath(TEST_DATA_FILES_PATH + "/specifications-in-apigee.json");
    }

    private String apigeeApiSpecificationJson() {
        return readFileInClassPath(TEST_DATA_FILES_PATH + "/openapi-specification.json");
    }

    private String codeGenGeneratedSpecificationHtml() {
        return readFileInClassPath(TEST_DATA_FILES_PATH + "/codegen-generated-spec.html");
    }
}