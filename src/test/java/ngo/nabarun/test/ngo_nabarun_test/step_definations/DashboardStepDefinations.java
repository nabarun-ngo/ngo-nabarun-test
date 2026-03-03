package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DashboardPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.LoginPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.assertions.LocatorAssertions;

public class DashboardStepDefinations {

	private final DashboardPageObjects dashboardPageObjects;
	private final LoginPageObjects loginPageObjects;
	private final ControlActions controlActions;

	public DashboardStepDefinations(ScenarioContext scenarioContext, ControlActions ca,
			DataProvider dataProvider,
			DashboardPageObjects dashboardPageObjects, LoginPageObjects loginPageObjects) {
		this.dashboardPageObjects = dashboardPageObjects;
		this.loginPageObjects = loginPageObjects;
		this.controlActions = ca;
	}

	@Then("^I logout from current session$")
	public void iLogoutFromCurrentSession() {
		controlActions.click(dashboardPageObjects.ProfileIcon.get());
		controlActions.click(dashboardPageObjects.LogoutLink.get());
		controlActions.click(dashboardPageObjects.LogoutPopupYes.get());

		assertThat(loginPageObjects.LoginPageHeader.get()).isVisible(
				new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
	}

}
