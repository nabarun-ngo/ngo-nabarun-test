package ngo.nabarun.test.ngo_nabarun_test.step_definations;

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
		
		ChromeOptions chrome_options = new ChromeOptions();
		chrome_options.setExperimentalOption("prefs", prefs);
		
        EdgeOptions edge_options = new EdgeOptions();
        edge_options.setExperimentalOption("prefs", prefs);
		
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
	}

	@BeforeStep
	public void beforeStep(Scenario scenario) {

	}

	@AfterStep
	public void afterStep(Scenario scenario) {
		

		// CommonHelpers.sanitizeFileName(step.getText())
	}

	@After
	public void afterScenario(Scenario scenario) throws InterruptedException {
		if(scenario.isFailed()) {
			byte[] screenshot = ((TakesScreenshot) scenarioContext.getDriver()).getScreenshotAs(OutputType.BYTES);
			scenario.attach(screenshot, "image/png", "error");	
		}
		scenarioContext.getDriver().quit();
	}

	@AfterAll()
	public static void afterTest() {
	}
}
