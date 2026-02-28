package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;
import ngo.nabarun.test.ngo_nabarun_test.utilities.SelfHealingLocator;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

/**
 * Reusable UI step definitions for all pages.
 * Use with ControlLookup-registered pages and accordions.
 * Supports: visibility assertions, generic dialog/modal actions, navigation.
 */
public class UIStepDefinitions {

    private final ControlLookup controlLookup;
    private final ScenarioContext scenarioContext;
    private final CommonPageObjects commonPageObjects;
    private final ElementHelper elementHelper;

    public UIStepDefinitions(ScenarioContext scenarioContext, ElementHelper elementHelper,
            CommonPageObjects commonPageObjects, ControlLookup controlLookup) {
        this.scenarioContext = scenarioContext;
        this.elementHelper = elementHelper;
        this.commonPageObjects = commonPageObjects;
        this.controlLookup = controlLookup;
    }

    // ---------- Visibility (should see / should not see) ----------

    @Then("I should see text {string} at {string} page")
    public void iShouldSeeTextAtPage(String expectedText, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(expectedText, "text", pageName, "page");
        assertThat(element.getLocator().first()).isVisible();
    }

    @Then("I should see text {string} at {string} accordion")
    public void iShouldSeeTextAtAccordion(String expectedText, String accordionName) {
        SelfHealingLocator element = controlLookup.getLookupElement(expectedText, "text", accordionName, "accordion");
        assertThat(element.getLocator().first()).isVisible();
    }

    @Then("I should not see text {string} at {string} page")
    public void iShouldNotSeeTextAtPage(String text, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(text, "text", pageName, "page");
        assertThat(element.getLocator().first()).not().isVisible();
    }

    @Then("I should not see text {string} at {string} accordion")
    public void iShouldNotSeeTextAtAccordion(String text, String accordionName) {
        SelfHealingLocator element = controlLookup.getLookupElement(text, "text", accordionName, "accordion");
        assertThat(element.getLocator().first()).not().isVisible();
    }

    @Then("The {string} link should be visible at {string} page")
    public void theLinkShouldBeVisibleAtPage(String linkText, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(linkText, "link", pageName, "page");
        assertThat(element.getLocator()).isVisible();
    }

    @Then("The {string} link should be visible at {string} accordion")
    public void theLinkShouldBeVisibleAtAccordion(String linkText, String accordionName) {
        SelfHealingLocator element = controlLookup.getLookupElement(linkText, "link", accordionName, "accordion");
        assertThat(element.getLocator()).isVisible();
    }

    @Then("The {string} link should not be visible at {string} page")
    public void theLinkShouldNotBeVisibleAtPage(String linkText, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(linkText, "link", pageName, "page");
        assertThat(element.getLocator()).not().isVisible();
    }

    @Then("The {string} button should not be displayed at {string} page")
    public void theButtonShouldNotBeDisplayedAtPage(String elementName, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(elementName, "button", pageName, "page");
        assertThat(element.getLocator()).not().isVisible();
    }

    @Then("The {string} button should not be displayed at {string} accordion")
    public void theButtonShouldNotBeDisplayedAtAccordion(String elementName, String accordionName) {
        SelfHealingLocator element = controlLookup.getLookupElement(elementName, "button", accordionName, "accordion");
        assertThat(element.getLocator()).not().isVisible();
    }

    // ---------- Dialog / modal (generic) ----------

    @Then("I click on {string} button in the open dialog")
    public void iClickOnButtonInTheOpenDialog(String buttonText) {
        Locator dialog = commonPageObjects.Popup_Container();
        Locator button = dialog.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(buttonText));
        button.waitFor();
        elementHelper.click(button);
    }

    @Then("I click on {string} link in the open dialog")
    public void iClickOnLinkInTheOpenDialog(String linkText) {
        Locator dialog = commonPageObjects.Popup_Container();
        Locator link = dialog.getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(linkText));
        link.waitFor();
        elementHelper.click(link);
    }

    @Then("The open dialog should contain text {string}")
    public void theOpenDialogShouldContainText(String expectedText) {
        Locator dialog = commonPageObjects.Popup_Container();
        assertThat(dialog.getByText(expectedText).first()).isVisible();
    }

    @Then("The open dialog should not be visible")
    public void theOpenDialogShouldNotBeVisible() {
        Locator dialog = commonPageObjects.Popup_Container();
        assertThat(dialog).not().isVisible();
    }

    // ---------- Navigate ----------

    @Then("I navigate to {string} from dashboard")
    public void iNavigateToFromDashboard(String menuOrScreenText) {
        SelfHealingLocator element = controlLookup.getLookupElement(menuOrScreenText, "text", "dashboard", "page");
        element.click();
        Page page = scenarioContext.getPage();
        page.waitForSelector(commonPageObjects.PageLoaderSelector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(60000));
    }

    // ---------- Wait ----------

    @Then("I wait for {string} to be visible at {string} page")
    public void iWaitForToBeVisibleAtPage(String elementDescription, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(elementDescription, "text", pageName, "page");
        element.getLocator().first().waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE).setTimeout(30000));
    }

    @Then("I wait for {string} button to be visible at {string} page")
    public void iWaitForButtonToBeVisibleAtPage(String buttonName, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(buttonName, "button", pageName, "page");
        element.getLocator().waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE).setTimeout(30000));
    }
}
