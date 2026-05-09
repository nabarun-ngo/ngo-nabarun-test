package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;


import com.microsoft.playwright.Locator;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class LoginPageObjects extends CommonPageObjects {

	public LoginPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}
	
	public String AcceptConsentLocator="//*[text()='Accept']";
	public String PasswordChangedTxtLocator="//*[normalize-space(text())='Your old password has been expired']";
	public String LoginPageHeaderLocator = "//*[normalize-space(text())='Welcome to Nabarun']";
	public String ContinueWithPasswordButtonLocator = "#password";
	
	
	public Supplier<Locator> ContinueWithPasswordButton = () -> findLocator("#password");
	public Supplier<Locator> ContinueWithEmailButton = () -> findLocator("#otp");
	public Supplier<Locator> LoginEmail = () -> findLocator("#username");
	public Supplier<Locator> LoginPassword = () -> findLocator("#password");
	public Supplier<Locator> LoginSubmit = () -> findLocator("//button[@type='submit']");

	public Supplier<Locator> LoginPageHeader = () -> findLocator("//*[normalize-space(text())='Welcome to Nabarun']");
	public Supplier<Locator> NewPassword = () -> findLocator("//*[@placeholder='Enter new password']");
	public Supplier<Locator> ConfirmNewPassword = () -> findLocator("//*[@placeholder='Confirm new password']");
	public Supplier<Locator> ChangePasswordSubmit = () -> findLocator(".af-nextButton");

	public Supplier<Locator> AcceptConsent = () -> findLocator("//*[text()='Accept']");

	
}
