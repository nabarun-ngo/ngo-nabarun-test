package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.SelfHealingLocator;
import ngo.nabarun.test.ngo_nabarun_test.utils.CommonUtils;
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
    private final CommonPageObjects commonPageObjects;
    private final ControlActions controlActions;
    private Locator openAccordion;
    // private ScenarioContext scenarioContext;

    public AccordionStepDefinitions(ControlActions controlActions,
            CommonPageObjects commonPageObjects, ControlLookup controlLookup, ScenarioContext scenarioContext) {
        this.controlActions = controlActions;
        this.commonPageObjects = commonPageObjects;
        this.controlLookup = controlLookup;
        // this.scenarioContext = scenarioContext;
    }

    @Then("^I open the (\\d+)(th|rd|st|nd) accordion$")
    public void iOpenTheAccordion(int resultNumber, String suffix) {
        this.openAccordion = commonPageObjects.getAccordions(null, resultNumber);
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
        Locator createPanel = commonPageObjects.Create_Accordion.get();
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

    @Then("^I click \"(.+)\" button in the opened accordion$")
    public void iClickButtonInTheAccordionRowContainingAtAccordion(String buttonText) {
        validateAccordion();
        Locator parent = this.openAccordion;
        parent.waitFor();
        Locator btn = this.commonPageObjects.getButtonMapping(buttonText, parent);
        btn.waitFor();
        controlActions.click(btn);
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
        Locator locator = commonPageObjects.getAccordions(null, -1);
        assertThat(locator)
                .hasCount(expectedRows,
                        new LocatorAssertions.HasCountOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
    }
}
