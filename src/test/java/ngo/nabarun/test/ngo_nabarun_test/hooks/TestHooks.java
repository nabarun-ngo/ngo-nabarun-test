package ngo.nabarun.test.ngo_nabarun_test.hooks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
import ngo.nabarun.test.ngo_nabarun_test.utilities.DevToolsUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class TestHooks {
	private static final Logger logger = LogManager.getLogger(TestHooks.class);
	private final ScenarioContext scenarioContext;
	private Browser browser;
	private BrowserContext context;
	private final ThreadLocal<Long> scenarioStartTime = new ThreadLocal<>();
	private final ThreadLocal<Long> stepStartTime = new ThreadLocal<>();

	public TestHooks(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@BeforeAll()
	public static void beforeTest() {
	}

	@Before()
	public void beforeScenario(Scenario scenario) {
		scenarioStartTime.set(System.currentTimeMillis());
		logger.info("******************************************************************************************");
		logger.info("Scenario Started: " + scenario.getName());
		logger.info("Scenario Tags: " + scenario.getSourceTagNames());
		logger.info("******************************************************************************************");
		scenarioContext.reset();
		Playwright playwright = Playwright.create();

		String browserName = Configs.BROWSER.toLowerCase();
		BrowserType.LaunchOptions launchOptions = switch (browserName) {
			case "chrome" -> new BrowserType.LaunchOptions().setChannel("chrome");
			case "edge" -> new BrowserType.LaunchOptions().setChannel("msedge");
			default -> throw new IllegalArgumentException("Unexpected browser: " + browserName);
		};

		launchOptions.setHeadless(Configs.IS_HEADLESS);
		launchOptions.setArgs(List.of("--start-maximized"));
		launchOptions.setSlowMo(500);

		logger.info("Launching browser: " + browserName + " in " + (Configs.IS_HEADLESS ? "headless" : "headed")
				+ " mode.");
		browser = playwright.chromium().launch(launchOptions);
		context = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));

		Page page = context.newPage();
		logger.info("New page created and timeout set to " + Configs.IMPLICIT_WAIT + "ms");
		page.setDefaultTimeout(Configs.IMPLICIT_WAIT);
		scenarioContext.setPage(page);
		DevToolsUtility devToolsUtility = new DevToolsUtility(scenarioContext);
		devToolsUtility.enableConsoleLogging(false);
		devToolsUtility.enableNetworkLogging(true);
	}

	@BeforeStep
	public void beforeStep(Scenario scenario) {
		stepStartTime.set(System.currentTimeMillis());
	}

	@AfterStep
	public void afterStep(Scenario scenario) {
		long duration = System.currentTimeMillis() - stepStartTime.get();
		logger.info("Step " + scenario.getStatus() + " | Duration: " + duration + "ms");
	}

	@After
	public void afterScenario(Scenario scenario) throws InterruptedException, IOException {
		long duration = System.currentTimeMillis() - scenarioStartTime.get();
		Page page = scenarioContext.getPage();
		String scenarioName = ThreadContext.get("scenarioName");
		if (scenario.isFailed()) {
			logger.error("Scenario FAILED. Capturing screenshot.");
			byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG));
			scenario.attach(screenshot, "image/png", "error_screenshot");
			Files.write(Paths.get("target/logs/" + scenarioName + ".png"), screenshot);
			File logFile = new File("target/logs/" + scenarioName + ".log");
			if (logFile.exists()) {
				byte[] logs = Files.readAllBytes(logFile.toPath());
				scenario.attach(logs, "text/plain", "error_log");
			}
		}
		logger.info("******************************************************************************************");
		logger.info("Scenario Finished: " + scenario.getName());
		logger.info("Scenario Status: " + scenario.getStatus());
		logger.info("Total Duration: " + duration + "ms");
		logger.info("******************************************************************************************");
		logger.info("Closing browser and cleaning up context.");
		page.close();
		context.close();
		browser.close();
		scenarioStartTime.remove();
		stepStartTime.remove();
	}

	@AfterAll()
	public static void afterTest() {

	}

}
