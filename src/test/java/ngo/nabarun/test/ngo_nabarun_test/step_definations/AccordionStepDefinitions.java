package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.microsoft.playwright.Locator;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.models.common.FieldInputModel;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;

import java.util.List;

/**
 * Reusable step definitions for FE accordion (app-accordion-list).
 * Uses stable locators: #create, #accordion-row-N, button_id / button_id_add.
 */
public class AccordionStepDefinitions {

    private final ControlLookup controlLookup;
    private final CommonPageObjects commonPageObjects;
    private final ControlActions controlActions;
    private Locator openAccordion;

    public AccordionStepDefinitions(ControlActions controlActions,
            CommonPageObjects commonPageObjects, ControlLookup controlLookup) {
        this.controlActions = controlActions;
        this.commonPageObjects = commonPageObjects;
        this.controlLookup = controlLookup;
    }

    @Then("^I open the (\\d+)(th|rd|st|nd) accordion$")
    public void iOpenTheAccordion(int resultNumber) {
        this.openAccordion = this.commonPageObjects.getAccordions(null).nth(resultNumber - 1);
        if (!isExpanded(this.openAccordion)) {
            controlActions.click(this.openAccordion);
        }
    }

    @Then("^I close the currently opened accordion$")
    public void iCloseTheCurrentlyOpenedAccordion() {
        if (this.openAccordion != null && isExpanded(this.openAccordion)) {
            controlActions.click(this.openAccordion);
        }
        this.openAccordion = null;
    }

    @Then("^I fill the following fields in the create accordion$")
    public void iFillTheCreateSectionAtAccordion(DataTable dataTable) {
        List<FieldInputModel> fieldInputModels = dataTable.asList(FieldInputModel.class);
        Locator createPanel = commonPageObjects.Create_Accordion.get();
        createPanel.waitFor();
        fillAccordionFields(createPanel, fieldInputModels);
    }

    @Then("^I fill the following fields in the opened accordion$")
    public void iFillTheUpdateSectionAtAccordion(DataTable dataTable) {
        validateAccordion();
        List<FieldInputModel> fieldInputModels = dataTable.asList(FieldInputModel.class);
        Locator parent = this.openAccordion;
        parent.waitFor();
        fillAccordionFields(parent, fieldInputModels);
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

    private void fillAccordionFields(Locator parentPanel, List<FieldInputModel> fieldInputModels) {
        if (!isExpanded(parentPanel)) {
            controlActions.click(parentPanel);
        }
        for (FieldInputModel fieldInputModel : fieldInputModels) {
            String fieldName = fieldInputModel.fieldName;
            String fieldType = fieldInputModel.fieldType;
            String actionType = fieldInputModel.fieldAction;
            String value = fieldInputModel.fieldValue;
            Locator locator = controlLookup.getLookupElement(fieldName, fieldType, parentPanel).getLocator();
            controlActions.executeAction(actionType, locator, fieldType, value);
        }
    }

    private static boolean isExpanded(Locator panel) {
        String expanded = panel.getAttribute("aria-expanded");
        return "true".equalsIgnoreCase(expanded);
    }

}
