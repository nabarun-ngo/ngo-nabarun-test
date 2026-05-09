package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class HomePageObjects extends CommonPageObjects {

	public HomePageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<Locator> JoinForm = () -> findLocator("//div[@id='join']");

	public Supplier<Locator> Join_Email = () -> findLocator("//input[@id='email']", JoinForm.get(), FindBy.XPATH);
	public Supplier<Locator> Join_Mobile = () -> findLocator("//input[@id='contactNumber']", JoinForm.get(),
			FindBy.XPATH);

	public Supplier<Locator> ContactForm = () -> findLocator("//div[@id='contact']");
	public Supplier<Locator> Contact_Email = () -> findLocator("//input[@id='email']", ContactForm.get(), FindBy.XPATH);
	public Supplier<Locator> Contact_Mobile = () -> findLocator("//input[@id='contactNumber']", ContactForm.get(),
			FindBy.XPATH);

	public Locator getTextBoxMapping(String elementName, Locator parentContext, boolean isTextArea) {
		return switch (elementName) {
			case "Your Name" -> findLocator("//input[@name='fullName']", ContactForm.get(), FindBy.XPATH);
			case "Your Email (JoinUs)" -> Join_Email.get();
			case "Your Mobile Number (JoinUs)" -> Join_Mobile.get();
			case "Your Email (ContactUs)" -> Contact_Email.get();
			case "Your Mobile Number (ContactUs)" -> Contact_Mobile.get();
			default ->
				scope(parentContext).getByRole(AriaRole.TEXTBOX, new Locator.GetByRoleOptions().setName(elementName));
		};
	}

	@Override
	public Locator getTextMapping(String elementName, Locator parent) {
		return switch (elementName) {
			case "I agree with the Rules and Regulations of Nabarun" -> findLocator("#acceptance");
			default -> super.getTextMapping(elementName, parent);
		};
	}

	@Override
	public Locator getLinkMapping(String elementName, Locator parent) {
		return switch (elementName) {
			case "Join Us", "Contact Us" ->
				findLocator("nav").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(elementName));
			default -> super.getLinkMapping(elementName, parent);
		};
	}

	public Locator getButtonMapping(String elementName, Locator parent) {
		return switch (elementName) {
			case "Join Now", "Send Message" ->
				scope(parent).getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(elementName));
			default -> super.getButtonMapping(elementName, parent);
		};
	}

}
