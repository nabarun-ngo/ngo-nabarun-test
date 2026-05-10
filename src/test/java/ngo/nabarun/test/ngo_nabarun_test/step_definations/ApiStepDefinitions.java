package ngo.nabarun.test.ngo_nabarun_test.step_definations;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

import static org.junit.jupiter.api.Assertions.*;

public class ApiStepDefinitions {

    private final ScenarioContext scenarioContext;
    private APIResponse lastResponse;
    private String baseUrl;

    public ApiStepDefinitions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Given("I set the base API URL to {string}")
    public void i_set_the_base_api_url_to(String url) {
        this.baseUrl = url;
    }

    @When("I send a POST request to {string} with payload:")
    public void i_send_a_post_request_to_with_payload(String endpoint, String payload) {
        String fullUrl = this.baseUrl + endpoint;
        lastResponse = scenarioContext.getRequestContext().post(fullUrl, RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setData(payload));
    }

    @Then("The API response status code should be {int}")
    public void the_api_response_status_code_should_be(Integer expectedStatusCode) {
        assertNotNull(lastResponse, "API response is null");
        assertEquals(expectedStatusCode, lastResponse.status(), "Unexpected status code. Response: " + lastResponse.text());
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
