package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.Response;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utilities.Auth0Client;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiStepDefinitions {

    private static final Logger logger = LogManager.getLogger(ApiStepDefinitions.class);
    private final ScenarioContext scenarioContext;
    private APIResponse lastResponse;
    private final Map<String, String> tokenJar = new HashMap<>();
    private String authToken;

    public ApiStepDefinitions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Given("^I login with \"(.+)\" user using API$")
    public void i_performed_login_with_an_user_having_role(String email) throws Auth0Exception {
        if (!tokenJar.containsKey(email)) {
            logger.info("Logging in with user: {}", email);
            AuthAPI auth = Auth0Client.authAPI();
            Response<TokenHolder> token = auth.login(email, Configs.TEST_DEFAULTPASSWORD.toCharArray())
                    .setAudience("https://nabarun.resourceserver.api")
                    .execute();
            tokenJar.put(email, token.getBody().getAccessToken());

        }
        this.authToken = tokenJar.get(email);
    }

    @When("^I send a (POST|PUT|PATCH) request to \"(.+)\" with payload:$")
    public void i_send_a_post_request_to_with_payload(String method, String endpoint, String payload) {
        String resolvedEndpoint = DataUtils.resolveData(endpoint, scenarioContext);
        String baseUrl = Configs.API_BASE_URL.endsWith("/") ? Configs.API_BASE_URL : Configs.API_BASE_URL + "/";
        String endpointPath = resolvedEndpoint.startsWith("/") ? resolvedEndpoint.substring(1) : resolvedEndpoint;
        String fullUrl = baseUrl + endpointPath;

        String resolvedPayload = DataUtils.resolveData(payload, scenarioContext);

        logger.info("Sending {} request to: {}", method, fullUrl);
        logger.info("Request Payload: {}", resolvedPayload);
        RequestOptions requestOptions = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer " + authToken)
                .setData(resolvedPayload);
        switch (method.toUpperCase()) {
            case "POST":
                lastResponse = scenarioContext.getRequestContext().post(fullUrl, requestOptions);
                break;
            case "PUT":
                lastResponse = scenarioContext.getRequestContext().put(fullUrl, requestOptions);
                break;
            case "PATCH":
                lastResponse = scenarioContext.getRequestContext().patch(fullUrl, requestOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        logger.info("Response status: {}", lastResponse.status());
        logger.info("Response body: {}", lastResponse.text());
    }

    @When("^I send a (GET|DELETE) request to \"(.+)\"$")
    public void i_send_a_get_request_to(String method, String endpoint) {
        String resolvedEndpoint = DataUtils.resolveData(endpoint, scenarioContext);
        String baseUrl = Configs.API_BASE_URL.endsWith("/") ? Configs.API_BASE_URL : Configs.API_BASE_URL + "/";
        String endpointPath = resolvedEndpoint.startsWith("/") ? resolvedEndpoint.substring(1) : resolvedEndpoint;
        String fullUrl = baseUrl + endpointPath;

        logger.info("Sending {} request to: {}",method, fullUrl);
        RequestOptions requestOptions = RequestOptions.create()
                .setHeader("Authorization", "Bearer " + authToken);
        switch (method.toUpperCase()) {
            case "GET":
                lastResponse = scenarioContext.getRequestContext().get(fullUrl, requestOptions);
                break;
            case "DELETE":
                lastResponse = scenarioContext.getRequestContext().delete(fullUrl, requestOptions);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
        logger.info("Response status: {}" ,lastResponse.status());
        logger.debug("Response body: {}", lastResponse.text());
    }

    @When("I extract data from response using JSON token {string} and store it as {string}")
    public void i_extract_data_from_response_using_json_token_and_store_it_as(String jsonPath, String variableName) {
        assertNotNull(lastResponse, "Last response is null, cannot extract data");
        String responseBody = lastResponse.text();
        try {
            String value = DataUtils.extractValueByPath(responseBody, jsonPath);
            scenarioContext.setCustomValue(variableName, value);
            logger.info("Extracted value '{}' from path '{}' and stored as variable '{}'", value, jsonPath, variableName);
        } catch (Exception e) {
            logger.error("Failed to extract data from response. Error: {}", e.getMessage());
            fail("Failed to extract data from response using JSONPath '" + jsonPath + "'. Response: " + responseBody
                    + ". Error: " + e.getMessage());
        }
    }

    @Then("The API response status code should be {int}")
    public void the_api_response_status_code_should_be(Integer expectedStatusCode) {
        assertNotNull(lastResponse, "API response is null");
        assertEquals(expectedStatusCode, lastResponse.status(),
                "Unexpected status code. Response: " + lastResponse.text());
    }

    @Then("The API response should contain error message {string}")
    public void the_api_response_should_contain_error_message(String expectedMessage) throws Exception {
        String responseBody = lastResponse.text();
        assertTrue(responseBody.contains(expectedMessage),
                "Expected error message '" + expectedMessage + "' not found in response: " + responseBody);
    }

    @Then("The API response attribute {string} should be {string}")
    public void the_api_response_attribute_should_be(String jsonPath, String expectedValue) {
        assertNotNull(lastResponse, "Last response is null, cannot validate attribute");
        String responseBody = lastResponse.text();
        String resolvedExpectedValue = DataUtils.resolveData(expectedValue, scenarioContext);
        try {
            String actualValue = DataUtils.extractValueByPath(responseBody, jsonPath);
            assertEquals(resolvedExpectedValue, actualValue, "Value mismatch for attribute: " + jsonPath);
            logger.info("Validated attribute '" + jsonPath + "' has expected value '" + resolvedExpectedValue + "'");
        } catch (Exception e) {
            logger.error("Failed to validate attribute. Error: " + e.getMessage());
            fail("Failed to validate attribute '" + jsonPath + "'. Response: " + responseBody + ". Error: " + e.getMessage());
        }
    }

    @Then("The API response should have the following attributes")
    public void the_api_response_should_have_the_following_attributes(List<Map<String, String>> attributes) {
        assertNotNull(lastResponse, "Last response is null, cannot validate attributes");
        String responseBody = lastResponse.text();
        for (Map<String, String> row : attributes) {
            String jsonPath = row.get("Attribute");
            String expectedValue = row.get("Value");
            String resolvedExpectedValue = DataUtils.resolveData(expectedValue, scenarioContext);
            try {
                String actualValue = DataUtils.extractValueByPath(responseBody, jsonPath);
                assertEquals(resolvedExpectedValue, actualValue, "Value mismatch for attribute: " + jsonPath);
                logger.info("Validated attribute '" + jsonPath + "' has expected value '" + resolvedExpectedValue + "'");
            } catch (Exception e) {
                logger.error("Failed to validate attribute '" + jsonPath + "'. Error: " + e.getMessage());
                fail("Failed to validate attribute '" + jsonPath + "'. Response: " + responseBody + ". Error: " + e.getMessage());
            }
        }
    }
}
