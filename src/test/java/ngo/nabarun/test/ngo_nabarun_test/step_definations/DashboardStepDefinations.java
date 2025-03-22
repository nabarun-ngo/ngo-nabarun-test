package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DashboardPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.LoginPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class DashboardStepDefinations {
	
	private DashboardPageObjects dashboardPageObjects;
	private LoginPageObjects loginPageObjects;
	private ElementHelper elementHelper;


	public DashboardStepDefinations(DashboardPageObjects dashboardPageObjects,LoginPageObjects loginPageObjects,ElementHelper elementHelper) {
		this.dashboardPageObjects=dashboardPageObjects;
		this.loginPageObjects=loginPageObjects;
		this.elementHelper=elementHelper;
	}


	@Then("^I logout from current session$")
	public void iLogoutFromCurrentSession() throws Throwable {
		elementHelper.click(dashboardPageObjects.ProfileIcon.get());
		elementHelper.click(dashboardPageObjects.LogoutLink.get());
		elementHelper.click(dashboardPageObjects.LogoutPopupYes.get());

		boolean isDisplayed=loginPageObjects.LoginPageHeader.get().isDisplayed();
		Assertions.assertTrue(isDisplayed,"Login Page is not displayed.");
	}

}
