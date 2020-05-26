package uk.nhs.digital.apispecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.nhs.digital.apispecs.config.ApigeeConfig;
import uk.nhs.digital.apispecs.config.Authentication;
import uk.nhs.digital.apispecs.dto.Content;
import uk.nhs.digital.apispecs.dto.ContentsList;
import uk.nhs.digital.apispecs.dto.Token;

import java.net.URI;
import java.time.Clock;
import java.util.Arrays;
import java.util.List;

public class ApigeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApigeeService.class);

    private static final String ERR_MSG_APIGEE_LIST_SPECS = "Failed to retrieve list of specifications from Apigee";
    private static final String ERR_MSG_APIGEE_SINGLE_SPEC = "Failed to retrieve specification from Apigee; spec id:";

    private static final String ERR_MSG_APIGEE_FAIL_WITH_HTTP_CODE = "Apigee responded with code : ";
    private static final String ERR_MSG_APIGEE_FAIL_WITH_RESPONSE = "Call to Apigee has failed; response: {}";

    private static final String ERR_MSG_OAUTH_TOKEN_FAIL = "Failed to obtain oAuth token from Apigee.";


    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";
    private static final String BEARER = "Bearer ";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String GRANT_TYPE = "grant_type";
    private static final String MFA_TOKEN = "mfa_token";

    private RestTemplate restTemplate;
    private ApigeeConfig config;
    private Clock clock;

    public ApigeeService(RestTemplate restTemplate, ApigeeConfig config, Clock clock) {
        this.restTemplate = restTemplate;
        this.config = config;
        this.clock = clock;
    }

    public List<Content> apigeeSpecsStatuses() throws ApigeeServiceException {

        return getContentList();
    }

    private List<Content> getContentList() throws ApigeeServiceException {

        LOGGER.info("Retrieving list of available specifications.");

        try {
            String accessToken = getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            headers.set(AUTHORIZATION, BEARER + accessToken);

            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<ContentsList> response = restTemplate.exchange(config.getAllSpecUrl().trim(), HttpMethod.GET, request, ContentsList.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                return response.getBody().getContents();
            } else {
                LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_RESPONSE, response);
                throw new ApigeeServiceException(ERR_MSG_APIGEE_FAIL_WITH_HTTP_CODE + response.getStatusCode());
            }

        } catch (Exception ex) {
            throw new ApigeeServiceException(ERR_MSG_APIGEE_LIST_SPECS, ex);
        }
    }

    public String getSpecification(final String specificationId) {

        LOGGER.info("Retrieving specification with id {}.", specificationId);

        try {
            String uri = UriComponentsBuilder.fromHttpUrl(config.getSingleSpecUrl()).build(specificationId).toString();
            String accessToken = getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            headers.set(AUTHORIZATION, BEARER + accessToken);

            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                return response.getBody();
            } else {
                LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_RESPONSE, response);

                throw new ApigeeServiceException(ERR_MSG_APIGEE_FAIL_WITH_HTTP_CODE + response.getStatusCode());
            }

        } catch (Exception ex) {
            throw new ApigeeServiceException(ERR_MSG_APIGEE_SINGLE_SPEC + specificationId, ex);
        }
    }

    private String getAccessToken() throws Exception {

        LOGGER.info("Fetching OAuth token.");

        ResponseEntity<Token> response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set(AUTHORIZATION, BASIC + config.getBasicToken());

            Authentication auth = new Authentication();
            MultiValueMap<String, String> bodyParamMap = new LinkedMultiValueMap<>();
            bodyParamMap.add(USERNAME, config.getUsername());
            bodyParamMap.add(PASSWORD, config.getPassword());
            bodyParamMap.add(GRANT_TYPE, PASSWORD);
            bodyParamMap.add(MFA_TOKEN, auth.googleAuthenticatorCode(config.getOtpKey(), clock));

            HttpEntity<?> request = new HttpEntity<>(bodyParamMap,headers);
            response = restTemplate.exchange(config.getTokenUrl().trim(), HttpMethod.POST, request, Token.class);
        } catch (Exception e) {
            throw new RuntimeException(ERR_MSG_OAUTH_TOKEN_FAIL, e);
        }

        if (response.getStatusCode() == HttpStatus.OK) {

            return response.getBody().getAccessToken();
        } else {
            LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_RESPONSE, response);

            throw new RuntimeException(ERR_MSG_OAUTH_TOKEN_FAIL);
        }
    }
}
