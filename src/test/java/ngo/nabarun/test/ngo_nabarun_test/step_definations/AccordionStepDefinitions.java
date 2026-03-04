package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.assertions.LocatorAssertions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.AccordionPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.SelfHealingLocator;
import ngo.nabarun.test.ngo_nabarun_test.utils.CommonUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reusable step definitions for FE accordion (app-accordion-list).
 * Uses stable locators: #create, #accordion-row-N, button_id / button_id_add.
 */
public class AccordionStepDefinitions {

    private static final Logger logger = LogManager.getLogger(AccordionStepDefinitions.class);
    private final ControlLookup controlLookup;
    private final AccordionPageObjects accordionPageObjects;
    private final ControlActions controlActions;
    private Locator openAccordion;
    private ScenarioContext scenarioContext;

    public AccordionStepDefinitions(ControlActions controlActions,
            AccordionPageObjects accordionPageObjects, ControlLookup controlLookup, ScenarioContext scenarioContext) {
        this.controlActions = controlActions;
        this.accordionPageObjects = accordionPageObjects;
        this.controlLookup = controlLookup;
        this.scenarioContext = scenarioContext;
    }

    @Then("^I open the (\\d+)(th|rd|st|nd) accordion$")
    public void iOpenTheAccordion(int resultNumber, String suffix) {
        this.openAccordion = accordionPageObjects.getAccordions(null, resultNumber);
        do {
            logger.info("Accordion is not expanded. Retrying...");
            this.openAccordion.click();
            CommonUtils.sleep(2);
        } while (!isExpanded(this.openAccordion));
        logger.info("Accordion is expanded.");
    }

    @Then("^I close the currently opened accordion$")
    public void iCloseTheCurrentlyOpenedAccordion() {
        if (this.openAccordion != null && isExpanded(this.openAccordion)) {
            controlActions.click(this.openAccordion);
            logger.info("Accordion is not collapsed. Retrying...");
        }
        this.openAccordion = null;
    }

    @Then("^I fill the following fields in the create accordion$")
    public void iFillTheCreateSectionAtAccordion(DataTable dataTable) {
        Locator createPanel = accordionPageObjects.Create_Accordion.get();
        createPanel.waitFor();
        fillAccordionFields(createPanel, dataTable);
    }

    @Then("^I fill the following fields in the opened accordion$")
    public void iFillTheUpdateSectionAtAccordion(DataTable dataTable) {
        validateAccordion();
        Locator parent = this.openAccordion;
        parent.waitFor();
        fillAccordionFields(parent, dataTable);
    }

    @Then("^I click \"(.+)\" button in the (opened|create) accordion$")
    public void iClickButtonInTheAccordionRowContainingAtAccordion(String buttonText, String accordionType) {
        if (accordionType.equals("opened")) {
            validateAccordion();
            Locator parent = this.openAccordion;
            parent.waitFor();
            Locator btn = this.accordionPageObjects.getButtonMapping(buttonText, parent);
            btn.waitFor();
            controlActions.click(btn);
        } else if (accordionType.equals("create")) {
            Locator createPanel = accordionPageObjects.Create_Accordion.get();
            createPanel.waitFor();
            Locator btn = this.accordionPageObjects.getButtonMapping(buttonText, createPanel);
            btn.waitFor();
            controlActions.click(btn);
        }
    }

    @Given("^I click \"(.+)\" button in the (opened|create) accordion and collect \"(.+)\" from response of \"(.+)\" and store as \"(.+)\"")
    public void i_click_and_collect(String buttonText, String accordionType, String jsonPath,
            String urlPattern, String storeKey) {

        String url = DataUtils.resolveData(urlPattern, scenarioContext);

        Page page = scenarioContext.getPage();
        Response response = page.waitForResponse(res -> res.url().contains(url), () -> {
            iClickButtonInTheAccordionRowContainingAtAccordion(buttonText, accordionType);
        });
        String responseText = response.text();
        String value = DataUtils.extractValueByPath(responseText, jsonPath);
        logger.info("Collected value of " + storeKey + " from " + urlPattern + " is: " + value);
        scenarioContext.setCustomValue(storeKey, value);
    }

    private void validateAccordion() {
        if (this.openAccordion == null) {
            throw new IllegalStateException(
                    "No accordion is opened. Please use 'I open the <resultNumber>(th|rd|st|nd) accordion' step to open an accordion.");
        }
    }

    private void fillAccordionFields(Locator parentPanel, DataTable table) {
        if (!isExpanded(parentPanel)) {
            controlActions.click(parentPanel);
        }
        List<Map<String, String>> fieldInputModels = table.asMaps(String.class, String.class);
        for (Map<String, String> fieldInputModel : fieldInputModels) {
            String fieldName = fieldInputModel.get("Field_Name");
            String fieldType = fieldInputModel.get("Field_Type");
            String actionType = fieldInputModel.get("Field_Action");
            String value = fieldInputModel.get("Field_Value");
            SelfHealingLocator element = controlLookup.getLookupElement(fieldName, fieldType, parentPanel);
            assertThat(element.getLocator()).isVisible(
                    new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
            controlActions.executeAction(actionType, element.getLocator(), fieldType, value);
        }
    }

    private static boolean isExpanded(Locator panel) {
        String expandedClass = panel.getAttribute("class");
        return expandedClass.contains("mat-expanded");
    }

    @Then("^The accordions should have (at least|exactly) (\\d+) rows$")
    public void theAccordionShouldHaveAtLeastRows(String condition, int expectedRows) {
        Locator locator = accordionPageObjects.getAccordions(null, -1);
        assertThat(locator)
                .hasCount(expectedRows,
                        new LocatorAssertions.HasCountOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
    }

    @Then("^The open accordion should have following values$")
    public void theAccordionShouldHaveAtLeastRows(DataTable dataTable) {
        List<Map<String, String>> fieldInputModels = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> fieldInputModel : fieldInputModels) {
            String sectionName = fieldInputModel.get("Section_Name");
            String fieldName = fieldInputModel.get("Field_Name");
            String value = DataUtils.resolveData(fieldInputModel.get("Field_Value"), scenarioContext);
            Locator section = accordionPageObjects.getFormMapping(sectionName, this.openAccordion);
            Locator element = accordionPageObjects.getReadOnlyField(fieldName, section);
            assertThat(element).hasText(value,
                    new LocatorAssertions.HasTextOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
        }
    }
}
