package ngo.nabarun.test.ngo_nabarun_test.pages;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ngo.nabarun.test.ngo_nabarun_test.config.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.helpers.TestDataHelper;
import ngo.nabarun.test.ngo_nabarun_test.models.User;
import ngo.nabarun.test.ngo_nabarun_test.pages.objects.CommonPageObjects;

public class CommonPage {
	private static Map<String, String> pageURLMapping = new HashMap<>();

	static {
		pageURLMapping.put("HOME", "/");
	}

	protected ScenarioContext scenarioContext;
	protected WebDriver driver;
	private CommonPageObjects pageObject;
	protected TestDataHelper dataHelper;

	public CommonPage(ScenarioContext scenarioContext, CommonPageObjects pageObject, TestDataHelper dataHelper) {
		this.scenarioContext = scenarioContext;
		this.driver = scenarioContext.getDriver();
		this.pageObject = pageObject;
		this.dataHelper = dataHelper;
	}

	public void goToPage(String pageName) {
		String rootURL = Configs.ROOT_URL;
		String pageURL = rootURL + pageURLMapping.get(pageName);
		driver.get(pageURL);
	}

	public void performActionOnElement(String actionName, String buttonName) {

		WebElement element = switch (buttonName.toUpperCase()) {
		case "TEST" -> pageObject.ContinueWithEmailButton.get();
		default -> driver.findElement(By.xpath("//*[normalize-space(text())= '" + buttonName + "']"));
		};

		switch (actionName.toUpperCase()) {
		case "CLICK" -> element.click();
		case "CLICK AND HOLD" -> {
			Actions action = new Actions(driver);
			action.moveToElement(element).clickAndHold().build().perform();
		}
		default -> throw new IllegalStateException("Invalid action : " + actionName);
		}
		;

	}

	public void loginToNabarun(String loginOption, String loginId, String loginIdType) {
		String email;
		String password = Configs.TEST_DEFAULTPASSWORD;
		if (loginIdType.equalsIgnoreCase("role")) {
			List<User> users=dataHelper.getUsersByRole(loginId);
			email = users.stream().findFirst().orElseThrow(()-> new RuntimeException("Unable to find users with role "+loginId)).getEmail();
		} else {
			email = loginId;
		}
		if (loginOption.equalsIgnoreCase("Password")) {
			scenarioContext.set(ContextKeys.Login_Option, loginOption);
			pageObject.ContinueWithPasswordButton.get().click();
			pageObject.LoginEmail.get().sendKeys(email);
			pageObject.LoginSubmit.get().click();
			pageObject.LoginPassword.get().sendKeys(password);
			pageObject.LoginSubmit.get().click();
		} else {
			throw new RuntimeException("LoginOption '" + loginOption + "' is not allowed.");
		}
	}

	public void switchToNewWindow() {
		String originalHandle = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles()) {
			if (!originalHandle.equalsIgnoreCase(handle)) {
				driver.switchTo().window(handle);
				scenarioContext.set(ContextKeys.Last_Window_Handle, originalHandle);
				scenarioContext.set(ContextKeys.Current_Window_Handle, handle);
				break;
			}
		}
	}

	public void backToPreviousWindow() {

	}

	public void checkPageHeader(String screenName) {
		String actualText = pageObject.PageHeader.get().getText();
		Assertions.assertEquals(screenName.toLowerCase(), actualText.toLowerCase());
	}

	public void waitForLoadingComplete() {
		int timeout = 60;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(t -> {
			Duration implicitWait = null;
			try {
				implicitWait = driver.manage().timeouts().getImplicitWaitTimeout();
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
				WebElement elm = driver.findElement(pageObject.PageLoaderLocator);
				return !(elm.isDisplayed());
			} catch (NoSuchElementException | StaleElementReferenceException e) {
				return true;
			} finally {
				driver.manage().timeouts().implicitlyWait(implicitWait);
			}
		});
	}
}
