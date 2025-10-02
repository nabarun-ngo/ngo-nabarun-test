package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import com.microsoft.playwright.Locator;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class HomePageObjects extends CommonPageObjects {

	public HomePageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<Locator> Join_Email = () -> findLocator("#JoinUsForm #email");
	public Supplier<Locator> Join_Mobile = () -> findLocator("#JoinUsForm #mobileno");
	public Supplier<Locator> Join_Email_Text = () -> findLocator("#id");
	public Supplier<Locator> Join_OTP = () -> findLocator("#otp");
	public Supplier<Locator> Request_Alert = () -> findLocator(".alert-success");

	public Locator getTextBoxMapping(String elementName, Locator parentContext, boolean isTextArea) {
		return switch (elementName) {
		case "Your Email (JoinUs)" -> Join_Email.get();
		case "Your Mobile Number (JoinUs)" -> Join_Mobile.get();
		default -> findLocator("//label[normalize-space(text())=\"" + elementName + "\"]/preceding-sibling::*", parentContext, FindBy.XPATH);
		};
	}

}
