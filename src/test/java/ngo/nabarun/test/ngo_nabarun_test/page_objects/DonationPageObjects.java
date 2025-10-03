package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import com.microsoft.playwright.Locator;


import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class DonationPageObjects extends CommonPageObjects {

	public DonationPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<Locator> DonationCreateAccordion = () -> findLocator("#createDonation");
	public Supplier<Locator> DonationCreateAlert = () -> findLocator("//app-alert//*[@id='description']");
	public Supplier<Locator> ADVSearch_DonationId = () -> findLocator("#donationId");
	public Supplier<Locator> ADVSearch_FirstName = () -> findLocator("#firstName");
	public Supplier<Locator> ADVSearch_LastName = () ->findLocator("#lastName");
	public Supplier<Locator> Accordion_AddIcon = () -> findLocator("//mat-icon[text()='add']");

	@Override
	public Locator getButtonMapping(String elementName, Locator parentContext) {
		return switch (elementName) {
		case "Add Icon" -> Accordion_AddIcon.get();
		default -> super.getButtonMapping(elementName, parentContext);
		};
	}

}
