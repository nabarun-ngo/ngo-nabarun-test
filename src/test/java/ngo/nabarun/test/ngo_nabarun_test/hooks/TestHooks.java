package ngo.nabarun.test.ngo_nabarun_test.hooks;

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
import ngo.nabarun.test.ngo_nabarun_test.utils.DBUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import ngo.nabarun.test.ngo_nabarun_test.utils.MemoryAppender;

public class TestHooks {
	private static final Logger logger = LogManager.getLogger(TestHooks.class);
	private final ScenarioContext scenarioContext;
	private Browser browser;
	private BrowserContext context;
	private Playwright playwright;
	private APIRequestContext requestContext;
	private final ThreadLocal<Long> scenarioStartTime = new ThreadLocal<>();
	private static final String SCREENSHOTS_DIR = "target/screenshots";

	public TestHooks(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@BeforeAll()
	public static void beforeTest() {
	}

	@Before()
	public void beforeScenario(Scenario scenario) {
		MemoryAppender.clear();
		try {
			Files.createDirectories(Paths.get(SCREENSHOTS_DIR));
		} catch (IOException e) {
			System.err.println(
					"CRITICAL: Failed to create screenshot directory: " + SCREENSHOTS_DIR + " - " + e.getMessage());
		}
		scenarioStartTime.set(System.currentTimeMillis());
		logger.info("******************************************************************************************");
		logger.info("Scenario Started: " + scenario.getName());
		logger.info("Scenario Tags: " + scenario.getSourceTagNames());
		logger.info("******************************************************************************************");
		scenarioContext.reset();
		playwright = Playwright.create();

		if (scenario.getSourceTagNames().contains("@api")) {
			logger.info("API test detected. Initializing API request context.");
			requestContext = playwright.request().newContext(new APIRequest.NewContextOptions()
					.setBaseURL(Configs.API_BASE_URL)
					.setIgnoreHTTPSErrors(true));
			scenarioContext.setRequestContext(requestContext);
		} else {
			String browserName = Configs.BROWSER.toLowerCase();
			BrowserType.LaunchOptions launchOptions = switch (browserName) {
				case "chrome" -> new BrowserType.LaunchOptions().setChannel("chrome");
				case "edge" -> new BrowserType.LaunchOptions().setChannel("msedge");
				default -> throw new IllegalArgumentException("Unexpected browser: " + browserName);
			};

			launchOptions.setHeadless(Configs.IS_HEADLESS);
			launchOptions.setArgs(List.of("--start-maximized"));
			launchOptions.setSlowMo(250);

			logger.info("Launching browser: " + browserName + " in " + (Configs.IS_HEADLESS ? "headless" : "headed")
					+ " mode.");
			browser = playwright.chromium().launch(launchOptions);
			context = browser.newContext(new Browser.NewContextOptions()
					.setPermissions(List.of("notifications"))
					.setViewportSize(null));

			Page page = context.newPage();
			logger.info("New page created and timeout set to " + Configs.IMPLICIT_WAIT + "ms");
			page.setDefaultTimeout(Configs.IMPLICIT_WAIT);
			scenarioContext.setPage(page);
			ngo.nabarun.test.ngo_nabarun_test.utils.StepState.setPage(page);
			DevToolsUtility devToolsUtility = new DevToolsUtility(scenarioContext);
			devToolsUtility.enableConsoleLogging(false);
			devToolsUtility.enableNetworkLogging(true);
		}
	}

	@BeforeStep
	public void beforeStep(Scenario scenario) {
	}

	@AfterStep
	public void afterStep(Scenario scenario) {
	}

	@After
	public void afterScenario(Scenario scenario) throws InterruptedException, IOException {
		long duration = System.currentTimeMillis() - scenarioStartTime.get();
		Page page = scenarioContext.getPage();
		String scenarioName = ThreadContext.get("scenarioName");

		logger.info("******************************************************************************************");
		logger.info("Scenario Finished: " + scenario.getName());
		logger.info("Scenario Status: " + scenario.getStatus());
		logger.info("Total Duration: " + duration + "ms");
		logger.info("******************************************************************************************");

		// Attach execution log captured by MemoryAppender
		String executionLog = MemoryAppender.getAndClearLog();
		if (!executionLog.isEmpty()) {
			scenario.attach(executionLog.getBytes(), "text/plain", "execution.log");
		}

		// Capture and save screenshot on failure (UI tests only)
		if (scenario.isFailed() && page != null) {
			logger.error("Scenario FAILED. Capturing screenshot.");
			byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG));
			// save screenshot
			Files.createDirectories(Paths.get(SCREENSHOTS_DIR));
			Files.write(Paths.get(SCREENSHOTS_DIR, scenarioName + ".png"), screenshot);
			// attach screenshot
			scenario.attach(screenshot, "image/png", scenarioName + ".png");
			logger.info("Attached screenshot to Cucumber report.");
		}

		logger.info("Closing Playwright and cleaning up context.");
		if (page != null)
			page.close();
		if (context != null)
			context.close();
		if (browser != null)
			browser.close();
		if (requestContext != null)
			requestContext.dispose();
		if (playwright != null)
			playwright.close();
		scenarioStartTime.remove();
		ngo.nabarun.test.ngo_nabarun_test.utils.StepState.clear();
	}

	@AfterAll()
	public static void afterTest() {
		DBUtils.close();
		logger.info("Database connection pool closed.");
	}
}
