package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.util.List;

import io.cucumber.java.en.Given;
import ngo.nabarun.test.ngo_nabarun_test.config.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.models.User;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.LoginPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class LoginStepDefinations {
	
	private LoginPageObjects pageObject;
	private DataProvider dataProvider;
	private ScenarioContext scenarioContext;
	private ElementHelper elementHelper;
	public LoginStepDefinations(LoginPageObjects pageObject,DataProvider dataProvider,ScenarioContext scenarioContext,ElementHelper elementHelper) {
		this.pageObject=pageObject;
		this.dataProvider=dataProvider;
		this.scenarioContext=scenarioContext;
		this.elementHelper=elementHelper;
	}
	
	@Given("^I login with \"(.+)\" (user|role) using (Password|OTP) option$")
	public void i_performed_login_with_an_user_having_role(String loginId, String loginIdType, String loginOption) throws Exception {
		String email;
		String password = Configs.TEST_DEFAULTPASSWORD;
		if (loginIdType.equalsIgnoreCase("role")) {
			List<User> users=dataProvider.getUsersByRole(loginId);
			email = users.stream().findFirst().orElseThrow(()-> new RuntimeException("Unable to find users with role "+loginId)).getEmail();
		} else {
			email = loginId;
		}
		if (loginOption.equalsIgnoreCase("Password")) {
			scenarioContext.set(ContextKeys.Login_Option, loginOption);
			elementHelper.click(pageObject.ContinueWithPasswordButton.get());
			pageObject.LoginEmail.get().sendKeys(email);
			elementHelper.click(pageObject.LoginSubmit.get());
			pageObject.LoginPassword.get().sendKeys(password);
			elementHelper.click(pageObject.LoginSubmit.get());
		} else {
			throw new RuntimeException("LoginOption '" + loginOption + "' is not allowed.");
		}
	}
}
