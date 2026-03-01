package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.util.List;
import java.util.Map;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.*;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import ngo.nabarun.test.ngo_nabarun_test.utilities.SelfHealingLocator;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CommonStepDefinitions {

	private final ControlLookup controlLookup;
	private final ScenarioContext scenarioContext;
	private final CommonPageObjects commonPageObjects;
	private final ControlActions controlActions;
	// private DataProvider dataProvider;

	public CommonStepDefinitions(ScenarioContext sc, ControlActions ca, DataProvider dp,
			CommonPageObjects commonPageObjects, ControlLookup controlLookup) {
		this.controlLookup = controlLookup;
		this.commonPageObjects = commonPageObjects;
		this.scenarioContext = sc;
		this.controlActions = ca;
		// this.dataProvider = dataProvider;
	}

	@Given("I have opened to Nabarun's web portal")
	public void that_i_am_on_nabarun_home_page() {
		String rootURL = Configs.ROOT_URL;
		scenarioContext.getPage().navigate(rootURL);
	}

	@Given("^I (click|click and hold|scroll) on \"(.+)\" (button|link|text|textbox) at \"(.+)\" page$")
	public void i_clicked_on_button(String actionName, String elementName, String elementType, String pageName) {
		SelfHealingLocator element = controlLookup.getLookupElement(elementName, elementType, pageName);
		controlActions.executeAction(actionName, element.getLocator(), elementType, "");
	}

	@Given("^I click on \"(.+)\" (button|link|text|textbox) at \"(.+)\" (page) and wait for new window to load$")
	public void i_clicked_wait_for_new_page(String elementName, String elementType, String pageName,
			String pageType) {
		Page page = scenarioContext.getPage();
		SelfHealingLocator element = controlLookup.getLookupElement(elementName, elementType, pageName);
		Page newWindowPage = page.context().waitForPage(element.getLocator()::click);
		newWindowPage.setDefaultTimeout(Configs.IMPLICIT_WAIT);
		scenarioContext.setPage(newWindowPage);
		page.close();
	}

	@When("^I (enter|select|click|upload) \"([^\"]*)\" on \"([^\"]*)\" (textbox|dropdown|radio|datepicker|textarea|fileinput|multiselect) at \"([^\"]*)\" (page)$")
	public void iEnterOnTextboxAtAccordion(String actionName, String rawValue, String elementName, String elementType,
			String pageName, String pageType) throws Throwable {
		String effectiveType = "multiselect".equalsIgnoreCase(elementType) ? "dropdown" : elementType;
		SelfHealingLocator element = controlLookup.getLookupElement(elementName, effectiveType, pageName);
		String value = DataUtils.replacePlaceholders(rawValue);
		controlActions.executeAction(actionName, element.getLocator(), elementType, value);
	}

	@Then("^I (check|uncheck) \"([^\"]*)\" checkbox at \"([^\"]*)\" (page)$")
	public void iCheckOrUncheckCheckbox(String checkOrUncheck, String elementName, String pageName, String pageType) {
		SelfHealingLocator element = controlLookup.getLookupElement(elementName, "checkbox", pageName);
		if ("check".equalsIgnoreCase(checkOrUncheck)) {
			element.getLocator().check();
		} else {
			element.getLocator().uncheck();
		}
	}

	@Then("I must be landed to {string} screen")
	public void i_must_be_landed_to_screen(String screenName) {
		Locator header = commonPageObjects.PageHeader(screenName);
		assertThat(header).isVisible(
				new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
	}

	@Then("I wait for loading to complete")
	public void i_wait_for_loading_to_complete() {
		this.controlActions.waitUntilDisappear(commonPageObjects.PageLoaderSelector());
	}

	@Then("^the \"(.+)\" (button|section|checkbox) should be displayed at \"(.+)\" (page)$")
	public void should_be_displayed(String elementName, String elementType, String pageName, String pageType) {
		SelfHealingLocator element = controlLookup.getLookupElement(elementName, elementType, pageName);
		assertThat(element.getLocator()).isVisible(
				new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
	}

	@Then("^I wait for (\\d+) seconds$")
	public void iWaitForSeconds(int wait) throws Throwable {
		Thread.sleep(wait * 1000L);
	}

	@Then("^I perform advance search with the following fields$")
	public void iFindTheCorrectAccordionUsingAdvancedSearch(DataTable table) {
		Locator parent = this.commonPageObjects.Search_Container();
		List<Map<String, String>> fieldInputModels = table.asMaps(String.class, String.class);
		for (Map<String, String> fieldInputModel : fieldInputModels) {
			String fieldName = fieldInputModel.get("Field_Name");
			String fieldType = fieldInputModel.get("Field_Type");
			String actionType = fieldInputModel.get("Field_Action");
			String value = fieldInputModel.get("Field_Value");
			SelfHealingLocator element = controlLookup.getLookupElement(fieldName, fieldType, parent);
			assertThat(element.getLocator()).isVisible(
					new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
			controlActions.executeAction(actionType, element.getLocator(), fieldType, value);
		}
		commonPageObjects.Adv_Search_Submit.get().click();
	}

	@Then("^I perform search with \"(.+)\"")
	public void iFindTheCorrectAccordionUsingSearch(String searchText) {
		Locator searchField = this.commonPageObjects.Simple_Search_Input.get();
		searchField.fill(searchText);
	}

	@Given("^I click on \"(.+)\" (button|link|text|textbox) at \"(.+)\" page and collect \"(.+)\" from response of \"(.+)\" and store as \"(.+)\"$")
	public void i_click_and_collect(String elementName, String elementType, String pageName, String jsonPath,
			String urlPattern, String storeKey) {
		Page page = scenarioContext.getPage();
		SelfHealingLocator element = controlLookup.getLookupElement(elementName, elementType, pageName);
		Response response = page.waitForResponse(res -> res.url().contains(urlPattern), () -> {
			element.click();
		});
		String responseText = response.text();
		String value = extractValueByPath(responseText, jsonPath);
		scenarioContext.setCustomValue(storeKey, value);
	}

	private String extractValueByPath(String json, String path) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(json);
			String[] parts = path.split("\\.");
			for (String part : parts) {
				if (part.contains("[") && part.contains("]")) {
					String fieldName = part.substring(0, part.indexOf("["));
					int index = Integer.parseInt(part.substring(part.indexOf("[") + 1, part.indexOf("]")));
					node = fieldName.isEmpty() ? node.get(index) : node.get(fieldName).get(index);
				} else {
					node = node.get(part);
				}
				if (node == null || node.isMissingNode())
					throw new RuntimeException("Path not found: " + path + " at part: " + part);
			}
			return node.isValueNode() ? node.asText() : node.toString();
		} catch (Exception e) {
			throw new RuntimeException("Failed to extract value from response: " + e.getMessage(), e);
		}
	}

}
