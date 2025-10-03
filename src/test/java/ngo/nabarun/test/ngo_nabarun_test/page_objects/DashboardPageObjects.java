package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import com.microsoft.playwright.Locator;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class DashboardPageObjects extends CommonPageObjects {

	public DashboardPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<Locator> ProfileIcon = () -> findLocator("//img[@alt=\"Profile\"]");
	
	public Supplier<Locator> LogoutLink = () -> findLocator("#logout");

	public  Supplier<Locator> LogoutPopupYes= () -> findLocator("//app-notification-modal//button[normalize-space(text())='Yes']");

}
