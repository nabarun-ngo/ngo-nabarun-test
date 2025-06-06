package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class LoginPageObjects extends CommonPageObjects {

	public LoginPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<WebElement> ContinueWithPasswordButton = () -> driver.findElement(By.id("password"));
	public Supplier<WebElement> ContinueWithEmailButton = () -> driver.findElement(By.id("otp"));
	public Supplier<WebElement> LoginEmail = () -> driver.findElement(By.id("username"));
	public Supplier<WebElement> LoginPassword = () -> driver.findElement(By.id("password"));
	public Supplier<WebElement> LoginSubmit = () -> driver.findElement(By.xpath("//button[@type='submit']"));

	public Supplier<WebElement> LoginPageHeader = () -> driver.findElement(By.xpath("//p[normalize-space(text())='Welcome to Nabarun']"));

}
