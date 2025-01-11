package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import io.cucumber.java.en.*;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.pages.CommonPage;

public class CommonStepDefinitions {

	@SuppressWarnings("unused")
	private ScenarioContext scenarioContext;
	private CommonPage commonPage;

	public CommonStepDefinitions(ScenarioContext scenarioContext, CommonPage commonPage) {
		this.scenarioContext = scenarioContext;
		this.commonPage = commonPage;
	}

	@Given("that I am on Nabarun {string} page")
	public void that_i_am_on_nabarun_home_page(String pageName) {
		commonPage.goToPage(pageName);
	}

	@Given("^I (click|click and hold) on \"(.+)\" button$")
	public void i_clicked_on_button(String action, String buttonName) {
		commonPage.performActionOnElement(action, buttonName);
	}

	@Then("^I switch to the (new|previous) tab$")
	public void iCloseTheCurrentTabAndSwitchToTheOriginalTab(String type) {
		if (type.equalsIgnoreCase("new")) {
			commonPage.switchToNewWindow();
		} else {
			commonPage.backToPreviousWindow();
		}
	}

	@Given("^I login with \"(.+)\" (user|role) using (Password|OTP) option$")
	public void i_performed_login_with_an_user_having_role(String loginId, String loginIdType, String loginOption) {
		commonPage.loginToNabarun(loginOption, loginId, loginIdType);
	}

	@Then("I must be landed to {string} screen")
	public void i_must_be_landed_to_screen(String screenName) {
		commonPage.checkPageHeader(screenName);
	}

	@Then("I wait for loading to complete")
	public void i_wait_for_loading_to_complete() {
		commonPage.waitForLoadingComplete();
	}

}
