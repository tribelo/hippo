package uk.nhs.digital.apispecs;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import uk.nhs.digital.apispecs.config.ApigeeConfig;
import uk.nhs.digital.apispecs.config.Authentication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApigeeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApigeeService.class);

    private static final String ERROR_MSG_1 = "Call to Apigee Spec endpoint failed with error code : {}";
    private static final String ERROR_MSG_2 = "Call to Apigee Spec endpoint failed with error message : {}";
    private static final String ERROR_MSG_3 = "Call to Apigee Spec endpoint failed with error message : ";
    private static final String ERROR_MSG_4 = "Call to Apigee Spec endpoint failed with error code : ";
    private static final String ERROR_MSG_5 = "Call to oAuth token endpoint failed with error code : {}";
    private static final String ERROR_MSG_6 = "Call to oAuth token endpoint failed with error code : ";

    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic ";
    private static final String BEARER = "Bearer ";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String GRANT_TYPE = "grant_type";
    private static final String MFA_TOKEN = "mfa_token";

    private RestTemplate restTemplate;
    private ApigeeConfig config;

    public ApigeeService(RestTemplate restTemplate,ApigeeConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    public List<SpecContent> apigeeSpecsStatuses() throws ApigeeServiceException {

        LOGGER.info("Calling Apigee endpoint to get list of specifications...");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set(AUTHORIZATION, BEARER + getAccessToken());

            HttpEntity<?> request = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(config.getSpecUrl().trim(), HttpMethod.GET, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {

                return retrieveSpecification(response.getBody());
            } else {
                LOGGER.error(ERROR_MSG_1, response.getStatusCode());
                throw new ApigeeServiceException(ERROR_MSG_4 + response.getStatusCode(), response.getStatusCode());
            }

        } catch (HttpClientErrorException ex) {

            LOGGER.error(ERROR_MSG_2, ex.getMessage());
            throw new ApigeeServiceException(ERROR_MSG_2, ex.getStatusCode());
        } catch (HttpServerErrorException ex) {

            LOGGER.error(ERROR_MSG_2, ex.getMessage());
            throw new ApigeeServiceException(ERROR_MSG_2 + ex.getMessage(), ex.getStatusCode());
        } catch (Exception ex) {

            LOGGER.error(ERROR_MSG_2, ex.getMessage());
            throw new ApigeeServiceException(ERROR_MSG_3 + ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

    private List<SpecContent> retrieveSpecification(String response) {
        List<SpecContent> specifications = new ArrayList<SpecContent>();
        JSONObject cobj =  new JSONObject(response);

        JSONArray arr = (JSONArray) cobj.get("contents");
        for (Object obj : arr) {
            JSONObject content = (JSONObject) obj;
            specifications.add(new SpecContent(content.get("id").toString(), content.get("name").toString(), content.get("modified").toString(), content.get("self").toString()));
        }

        return specifications;
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
        bodyParamMap.add(MFA_TOKEN, auth.googleAuthenticatorCode(config.getOtpKey()));

        HttpEntity<?> request = new HttpEntity<>(bodyParamMap,headers);
        ResponseEntity<String> response = restTemplate.exchange(config.getTokenUrl().trim(), HttpMethod.POST, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {

            JSONObject obj = new JSONObject(response.getBody());
            return obj.get(ACCESS_TOKEN).toString();
        } else {
            LOGGER.error(ERROR_MSG_5, response.getStatusCode());
            throw new RuntimeException(ERROR_MSG_6 + response.getStatusCode());
        }

    }

}
