package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import org.junit.jupiter.api.Assertions;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DashboardPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.LoginPageObjects;

public class DashboardStepDefinations {
	
	private DashboardPageObjects dashboardPageObjects;
	private LoginPageObjects loginPageObjects;


	public DashboardStepDefinations(DashboardPageObjects dashboardPageObjects,LoginPageObjects loginPageObjects) {
		this.dashboardPageObjects=dashboardPageObjects;
		this.loginPageObjects=loginPageObjects;
	}


	@Then("^I logout from current session$")
	public void iLogoutFromCurrentSession() throws Throwable {
		
		dashboardPageObjects.ProfileIcon.get().click();
		dashboardPageObjects.LogoutLink.get().click();
		dashboardPageObjects.LogoutPopupYes.get().click();

		boolean isDisplayed=loginPageObjects.LoginPageHeader.get().isDisplayed();
		Assertions.assertTrue(isDisplayed,"Login Page is not displayed.");
	}
}
