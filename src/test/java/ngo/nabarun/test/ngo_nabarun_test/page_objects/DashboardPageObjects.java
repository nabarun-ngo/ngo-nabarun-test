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

	public Supplier<Locator> LogoutPopupYes = () -> findLocator(
			"//app-notification-modal//button[normalize-space(text())='Yes']");

	@Override
	public Locator getTextMapping(String elementName, Locator parent) {
		return switch (elementName) {
			case "Donations" -> findLocator("#donationTile");
			case "Accounts" -> findLocator("#accountTile");
			case "Expenses" -> findLocator("#expenseTile");
			case "Tasks" -> findLocator("#worklistTile");
			case "Members" -> findLocator("#memberTile");
			case "Projects" -> findLocator("#eventTile");
			case "Requests" -> findLocator("#requestTile");
			case "Reports" -> findLocator("#reportsTile");
			case "Events & Meetings" -> findLocator("#noticeTile");
			case "Admin Console" -> findLocator("#adminTile");
			default -> super.getTextMapping(elementName, parent);
		};
	}
}
