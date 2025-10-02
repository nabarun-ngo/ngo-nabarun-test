package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.microsoft.playwright.options.WaitForSelectorState;
import org.junit.jupiter.api.Assertions;


import com.microsoft.playwright.*;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DevToolsUtility;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;
import ngo.nabarun.test.ngo_nabarun_test.utils.CommonUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CommonStepDefinitions {

	private final ControlLookup controlLookup;
	private final ScenarioContext scenarioContext;
	private final Page page;
	private final CommonPageObjects commonPageObjects;
	private final ElementHelper elementHelper;
	//private DataProvider dataProvider;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public CommonStepDefinitions(ScenarioContext sc, ElementHelper eh, DataProvider dp, CommonPageObjects commonPageObjects, ControlLookup controlLookup) {
		this.controlLookup = controlLookup;
		this.commonPageObjects = commonPageObjects;
		this.scenarioContext = sc;
		this.page = scenarioContext.getPage();
		this.elementHelper = eh;
		//this.dataProvider = dataProvider;
	}

	@Given("I have opened to Nabarun's web portal")
	public void that_i_am_on_nabarun_home_page() {
		String rootURL = Configs.ROOT_URL;
		page.navigate(rootURL);
	}

	@Given("^I (click|click and hold|scroll) on \"(.+)\" (button|link|text|textbox) at \"(.+)\" (page|accordion)$")
	public void i_clicked_on_button(String actionName, String elementName, String elementType, String pageName,
			String pageType) throws Exception {
		Locator element = controlLookup.getLookupElement(elementName, elementType, pageName, pageType);

		switch (actionName) {
		case "click" -> element.click();
		case "scroll" -> element.scrollIntoViewIfNeeded();
		case "click and hold" -> {
			element.hover();
			// Playwright does not have direct click-and-hold, but you can use dispatchEvent if needed
		}
		default -> throw new IllegalStateException("Invalid action : " + actionName);
		}
		;
	}

    @Given("^I click on \"(.+)\" (button|link|text|textbox) at \"(.+)\" (page|accordion) and wait for new window to load$")
    public void i_clicked_wait_for_new_page(String elementName, String elementType, String pageName,
                                    String pageType) throws Exception {
        Locator element = controlLookup.getLookupElement(elementName, elementType, pageName, pageType);
        Page newWindowPage = page.context().waitForPage(element::click);
        scenarioContext.setPage(newWindowPage);
        System.out.println(newWindowPage.url());
    }

	@Then("^I (enter|select|click|upload) \"([^\"]*)\" on \"([^\"]*)\" (textbox|dropdown|radio|datepicker|textarea|fileinput) at \"([^\"]*)\" (page|accordion)$")
	public void iEnterOnTextboxAtAccordion(String actionName, String rawValue, String elementName, String elementType,
			String pageName, String pageType) throws Throwable {
		Locator element = controlLookup.getLookupElement(elementName, elementType, pageName, pageType);
		String value = DataUtils.replacePlaceholders(rawValue);
		switch (actionName.toUpperCase()) {
            case "ENTER" -> {
                //elementHelper.scrollIntoView(element);
                element.clear();
                element.fill(value);
            }
            case "SELECT" -> {
                switch (elementType.toLowerCase()) {
                    case "dropdown" -> elementHelper.selectMatOption(element, value);
                    case "datepicker" -> elementHelper.selectMatDate(element, sdf.parse(value));
                }
            }
            case "CLICK" -> elementHelper.clickRadioOption(element, value);
            case "UPLOAD" -> {
                String filePath = CommonUtils.getFileFromResources(value);
                element.setInputFiles(Path.of(filePath));
            }
            default -> throw new IllegalStateException("Invalid action : " + actionName);
        }
	}

    @Then("I must be landed to {string} screen")
	public void i_must_be_landed_to_screen(String screenName) {
        assertThat(commonPageObjects.PageHeader()).containsText(screenName);
    }

	@Then("I wait for loading to complete")
	public void i_wait_for_loading_to_complete() {
	    // Wait for the loader to disappear using Playwright
	    page.waitForSelector(commonPageObjects.PageLoaderSelector, new Page.WaitForSelectorOptions()
	        .setState(WaitForSelectorState.HIDDEN)

	        .setTimeout(60000));
	}

	@Then("^the \"(.+)\" (button|section) should be displayed at \"(.+)\" page$")
	public void should_be_displayed(String elementName, String elementType, String pageName) {

        Locator element = controlLookup.getLookupElement(elementName, elementType, pageName, "page");
        assertThat(commonPageObjects.PageHeader()).isVisible();
	}

	@Then("^I wait for (\\d+) seconds$")
	public void iWaitForSeconds(int wait) throws Throwable {
		Thread.sleep(wait* 1000L);
	}

	@Then("^I opened the accordion of index (\\d+) at \"([^\"]*)\" (page|accordion)$")
	public void iOpenedTheAccordionAtIndex(int index,String pageName,String pageType) throws Throwable {
		Locator parent = null;
		if(pageType.equalsIgnoreCase("accordion")) {
			parent= controlLookup.getAccordionMapping(pageName);
		}
		elementHelper.click(commonPageObjects.getAccordion(index,parent));
	}
	
	@Then("^I map \"([^\"]*)\" element as \"([^\"]*)\" accordion$")
	public void iMapCreateDonationAccordionAsAccordion(String selector, String accordionName) throws Throwable {
		Locator element = page.locator(selector);
		controlLookup.setAccordionMapping(accordionName, element);
	}

	@Then("^I wait for following text to display at \"(.+)\" (page|accordion)$")
	public void iWaitForFollowingTextToDisplay(String pageName,String pageType,DataTable table) throws Throwable {
	    List<Map<String, String>> rows = table.asMaps(String.class, String.class);
	    for (Map<String, String> columns : rows) {
	        String content = columns.get("Expected_Content");
			Locator element = controlLookup.getLookupElement(content, "text", pageName, pageType);
			elementHelper.elementWait(element,30);
			Assertions.assertTrue(element.isVisible(),"Element "+content+" is not displayed.");
	    }
	}
	
}
