package uk.nhs.digital.apispecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

    private static final String ERR_MSG_APIGEE_FAIL_WITH_ERR_CODE = "Call to Apigee Spec endpoint failed with error code : {}";
    private static final String ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG = "Call to Apigee Spec endpoint failed with error message : {}";
    private static final String ERR_MSG_APIGEE_FAIL = "Call to Apigee Spec endpoint failed with error message : ";
    private static final String ERR_CODE_APIGEE_FAIL = "Call to Apigee Spec endpoint failed with error code : ";
    private static final String ERR_MSG_OAUTH_FAIL_WITH_ERR_CODE = "Call to oAuth token endpoint failed with error code : {}";
    private static final String ERR_CODE_OAUTH_FAIL = "Call to oAuth token endpoint failed with error code : ";

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

        LOGGER.info("Calling Apigee endpoint to get list of specifications...");

        try {
            String accessToken = getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(AUTHORIZATION, BEARER + accessToken);

            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<ContentsList> response = restTemplate.exchange(config.getAllSpecUrl().trim(), HttpMethod.GET, request, ContentsList.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                return response.getBody().getContents();
            } else {
                LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_ERR_CODE, response.getStatusCode());
                throw new ApigeeServiceException(ERR_CODE_APIGEE_FAIL + response.getStatusCode(), response.getStatusCode());
            }

        } catch (HttpClientErrorException ex) {

            LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG, ex.getMessage());
            throw new ApigeeServiceException(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG, ex.getStatusCode());
        } catch (HttpServerErrorException ex) {

            LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG, ex.getMessage());
            throw new ApigeeServiceException(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG + ex.getMessage(), ex.getStatusCode());
        } catch (Exception ex) {

            LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG, ex.getMessage());
            throw new ApigeeServiceException(ERR_MSG_APIGEE_FAIL + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    public String getSpecification(final String specificationId) {

        LOGGER.info("Calling Apigee endpoint to get specification for id : {}", specificationId);

        try {
            URI uri = UriComponentsBuilder.fromHttpUrl(config.getSingleSpecUrl() ).build(specificationId);
            String accessToken = getAccessToken();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(AUTHORIZATION, BEARER + accessToken);

            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(uri.getPath(), HttpMethod.GET, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                return response.getBody();
            } else {
                LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_ERR_CODE, response.getStatusCode());
                throw new ApigeeServiceException(ERR_CODE_APIGEE_FAIL + response.getStatusCode(), response.getStatusCode());
            }

        } catch (HttpClientErrorException ex) {

            LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG, ex.getMessage());
            throw new ApigeeServiceException(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG, ex.getStatusCode());
        } catch (HttpServerErrorException ex) {

            LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG, ex.getMessage());
            throw new ApigeeServiceException(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG + ex.getMessage(), ex.getStatusCode());
        } catch (Exception ex) {

            LOGGER.error(ERR_MSG_APIGEE_FAIL_WITH_ERR_MSG, ex.getMessage());
            throw new ApigeeServiceException(ERR_MSG_APIGEE_FAIL + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private String getAccessToken() throws Exception {

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
        ResponseEntity<Token> response = restTemplate.exchange(config.getTokenUrl().trim(), HttpMethod.POST, request, Token.class);

        if (response.getStatusCode() == HttpStatus.OK) {

            return response.getBody().getAccessToken();
        } else {

            LOGGER.error(ERR_MSG_OAUTH_FAIL_WITH_ERR_CODE, response.getStatusCode());
            throw new RuntimeException(ERR_CODE_OAUTH_FAIL + response.getStatusCode());
        }
    }
}
