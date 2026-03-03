package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.util.List;
import java.util.Map;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.ProfilePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

@SuppressWarnings("unused")
public class ProfileStepDefinations {

	private final ControlActions controlActions;
	private final ProfilePageObjects profilePageObjects;
	private final CommonStepDefinitions commonStepDefination;
	private final ScenarioContext scenarioContext;

	public ProfileStepDefinations(ScenarioContext scenarioContext, ControlActions ca,
			DataProvider dataProvider, ProfilePageObjects profilePageObjects,
			CommonStepDefinitions commonStepDefination) {
		this.controlActions = ca;
		this.commonStepDefination = commonStepDefination;
		this.profilePageObjects = profilePageObjects;
		this.scenarioContext = scenarioContext;
	}

	@Then("^I should see (\\d+) member profile with the following details$")
	public void i_should_see_member_profile_at_page(int memberCount, DataTable dataTable) {
		List<Locator> cards = profilePageObjects.profileCard.get().all();

		assertThat(profilePageObjects.profileCard.get())
				.hasCount(memberCount,
						new LocatorAssertions.HasCountOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));

		List<Map<String, String>> expectedDetailsList = dataTable.asMaps(String.class, String.class);

		for (Map<String, String> expectedDetails : expectedDetailsList) {
			String expectedName = DataUtils.resolveData(expectedDetails.get("Name"), scenarioContext);
			String expectedRole = DataUtils.resolveData(expectedDetails.get("Role"), scenarioContext);

			boolean found = false;
			for (Locator card : cards) {
				String actualName = profilePageObjects.profileName(card).textContent().trim();
				String actualRole = profilePageObjects.profileRole(card).textContent().trim();

				if (actualName.equalsIgnoreCase(expectedName) && actualRole.equalsIgnoreCase(expectedRole)) {
					found = true;
					break;
				}
			}

			if (expectedDetailsList.size() > 0 && !found) {
				throw new AssertionError(
						"Could not find member profile with Name: " + expectedName + " and Role: " + expectedRole);
			}
		}

	}
}
