package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.microsoft.playwright.Locator;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Reusable step definitions for FE accordion (app-accordion-list).
 * Uses stable locators: #create, #accordion-row-N, button_id / button_id_add.
 */
public class AccordionStepDefinitions {

    private final ControlLookup controlLookup;
    private final CommonPageObjects commonPageObjects;
    // private final ScenarioContext scenarioContext;
    private final ElementHelper elementHelper;

    public AccordionStepDefinitions(ScenarioContext scenarioContext, ElementHelper elementHelper,
            CommonPageObjects commonPageObjects, ControlLookup controlLookup) {
        // this.scenarioContext = scenarioContext;
        this.elementHelper = elementHelper;
        this.commonPageObjects = commonPageObjects;
        this.controlLookup = controlLookup;
    }

    private Locator getAccordionParent(String accordionName) {
        Locator parent = controlLookup.getAccordionMapping(accordionName);
        if (parent == null) {
            throw new RuntimeException("Accordion \"" + accordionName
                    + "\" is not mapped. Use: I use the create section on \"Page\" page as \"" + accordionName
                    + "\" accordion (or accordion list), or I map \"selector\" element as \"" + accordionName
                    + "\" accordion");
        }
        return parent;
    }

    private static boolean isExpanded(Locator panel) {
        String expanded = panel.getAttribute("aria-expanded");
        return "true".equalsIgnoreCase(expanded);
    }

    @Then("I open the create section at {string} accordion")
    public void iOpenTheCreateSectionAtAccordion(String accordionName) {
        Locator parent = getAccordionParent(accordionName);
        Locator createPanel = commonPageObjects.getAccordionCreateSection(parent);
        if (createPanel.count() == 0) {
            createPanel = parent; // mapped accordion is the create section itself (e.g. #create)
        }
        createPanel.waitFor();
        if (!isExpanded(createPanel)) {
            Locator header = commonPageObjects.getAccordionPanelHeader(createPanel);
            elementHelper.click(header);
        }
    }

    @Then("I use the create section on {string} page as {string} accordion")
    public void iUseTheCreateSectionOnPageAsAccordion(String pageName, String accordionName) {
        Locator locator = controlLookup.getAccordionLocatorForPage(pageName, "create");
        controlLookup.setAccordionMapping(accordionName, locator);
    }

    @Then("I use the accordion list on {string} page as {string} accordion")
    public void iUseTheAccordionListOnPageAsAccordion(String pageName, String accordionName) {
        Locator locator = controlLookup.getAccordionLocatorForPage(pageName, "list");
        controlLookup.setAccordionMapping(accordionName, locator);
    }

    @Then("I open the accordion row containing {string} at {string} accordion")
    public void iOpenTheAccordionRowContainingAtAccordion(String searchText, String accordionName) {
        Locator parent = getAccordionParent(accordionName);
        Locator rowPanel = commonPageObjects.getAccordionRowByContent(parent, searchText);
        rowPanel.waitFor();
        if (!isExpanded(rowPanel)) {
            Locator header = commonPageObjects.getAccordionPanelHeader(rowPanel);
            elementHelper.click(header);
        }
    }

    @Then("I click {string} button in the accordion row containing {string} at {string} accordion")
    public void iClickButtonInTheAccordionRowContainingAtAccordion(String buttonId, String searchText,
            String accordionName) {
        Locator parent = getAccordionParent(accordionName);
        Locator rowPanel = commonPageObjects.getAccordionRowByContent(parent, searchText);
        rowPanel.waitFor();
        Locator btn = commonPageObjects.getAccordionButtonInRowLocator(rowPanel, buttonId);
        btn.waitFor();
        elementHelper.click(btn);
    }

    @Then("I open the accordion row with index {int} at {string} accordion")
    public void iOpenTheAccordionRowWithIndexAtAccordion(int rowIndex, String accordionName) {
        Locator parent = getAccordionParent(accordionName);
        Locator rowPanel = commonPageObjects.getAccordionRowByRowIndex(parent, rowIndex);
        rowPanel.waitFor();
        if (!isExpanded(rowPanel)) {
            Locator header = commonPageObjects.getAccordionPanelHeader(rowPanel);
            elementHelper.click(header);
        }
    }

    @Then("The accordion {string} should have at least {int} rows")
    public void theAccordionShouldHaveAtLeastRows(String accordionName, int minRows) {
        Locator parent = getAccordionParent(accordionName);
        for (int i = 1; i <= minRows; i++) {
            Locator row = commonPageObjects.getAccordionRowByRowIndex(parent, i);
            assertThat(row).isVisible();
        }
    }

    @Then("The accordion {string} should show the create section")
    public void theAccordionShouldShowTheCreateSection(String accordionName) {
        Locator parent = getAccordionParent(accordionName);
        Locator createSection = commonPageObjects.getAccordionCreateSection(parent);
        assertThat(createSection).isVisible();
    }

    @Then("I click {string} button in the create section at {string} accordion")
    public void iClickButtonInTheCreateSectionAtAccordion(String buttonId, String accordionName) {
        Locator parent = getAccordionParent(accordionName);
        Locator btn = commonPageObjects.getAccordionButtonInCreateSection(parent, buttonId);
        btn.waitFor();
        elementHelper.click(btn);
    }

    @Then("I click {string} button in accordion row {int} at {string} accordion")
    public void iClickButtonInAccordionRowAtAccordion(String buttonId, int rowIndex, String accordionName) {
        Locator parent = getAccordionParent(accordionName);
        Locator btn = commonPageObjects.getAccordionButtonInRow(parent, rowIndex, buttonId);
        btn.waitFor();
        elementHelper.click(btn);
    }

    @Then("The accordion {string} should show no records")
    public void theAccordionShouldShowNoRecords(String accordionName) {
        Locator parent = getAccordionParent(accordionName);
        Locator emptyMessage = parent.getByText("No records found");
        assertThat(emptyMessage).isVisible();
    }
}
