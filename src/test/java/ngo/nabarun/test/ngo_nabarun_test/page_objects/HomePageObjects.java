package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class HomePageObjects extends CommonPageObjects {

	public HomePageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<WebElement> Join_Email = () -> driver.findElement(By.cssSelector("#JoinUsForm #email"));
	public Supplier<WebElement> Join_Mobile = () -> driver.findElement(By.cssSelector("#JoinUsForm #mobileno"));
	public Supplier<WebElement> Join_Email_Text = () -> driver.findElement(By.id("id"));
	public Supplier<WebElement> Join_OTP = () -> driver.findElement(By.id("otp"));
	public Supplier<WebElement> Request_Alert = () -> driver.findElement(By.cssSelector(".alert-success"));

	public WebElement getTextBoxMapping(String elementName, SearchContext parentContext, boolean isTextArea) {
		return switch (elementName) {
		case "Your Email (JoinUs)" -> Join_Email.get();
		case "Your Mobile Number (JoinUs)" -> Join_Mobile.get();
		default -> getSearchContext(parentContext)
				.findElement(By.xpath("//label[normalize-space(text())=\"" + elementName + "\"]/preceding-sibling::*"));
		};
	}

}
