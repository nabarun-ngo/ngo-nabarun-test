package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import ngo.nabarun.test.ngo_nabarun_test.config.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.CommonHelpers;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DevToolsUtility;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class CommonStepDefinitions {

	private ControlLookup controlLookup;
	private ScenarioContext scenarioContext;
	private WebDriver driver;
	private CommonPageObjects commonPageObjects;
	private ElementHelper elementHelper;
	private DataProvider dataProvider;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public CommonStepDefinitions(ScenarioContext scenarioContext, ControlLookup controlLookup,
			CommonPageObjects commonPageObjects, ElementHelper elementHelper, DataProvider dataProvider) {
		this.controlLookup = controlLookup;
		this.commonPageObjects = commonPageObjects;
		this.scenarioContext = scenarioContext;
		this.driver = scenarioContext.getDriver();
		this.elementHelper = elementHelper;
		this.dataProvider = dataProvider;
	}

	@Given("I have opened to Nabarun's web portal")
	public void that_i_am_on_nabarun_home_page() {
		String rootURL = Configs.ROOT_URL;
		driver.get(rootURL);
	}

	@Given("^I (click|click and hold|scroll) on \"(.+)\" (button|link|text|textbox) at \"(.+)\" (page|accordion)$")
	public void i_clicked_on_button(String actionName, String elementName, String elementType, String pageName,
			String pageType) throws Exception {
		WebElement element = controlLookup.getLookupElement(elementName, elementType, pageName, pageType);

		switch (actionName.toUpperCase()) {
		case "CLICK" -> elementHelper.click(element);
		case "SCROLL" -> elementHelper.scrollIntoView(element);
		case "CLICK AND HOLD" -> {
			//elementHelper.scrollIntoView(element);
			Actions action = new Actions(driver);
			action.moveToElement(element).clickAndHold().build().perform();
		}
		default -> throw new IllegalStateException("Invalid action : " + actionName);
		}
		;
	}

	@Then("^I (enter|select|click|upload) \"([^\"]*)\" on \"([^\"]*)\" (textbox|dropdown|radio|datepicker|textarea|fileinput) at \"([^\"]*)\" (page|accordion)$")
	public void iEnterOnTextboxAtAccordion(String actionName, String rawValue, String elementName, String elementType,
			String pageName, String pageType) throws Throwable {
		WebElement element = controlLookup.getLookupElement(elementName, elementType, pageName, pageType);
		String value = dataProvider.replacePlaceholders(rawValue);
		switch (actionName.toUpperCase()) {
		case "ENTER" -> {
			//elementHelper.scrollIntoView(element);
			element.clear();
			element.sendKeys(value);
		}
		case "SELECT" -> {
			switch (elementType.toLowerCase()) {
			case "dropdown" -> elementHelper.selectMatOption(element, value);
			case "datepicker" -> elementHelper.selectMatDate(element, sdf.parse(value));
			}
		}
		case "CLICK" -> elementHelper.clickRadioOption(element, value);
		case "UPLOAD" -> {
			String filePath = CommonHelpers.getFileFromResources(value);
			element.sendKeys(filePath);
		}
		default -> throw new IllegalStateException("Invalid action : " + actionName);
		}
		;
	}

	@Then("^I switch to the (new|previous) tab$")
	public void iCloseTheCurrentTabAndSwitchToTheOriginalTab(String type) {
		String originalHandle = driver.getWindowHandle();
		if (type.equalsIgnoreCase("new")) {
			for (String handle : driver.getWindowHandles()) {
				if (!originalHandle.equalsIgnoreCase(handle)) {
					driver.switchTo().window(handle);
					scenarioContext.set(ContextKeys.Last_Window_Handle, originalHandle);
					scenarioContext.set(ContextKeys.Current_Window_Handle, handle);
					DevToolsUtility devToolsUtility = new DevToolsUtility(driver);
					devToolsUtility.enableNetworkLogging();
					devToolsUtility.enableConsoleLogging();
					break;
				}
			}
		} else {
			// TODO Implement
		}
	}

	@Then("I must be landed to {string} screen")
	public void i_must_be_landed_to_screen(String screenName) {
		String actualText = commonPageObjects.PageHeader.get().getText();
		Assertions.assertEquals(screenName.toLowerCase(), actualText.toLowerCase());
	}

	@Then("I wait for loading to complete")
	public void i_wait_for_loading_to_complete() throws InterruptedException {
		Thread.sleep(2000);
		int timeout = 60;
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
		wait.until(t -> {
			Duration implicitWait = null;
			try {
				implicitWait = driver.manage().timeouts().getImplicitWaitTimeout();
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
				WebElement elm = driver.findElement(commonPageObjects.PageLoaderLocator);
				return !(elm.isDisplayed());
			} catch (NoSuchElementException | StaleElementReferenceException e) {
				return true;
			} finally {
				driver.manage().timeouts().implicitlyWait(implicitWait);
			}
		});
		Thread.sleep(2000);
	}

	@Then("^the \"(.+)\" (button|section) should be displayed at \"(.+)\" page$")
	public void should_be_displayed(String elementName, String elementType, String pageName) {
		WebElement element = controlLookup.getLookupElement(elementName, elementType, pageName, "page");
		Assertions.assertTrue(element.isDisplayed(),
				"Element " + elementName + " is not displayed on " + pageName + " page.");
	}

	@Then("^I wait for (\\d+) seconds$")
	public void iWaitForSeconds(int wait) throws Throwable {
		Thread.sleep(wait* 1000L);
	}

	@Then("^I opened the accordion of index (\\d+) at \"([^\"]*)\" (page|accordion)$")
	public void iOpenedTheAccordionAtIndex(int index,String pageName,String pageType) throws Throwable {
		SearchContext parent = null;
		if(pageType.equalsIgnoreCase("accordion")) {
			parent= controlLookup.getAccordionMapping(pageName);
		}
		elementHelper.click(commonPageObjects.getAccordion(index,parent));
	}
	
	@Then("^I map \"([^\"]*)\" element as \"([^\"]*)\" accordion$")
	public void iMapCreateDonationAccordionAsAccordion(String xpath, String accordionName) throws Throwable {
		WebElement element = driver.findElement(By.xpath(xpath));
		controlLookup.setAccordionMapping(accordionName, element);
	}

	@Then("^I wait for following text to display at \"(.+)\" (page|accordion)$")
	public void iWaitForFollowingTextToDisplay(String pageName,String pageType,DataTable table) throws Throwable {
	    List<Map<String, String>> rows = table.asMaps(String.class, String.class);
	    for (Map<String, String> columns : rows) {
	        String content = columns.get("Expected_Content");
			WebElement element = controlLookup.getLookupElement(content, "text", pageName, pageType);
			element=elementHelper.elementWait().until(ExpectedConditions.visibilityOf(element));
			Assertions.assertTrue(element.isDisplayed(),"Element "+content+" is not displayed.");
	    }
	}

}
