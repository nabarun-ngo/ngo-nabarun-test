package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import com.microsoft.playwright.Locator;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class ProfilePageObjects extends CommonPageObjects {

	public ProfilePageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<Locator> presentAddress = () -> findLocator(
			"(//*[normalize-space(text())='Present Address']/following-sibling::*)[1]");
	public Supplier<Locator> permanentAddress = () -> findLocator(
			"(//*[normalize-space(text())='Permanent Address']/following-sibling::*)[1]");
	public Supplier<Locator> profileCard = () -> findLocator("app-profile-card");

	public Locator profileName(Locator parent) {
		return parent.locator(".text-black.font-semibold");
	}

	public Locator profileRole(Locator parent) {
		return parent.locator(".text-gray-500.font-medium");
	}
}
