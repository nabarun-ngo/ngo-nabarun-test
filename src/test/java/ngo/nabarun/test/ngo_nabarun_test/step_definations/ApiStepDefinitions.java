package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.auth0.client.auth.AuthAPI;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.TokenHolder;
import com.auth0.net.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Map;

public class ApiStepDefinitions {

    private static final Logger logger = LogManager.getLogger(ApiStepDefinitions.class);
    private final ScenarioContext scenarioContext;
    private APIResponse lastResponse;
    private Map<String, String> tokenJar = new HashMap<>();
    private String authToken;

    public ApiStepDefinitions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Given("^I login with \"(.+)\" user using API$")
    public void i_performed_login_with_an_user_having_role(String email) throws Auth0Exception {
        if (!tokenJar.containsKey(email)) {
            logger.info("Logging in with user: " + email);
            AuthAPI auth = Auth0Client.authAPI();
            Response<TokenHolder> token = auth.login(email, Configs.TEST_DEFAULTPASSWORD.toCharArray())
                    .setAudience("https://nabarun.resourceserver.api")
                    .execute();
            tokenJar.put(email, token.getBody().getAccessToken());

        }
        this.authToken = tokenJar.get(email);
    }

    @When("I send a POST request to {string} with payload:")
    public void i_send_a_post_request_to_with_payload(String endpoint, String payload) {
        String resolvedEndpoint = DataUtils.resolveData(endpoint, scenarioContext);
        String baseUrl = Configs.API_BASE_URL.endsWith("/") ? Configs.API_BASE_URL : Configs.API_BASE_URL + "/";
        String endpointPath = resolvedEndpoint.startsWith("/") ? resolvedEndpoint.substring(1) : resolvedEndpoint;
        String fullUrl = baseUrl + endpointPath;
        String resolvedPayload = DataUtils.resolveData(payload, scenarioContext);

        logger.info("Sending POST request to: " + fullUrl);
        logger.debug("Request Payload: " + resolvedPayload);

        lastResponse = scenarioContext.getRequestContext().post(fullUrl, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer " + authToken)
                .setData(resolvedPayload));
        logger.info("Response status: " + lastResponse.status());
        logger.debug("Response body: " + lastResponse.text());
    }

    @When("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
        String resolvedEndpoint = DataUtils.resolveData(endpoint, scenarioContext);
        String baseUrl = Configs.API_BASE_URL.endsWith("/") ? Configs.API_BASE_URL : Configs.API_BASE_URL + "/";
        String endpointPath = resolvedEndpoint.startsWith("/") ? resolvedEndpoint.substring(1) : resolvedEndpoint;
        String fullUrl = baseUrl + endpointPath;

        logger.info("Sending GET request to: " + fullUrl);

        lastResponse = scenarioContext.getRequestContext().get(fullUrl, RequestOptions.create()
                .setHeader("Authorization", "Bearer " + authToken));

        logger.info("Response status: " + lastResponse.status());
        logger.debug("Response body: " + lastResponse.text());
    }

    @When("I extract data from response using JSON token {string} and store it as {string}")
    public void i_extract_data_from_response_using_json_token_and_store_it_as(String jsonPath, String variableName) {
        assertNotNull(lastResponse, "Last response is null, cannot extract data");
        String responseBody = lastResponse.text();
        try {
            String value = DataUtils.extractValueByPath(responseBody, jsonPath);
            scenarioContext.setCustomValue(variableName, value);
            logger.info("Extracted value '" + value + "' from path '" + jsonPath + "' and stored as variable '"
                    + variableName + "'");
        } catch (Exception e) {
            logger.error("Failed to extract data from response. Error: " + e.getMessage());
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

    @Then("The API response should contain a valid donation ID")
    public void the_api_response_should_contain_a_valid_donation_id() throws Exception {
        String responseBody = lastResponse.text();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseBody);

        // Assuming response returns the created donation object with an ID
        // or a structure containing ID
        JsonNode idNode = jsonNode.get("id");
        if (idNode == null && jsonNode.get("data") != null) {
            idNode = jsonNode.get("data").get("id");
        }

        assertNotNull(idNode, "Donation ID not found in response: " + responseBody);
        assertFalse(idNode.asText().isEmpty(), "Donation ID is empty");
    }

    @Then("The API response should contain error message {string}")
    public void the_api_response_should_contain_error_message(String expectedMessage) throws Exception {
        String responseBody = lastResponse.text();
        assertTrue(responseBody.contains(expectedMessage),
                "Expected error message '" + expectedMessage + "' not found in response: " + responseBody);
    }
}
