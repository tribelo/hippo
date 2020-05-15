package uk.nhs.digital.apispecs.jobs;

import org.onehippo.repository.scheduling.RepositoryJob;
import org.onehippo.repository.scheduling.RepositoryJobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import uk.nhs.digital.apispecs.ApiSpecPublicationService;
import uk.nhs.digital.apispecs.ApiSpecRepository;
import uk.nhs.digital.apispecs.ApigeeService;
import uk.nhs.digital.apispecs.config.ApigeeConfig;

import java.time.Clock;
import javax.jcr.RepositoryException;

public class ApiSpecConversionJob implements RepositoryJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSpecConversionJob.class);

    private static final String APIGEE_SPEC_URL = "apigeeSpecUrl";
    private static final String OAUTH_TOKEN_URL = "oAuthTokenUrl";
    private static final String OAUTH_TOKEN_USERNAME = "oAuthTokenUsername";
    private static final String OAUTH_TOKEN_PASSWORD = "oAuthTokenPassword";
    private static final String BASIC_TOKEN = "basicToken";
    private static final String OTP_KEY = "otpKey";

    @Override
    public void execute(RepositoryJobExecutionContext context) throws RepositoryException {

        String apigeeUrl = context.getAttribute(APIGEE_SPEC_URL);
        String tokenUrl = context.getAttribute(OAUTH_TOKEN_URL);
        String username = context.getAttribute(OAUTH_TOKEN_USERNAME);
        String password = context.getAttribute(OAUTH_TOKEN_PASSWORD);
        String basicToken = context.getAttribute(BASIC_TOKEN);
        String otpKey = context.getAttribute(OTP_KEY);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ApigeeConfig config = new ApigeeConfig(apigeeUrl,tokenUrl,username,password,basicToken,otpKey);
            Clock clock = Clock.systemDefaultZone();

            ApiSpecPublicationService apiSpecPublicationService = new ApiSpecPublicationService(new ApigeeService(restTemplate, config, clock), new ApiSpecRepository());
            apiSpecPublicationService.publish();

        } catch (Exception ex) {
            LOGGER.error(ex.getLocalizedMessage());
        }
    }
}
