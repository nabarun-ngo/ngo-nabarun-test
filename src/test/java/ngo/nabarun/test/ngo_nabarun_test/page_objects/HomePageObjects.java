package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import com.microsoft.playwright.Locator;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class HomePageObjects extends CommonPageObjects {

	public HomePageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<Locator> Join_Email = () -> findLocator("//div[@id='join']//input[@id='email']");
	public Supplier<Locator> Join_Mobile = () -> findLocator("//div[@id='join']//input[@id='contactNumber']");
	public Supplier<Locator> Join_Email_Text = () -> findLocator("#id");
	public Supplier<Locator> Join_OTP = () -> findLocator("#otp");
	public Supplier<Locator> Request_Alert = () -> findLocator(".alert-success");

	public Locator getTextBoxMapping(String elementName, Locator parentContext, boolean isTextArea) {
		return switch (elementName) {
			case "Your Email (JoinUs)" -> Join_Email.get();
			case "Your Mobile Number (JoinUs)" -> Join_Mobile.get();
			default ->
				findLocator("//*[normalize-space(text())='" + elementName + "']/parent::label/following-sibling::*",
						parentContext, FindBy.XPATH);
		};
	}

	@Override
	public Locator getTextMapping(String elementName, Locator parent) {
		return switch (elementName) {
			case "I agree with the Rules and Regulations of Nabarun" -> findLocator("#acceptance");
			case "Your Mobile Number (JoinUs)" -> Join_Mobile.get();
			default -> super.getTextMapping(elementName, parent);
		};
	}

}
