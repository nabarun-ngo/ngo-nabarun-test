package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class DashboardPageObjects extends CommonPageObjects {

	public DashboardPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<WebElement> ProfileIcon = () -> elementWait
			.until(ExpectedConditions.elementToBeClickable(By.xpath("//img[@alt=\"Profile\"]")));
	
	public Supplier<WebElement> LogoutLink = () -> elementWait
			.until(ExpectedConditions.elementToBeClickable(By.id("logout")));

	public  Supplier<WebElement> LogoutPopupYes= () -> elementWait
			.until(ExpectedConditions.elementToBeClickable(By.xpath("//app-notification-modal//button[normalize-space(text())='Yes']")));

}
