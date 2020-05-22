package uk.nhs.digital.apispecs.jobs;

import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import uk.nhs.digital.apispecs.ApiSpecHtmlProvider;
import uk.nhs.digital.apispecs.ApiSpecPublicationService;
import uk.nhs.digital.apispecs.ApiSpecRepository;
import uk.nhs.digital.apispecs.ApigeeService;
import uk.nhs.digital.apispecs.config.ApigeeConfig;

import java.time.Clock;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

public class ApiSpecConversionJob implements RepositoryJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpecConversionJob.class);

    private static final String APIGEE_ALL_SPEC_URL = "devzone.apigee.url.specs.all";
    private static final String OAUTH_TOKEN_URL = "devzone.apigee.url.oauth.token";
    private static final String APIGEE_SINGLE_SPEC_URL = "devzone.apigee.url.specs.individual";
    private static final String OAUTH_TOKEN_USERNAME = "devzone.apigee.username";
    private static final String OAUTH_TOKEN_PASSWORD = "devzone.apigee.password";
    private static final String BASIC_TOKEN = "devzone.apigee.basicauthtoken";
    private static final String OTP_KEY = "devzone.apigee.otpkey";

    @Override
    public void execute(RepositoryJobExecutionContext context) throws RepositoryException {

        final Session session = context.createSession(new SimpleCredentials("admin", "admin".toCharArray()));

        String apigeeAllSpecUrl = System.getProperty(APIGEE_ALL_SPEC_URL);
        String apigeeSingleSpecUrl = System.getProperty(APIGEE_SINGLE_SPEC_URL);
        String tokenUrl = System.getProperty(OAUTH_TOKEN_URL);
        String username = System.getProperty(OAUTH_TOKEN_USERNAME);
        String password = System.getProperty(OAUTH_TOKEN_PASSWORD);
        String basicToken = System.getProperty(BASIC_TOKEN);
        String otpKey = System.getProperty(OTP_KEY);

        try {
            final RestTemplate restTemplate = new RestTemplate();
            final ApigeeConfig config = new ApigeeConfig(apigeeAllSpecUrl, apigeeSingleSpecUrl, tokenUrl, username, password, basicToken, otpKey);
            final Clock clock = Clock.systemDefaultZone();

            final ApigeeService apigeeService = new ApigeeService(restTemplate, config, clock);

            final ApiSpecPublicationService apiSpecPublicationService = new ApiSpecPublicationService(
                apigeeService,
                new ApiSpecRepository(session),
                new ApiSpecHtmlProvider(apigeeService)
            );

            apiSpecPublicationService.publish();

        } catch (Exception ex) {
            LOGGER.error(ex.getLocalizedMessage());
        } finally {
            session.logout();
        }
    }
}
