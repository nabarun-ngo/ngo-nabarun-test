package ngo.nabarun.test.ngo_nabarun_test.hooks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import com.microsoft.playwright.*;

import com.microsoft.playwright.options.ScreenshotType;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utils.CommonUtils;

public class TestHooks {

	private final ScenarioContext scenarioContext;
	private Browser browser;
	private BrowserContext context;

	public TestHooks(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@BeforeAll()
	public static void beforeTest() {
	}

	@Before()
	public void beforeScenario(Scenario scenario) {
		scenarioContext.reset();
		Playwright playwright = Playwright.create();

		String browserName = Configs.BROWSER.toLowerCase();
		BrowserType.LaunchOptions launchOptions = switch (browserName) {
			case "chrome" -> new BrowserType.LaunchOptions().setChannel("chrome");
			case "edge" -> new BrowserType.LaunchOptions().setChannel("msedge");
			default -> throw new IllegalArgumentException("Unexpected browser: " + browserName);
		};

		boolean headless = CommonUtils.getEnvProperty("headless", "N").equals("Y");
		launchOptions.setHeadless(headless);
		launchOptions.setArgs(List.of("--start-maximized"));
		launchOptions.setSlowMo(500);

		browser = playwright.chromium().launch(launchOptions);
		context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

		Page page = context.newPage();
		page.setDefaultTimeout(Configs.IMPLICIT_WAIT * 1000);
		scenarioContext.setPage(page);

	}

	@BeforeStep
	public void beforeStep(Scenario scenario) {
	}

	@AfterStep
	public void afterStep(Scenario scenario) {
		// CommonHelpers.sanitizeFileName(step.getText())
	}

	@After
	public void afterScenario(Scenario scenario) throws InterruptedException, IOException {
		Page page = scenarioContext.getPage();
		if (scenario.isFailed()) {
			byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG));
			scenario.attach(screenshot, "image/png", "error_screenshot");
			byte[] logs = Files.readAllBytes(new File("logs/test.log").toPath());
			scenario.attach(logs, "text/plain", "error_log");
		}
		page.close();
		context.close();
		browser.close();
	}

	@AfterAll()
	public static void afterTest() {

	}

}
