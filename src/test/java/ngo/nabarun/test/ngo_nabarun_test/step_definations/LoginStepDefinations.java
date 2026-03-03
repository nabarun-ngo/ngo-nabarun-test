package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.util.List;

import com.auth0.client.mgmt.types.ListUsersByEmailRequestParameters;
import com.auth0.client.mgmt.types.UpdateUserRequestContent;
import com.auth0.client.mgmt.types.UserResponseSchema;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.models.api.User;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.LoginPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.Auth0Client;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import ngo.nabarun.test.ngo_nabarun_test.utils.CommonUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

public class LoginStepDefinations {

	private final LoginPageObjects pageObject;
	private final DataProvider dataProvider;
	private final ScenarioContext scenarioContext;
	private final ControlActions controlActions;

	public LoginStepDefinations(ScenarioContext scenarioContext, ControlActions ca,
			DataProvider dataProvider, LoginPageObjects pageObject) {
		this.pageObject = pageObject;
		this.dataProvider = dataProvider;
		this.scenarioContext = scenarioContext;
		this.controlActions = ca;
	}

	@Given("^I login with \"(.+)\" (user|role) using (Password|OTP) option$")
	public void i_performed_login_with_an_user_having_role(String loginId, String loginIdType, String loginOption)
			throws Exception {
		String email;
		String password = Configs.TEST_DEFAULTPASSWORD;
		scenarioContext.set(ContextKeys.Login_Option, loginOption);
		scenarioContext.set(ContextKeys.Login_Id, loginId);
		scenarioContext.set(ContextKeys.Login_Id_Type, loginIdType);

		if (loginIdType.equalsIgnoreCase("role")) {
			List<User> users = dataProvider.getUsersByRoleViaAPI(loginId);
			email = users.stream().findFirst()
					.orElseThrow(() -> new RuntimeException("Unable to find users with role " + loginId)).getEmail();
		} else {
			email = DataUtils.resolveData(loginId, scenarioContext);
		}

		if (loginOption.equalsIgnoreCase("Password")) {
			controlActions.click(pageObject.ContinueWithPasswordButton.get());
			pageObject.LoginEmail.get().fill(email);
			controlActions.click(pageObject.LoginSubmit.get());
			pageObject.LoginPassword.get().fill(password);
			controlActions.click(pageObject.LoginSubmit.get());
		} else {
			throw new RuntimeException("LoginOption '" + loginOption + "' is not allowed.");
		}
	}

	@And("^I handle (user consent|change password|all conditional post login) screen(| if needed)$")
	public void iCheckIfUserConsentScreenAppearedOrNot(String screen, String condition) throws Exception {
		boolean expectedToAppear = condition.trim().equalsIgnoreCase("if needed") ? false : true;
		switch (screen.trim().toLowerCase()) {
			case "user consent":
				handle_user_consent_screen(expectedToAppear);
				break;
			case "change password":
				handle_change_password_screen(expectedToAppear);
				break;
			case "all conditional post login":
				handle_change_password_screen(expectedToAppear);
				handle_user_consent_screen(expectedToAppear);
				break;
			default:
				throw new RuntimeException("Screen '" + screen + "' is not allowed.");
		}
	}

	@And("^I change \"(.+)\" user's (password to default password|email as verified)$")
	public void iChangeUsersPasswordToDefaultPassword(String email, String action) throws Throwable {
		email = DataUtils.resolveData(email, scenarioContext);
		UserResponseSchema user = Auth0Client.managementAPI().users()
				.listUsersByEmail(ListUsersByEmailRequestParameters.builder().email(email).build()).getFirst();
		if (action.equalsIgnoreCase("password to default password")) {
			Auth0Client.managementAPI().users().update(user.getUserId().get(),
					UpdateUserRequestContent.builder().password(Configs.TEST_DEFAULTPASSWORD).build());
		} else if (action.equalsIgnoreCase("email as verified")) {
			Auth0Client.managementAPI().users().update(user.getUserId().get(),
					UpdateUserRequestContent.builder().emailVerified(true).build());
		} else {
			throw new RuntimeException("Action '" + action + "' is not allowed.");
		}
		CommonUtils.sleep(5);
	}

	private void handle_user_consent_screen(boolean isGuranteed) throws Exception {
		if (isGuranteed || controlActions.isElementPresent(pageObject.AcceptConsentLocator, 2)) {
			pageObject.AcceptConsent.get().scrollIntoViewIfNeeded();
			controlActions.click(pageObject.AcceptConsent.get());
		}
	}

	private void handle_change_password_screen(boolean isGuranteed) throws Exception {
		if (isGuranteed || controlActions.isElementPresent(pageObject.PasswordChangedTxtLocator, 2)) {
			pageObject.NewPassword.get().fill(Configs.TEST_DEFAULTPASSWORD);
			pageObject.ConfirmNewPassword.get().fill(Configs.TEST_DEFAULTPASSWORD);
			controlActions.click(pageObject.ChangePasswordSubmit.get());
			Thread.sleep(5000); // wait for the change password to complete
			scenarioContext.getPage().reload();
			Thread.sleep(5000);
		}
	}

}
