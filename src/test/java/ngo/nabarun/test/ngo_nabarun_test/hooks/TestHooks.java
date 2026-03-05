package ngo.nabarun.test.ngo_nabarun_test.hooks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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
		String scenarioName = scenario.getName().replaceAll("[^a-zA-Z0-9-]", "_");

		// Capture and save screenshot on failure
		if (scenario.isFailed()) {
			logger.error("Scenario FAILED. Capturing screenshot.");
			byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setType(ScreenshotType.PNG));
			Files.createDirectories(Paths.get("target/logs"));
			Files.write(Paths.get("target/logs/" + scenarioName + ".png"), screenshot);
		}

		// Attach ALL files under target/logs/ to the Cucumber report
		File logsDir = new File("target/logs");
		if (logsDir.exists() && logsDir.isDirectory()) {
			File[] logFiles = logsDir.listFiles();
			if (logFiles != null && logFiles.length > 0) {
				Arrays.sort(logFiles); // consistent ordering
				for (File file : logFiles) {
					if (!file.isFile())
						continue;
					try {
						byte[] content = Files.readAllBytes(file.toPath());
						String mimeType = resolveMimeType(file.getName());
						scenario.attach(content, mimeType, file.getName());
						logger.info("Attached to Cucumber report: " + file.getName() + " (" + mimeType + ")");
					} catch (IOException e) {
						logger.warn("Could not attach file: " + file.getName() + " - " + e.getMessage());
					}
				}
			} else {
				logger.warn("No files found in logs directory: " + logsDir.getAbsolutePath());
			}
		} else {
			logger.warn("Logs directory does not exist: " + logsDir.getAbsolutePath());
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

	private String resolveMimeType(String fileName) {
		String lower = fileName.toLowerCase();
		if (lower.endsWith(".png"))
			return "image/png";
		if (lower.endsWith(".jpg") || lower.endsWith(".jpeg"))
			return "image/jpeg";
		if (lower.endsWith(".log") || lower.endsWith(".txt"))
			return "text/plain";
		if (lower.endsWith(".json"))
			return "application/json";
		if (lower.endsWith(".xml"))
			return "application/xml";
		if (lower.endsWith(".html"))
			return "text/html";
		return "application/octet-stream";
	}

	@AfterAll()
	public static void afterTest() {
	}

}
