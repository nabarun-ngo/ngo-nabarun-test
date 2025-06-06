package ngo.nabarun.test.ngo_nabarun_test.hooks;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import ngo.nabarun.test.ngo_nabarun_test.config.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DevToolsUtility;

public class TestHooks {

	private ScenarioContext scenarioContext;

	public TestHooks(ScenarioContext scenarioContext) {
		this.scenarioContext = scenarioContext;
	}

	@BeforeAll()
	public static void beforeTest() {
	}

	@Before()
	public void beforeScenario(Scenario scenario) {
		scenarioContext.reset();
		Map<String, Object> prefs = new HashMap<>();
		prefs.put("profile.default_content_setting_values.notifications", 1); // 1 = Allow, 2 = Block
		String headless=System.getProperty("headless","N");

		ChromeOptions chrome_options = new ChromeOptions();
		chrome_options.setExperimentalOption("prefs", prefs);
		chrome_options.addArguments("--no-sandbox");
		chrome_options.addArguments("--disable-dev-shm-usage");
		
        EdgeOptions edge_options = new EdgeOptions();
        edge_options.setExperimentalOption("prefs", prefs);
        edge_options.addArguments("--no-sandbox");
        edge_options.addArguments("--disable-dev-shm-usage");
		
        if(headless.equals("Y")) {
            chrome_options.addArguments("--headless");
            edge_options.addArguments("--headless");
        }
		String browser = Configs.BROWSER;
		WebDriver driver = switch (browser.toUpperCase()) {
		case "CHROME" -> new ChromeDriver(chrome_options);
		case "EDGE" -> new EdgeDriver(edge_options);
		default -> throw new IllegalArgumentException("Unexpected value: " + browser.toUpperCase());
		};
		driver.manage().timeouts()
				.implicitlyWait(Duration.ofSeconds(Configs.IMPLICIT_WAIT));
		driver.manage().window().maximize();
		scenarioContext.setDriver(driver);
		
        DevToolsUtility devToolsUtility = new DevToolsUtility(driver);
        devToolsUtility.enableNetworkLogging();
        devToolsUtility.enableConsoleLogging();
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
		if(scenario.isFailed()) {
			byte[] screenshot = ((TakesScreenshot) scenarioContext.getDriver()).getScreenshotAs(OutputType.BYTES);
			scenario.attach(screenshot, "image/png", "error_screenshot");	
			byte[] logs=Files.readAllBytes(new File("logs/test.log").toPath());
			scenario.attach(logs, "text/plain", "error_log");	
		}
		scenarioContext.getDriver().quit();
	}

	@AfterAll()
	public static void afterTest() {
		
	}
	

   
}
