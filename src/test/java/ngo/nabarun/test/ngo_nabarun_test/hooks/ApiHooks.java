package ngo.nabarun.test.ngo_nabarun_test.hooks;

import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Playwright;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class ApiHooks {

    private final ScenarioContext scenarioContext;
    private Playwright playwright;
    private APIRequestContext requestContext;

    public ApiHooks(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    @Before("@api")
    public void setUpApi() {
        playwright = Playwright.create();
        requestContext = playwright.request().newContext(new APIRequest.NewContextOptions()
                .setIgnoreHTTPSErrors(true));
        scenarioContext.setRequestContext(requestContext);
    }

    @After("@api")
    public void tearDownApi() {
        if (requestContext != null) {
            requestContext.dispose();
        }
        if (playwright != null) {
            playwright.close();
        }
    }
}
