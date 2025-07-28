package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.text.SimpleDateFormat;
import java.util.List;

import org.openqa.selenium.WebElement;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.models.api.User;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.LoginPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class LoginStepDefinations {

	private LoginPageObjects pageObject;
	private DataProvider dataProvider;
	private ScenarioContext scenarioContext;
	private ElementHelper elementHelper;
	private ControlLookup controlLookup;
	private CommonStepDefinitions commonStepDefinitions;

	public LoginStepDefinations(LoginPageObjects pageObject, DataProvider dataProvider, ScenarioContext scenarioContext,
			ElementHelper elementHelper, ControlLookup controlLookup, CommonStepDefinitions commonStepDefinitions) {
		this.pageObject = pageObject;
		this.dataProvider = dataProvider;
		this.scenarioContext = scenarioContext;
		this.elementHelper = elementHelper;
		this.controlLookup = controlLookup;
		this.commonStepDefinitions = commonStepDefinitions;
	}

	@Given("^I login with \"(.+)\" (user|role) using (Password|OTP) option$")
	public void i_performed_login_with_an_user_having_role(String loginId, String loginIdType, String loginOption)
			throws Exception {
		String email;
		String password = Configs.TEST_DEFAULTPASSWORD;

		if (loginIdType.equalsIgnoreCase("role")) {
			List<User> users = dataProvider.getUsersByRoleViaAPI(loginId);
			email = users.stream().findFirst()
					.orElseThrow(() -> new RuntimeException("Unable to find users with role " + loginId)).getEmail();
		} else {
			email = loginId;
		}

		if (email.equalsIgnoreCase("{NewUserEmail}")) {
			email = scenarioContext.get(ContextKeys.New_User_Email, String.class);
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

	@And("^I handle (user consent|change password|complete profile|all conditional post login) screen if it appeared$")
	public void iCheckIfUserConsentScreenAppearedOrNot(String screen) throws Throwable {
		switch (screen.toLowerCase()) {
		case "user consent":
			handle_user_consent_screen_if_it_appeared();
			break;
		case "change password":
			handle_change_password_screen_if_it_appeared();
			break;
		case "complete profile":
			handle_complete_profile_screen_if_it_appeared();
			break;
		case "all conditional post login":
			handle_user_consent_screen_if_it_appeared();
			// handle_change_password_screen_if_it_appeared();
			handle_complete_profile_screen_if_it_appeared();
			break;
		default:
			throw new RuntimeException("Screen '" + screen + "' is not allowed.");
		}
	}

	private void handle_user_consent_screen_if_it_appeared() throws Exception {
		// Handling consent screen if it appeared
		if (elementHelper.isElementPresent(pageObject.AcceptConsentLocator, 5)) {
			elementHelper.click(pageObject.AcceptConsent.get());
		}
	}

	private void handle_change_password_screen_if_it_appeared() throws Exception {
	    // Handling change password screen if it appeared
	    if (elementHelper.isElementPresent(pageObject.PasswordChangedTxtLocator, 5)) {
	        pageObject.NewPassword.get().sendKeys(Configs.TEST_DEFAULTPASSWORD);
	        pageObject.ConfirmNewPassword.get().sendKeys(Configs.TEST_DEFAULTPASSWORD);
	        elementHelper.click(pageObject.ChangePasswordSubmit.get());
	    }
	}

	private void handle_complete_profile_screen_if_it_appeared() throws Exception {
		// Handling complete profile screen if it appeared
		if (elementHelper.isElementPresent(pageObject.PageHeaderLocator, 10)
				&& pageObject.PageHeader.get().getText().toUpperCase().contains("COMPLETE PROFILE")) {

			WebElement checkbox = controlLookup.getLookupElement("Permanent Address same as Present Address", "text",
					"Profile", "page");
			elementHelper.click(checkbox);
			commonStepDefinitions.i_wait_for_loading_to_complete();

			WebElement title = controlLookup.getLookupElement("Title", "dropdown", "Profile", "page");
			elementHelper.selectMatOption(title, "Mr");

			WebElement gender = controlLookup.getLookupElement("Gender", "dropdown", "Profile", "page");
			elementHelper.selectMatOption(gender, "Male");

			WebElement dateOfBirth = controlLookup.getLookupElement("Date of Birth", "datepicker", "Profile", "page");
			elementHelper.selectMatDate(dateOfBirth, new SimpleDateFormat("dd/MM/yyyy").parse("24/12/1998"));

			WebElement number = controlLookup.getLookupElement("Phone Number (WhatsApp)", "textbox", "Profile", "page");
			number.clear();
			number.sendKeys("9123899870");

			WebElement desc = controlLookup.getLookupElement("Descrive something about you", "textarea", "Profile",
					"page");
			desc.clear();

			desc.sendKeys("yguuyyyui");

			controlLookup.getLookupElement("Address Line 1", "textbox", "Profile", "page").sendKeys("{RandomLocation}");
			controlLookup.getLookupElement("Hometown", "textbox", "Profile", "page").sendKeys("{RandomLocation}");

			WebElement country = controlLookup.getLookupElement("Country", "dropdown", "Profile", "page");
			elementHelper.selectMatOption(country, "India");
			commonStepDefinitions.i_wait_for_loading_to_complete();

			WebElement state = controlLookup.getLookupElement("State", "dropdown", "Profile", "page");
			elementHelper.selectMatOption(state, "West Bengal");
			commonStepDefinitions.i_wait_for_loading_to_complete();

			WebElement district = controlLookup.getLookupElement("District", "dropdown", "Profile", "page");
			elementHelper.selectMatOption(district, "North 24 Parganas");
			commonStepDefinitions.i_wait_for_loading_to_complete();
			controlLookup.getLookupElement("Update", "button", "Profile", "page").click();
			commonStepDefinitions.i_wait_for_loading_to_complete();
		}
	}

}
