package ngo.nabarun.test.ngo_nabarun_test.pages.objects;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class CommonPageObjects {

	private WebDriver driver;

	public CommonPageObjects(ScenarioContext scenarioContext) {
		this.driver = scenarioContext.getDriver();
	}

	public By PageLoaderLocator = By.xpath("//*[normalize-space(text())='Please wait, Things are getting ready...']");

	public Supplier<WebElement> ContinueWithPasswordButton = () -> driver.findElement(By.id("password"));
	public Supplier<WebElement> ContinueWithEmailButton = () -> driver.findElement(By.id("otp"));
	public Supplier<WebElement> LoginEmail = () -> driver.findElement(By.id("username"));
	public Supplier<WebElement> LoginPassword = () -> driver.findElement(By.id("password"));
	public Supplier<WebElement> LoginSubmit = () -> driver.findElement(By.xpath("//button[@type='submit']"));
	public Supplier<WebElement> PageHeader = () -> driver.findElement(By.xpath("//app-page-title//span"));

}
