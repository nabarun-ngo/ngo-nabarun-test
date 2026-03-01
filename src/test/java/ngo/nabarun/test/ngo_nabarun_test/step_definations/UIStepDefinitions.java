package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import ngo.nabarun.test.ngo_nabarun_test.utilities.SelfHealingLocator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.List;
import java.util.Map;

/**
 * Reusable UI step definitions for all pages.
 * Use with ControlLookup-registered pages and accordions.
 * Supports: visibility assertions, generic dialog/modal actions, navigation.
 */
public class UIStepDefinitions {
    private static final Logger logger = LogManager.getLogger(UIStepDefinitions.class);
    private final ControlLookup controlLookup;
    private final CommonPageObjects commonPageObjects;
    private final ControlActions controlActions;

    public UIStepDefinitions(ScenarioContext scenarioContext, ControlActions ca,
            CommonPageObjects commonPageObjects, ControlLookup controlLookup) {
        this.controlActions = ca;
        this.commonPageObjects = commonPageObjects;
        this.controlLookup = controlLookup;
    }

    // ---------- Visibility (should see / should not see) ----------

    @Then("I should see text {string} at {string} page")
    public void iShouldSeeTextAtPage(String expectedText, String pageName) {
        logger.debug("Assertion: Verifying that text '{}' is visible on page '{}'", expectedText, pageName);
        SelfHealingLocator element = controlLookup.getLookupElement(expectedText, "text", pageName);
        assertThat(element.getLocator().first()).isVisible(
                new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
    }

    @Then("^I wait for following text to display at \"(.+)\" (page)$")
    public void iWaitForFollowingTextToDisplay(String pageName, String pageType, DataTable table) {
        logger.debug("Step: Waiting for multiple texts to be visible on page '{}'", pageName);
        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> columns : rows) {
            String content = columns.get("Expected_Content");
            logger.debug("Waiting for text: '{}'", content);
            SelfHealingLocator element = controlLookup.getLookupElement(content, "text", pageName);
            assertThat(element.getLocator().first()).isVisible(
                    new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
        }
    }

    @Then("I should not see text {string} at {string} page")
    public void iShouldNotSeeTextAtPage(String text, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(text, "text", pageName);
        assertThat(element.getLocator().first()).not().isVisible(
                new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
    }

    // ---------- Dialog / modal (generic) ----------

    @Then("I click on {string} button in the open modal")
    public void iClickOnButtonInTheOpenModal(String buttonText) {
        logger.debug("Step: Clicking on '{}' button in the open modal.", buttonText);
        Locator dialog = commonPageObjects.Popup_Container();
        Locator button = dialog.getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(buttonText));
        button.waitFor();
        controlActions.click(button);
    }

    @Then("The open modal should contain text {string}")
    public void theOpenModalShouldContainText(String expectedText) {
        Locator dialog = commonPageObjects.Popup_Container();
        assertThat(dialog.getByText(expectedText).first()).isVisible();
    }

    @Then("The open modal should not be visible")
    public void theOpenModalShouldNotBeVisible() {
        Locator dialog = commonPageObjects.Popup_Container();
        assertThat(dialog).not().isVisible(
                new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
    }

    // ---------- Wait ----------

    @Then("I wait for {string} text to be visible at {string} page")
    public void iWaitForToBeVisibleAtPage(String elementDescription, String pageName) {
        SelfHealingLocator element = controlLookup.getLookupElement(elementDescription, "text", pageName);
        element.getLocator().first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
    }
}
