package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DonationPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlLookup;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class DonationStepDefinations {

	private DonationPageObjects donationPageObjects;
	private ControlLookup controlLookup;
	private ScenarioContext scenarioContext;
	private ElementHelper elementHelper;

	public DonationStepDefinations(
			ControlLookup controlLookup, 
			ScenarioContext scenarioContext,
			ElementHelper elementHelper,
			DonationPageObjects donationPageObjects) {
		this.donationPageObjects = donationPageObjects;
		this.controlLookup = controlLookup;
		this.scenarioContext=scenarioContext;
		this.elementHelper=elementHelper;
	}

	@Then("^I map (create|update) donation accordion as \"([^\"]*)\" accordion$")
	public void iMapCreateDonationAccordionAsAccordion(String type, String accordionName) throws Throwable {
		if (type.equalsIgnoreCase("create")) {
			controlLookup.setAccordionMapping(accordionName, donationPageObjects.DonationCreateAccordion.get());
		} else {

		}
	}

	@Then("^I capture and store the donation id$")
	public void iCaptureAndStoreTheDonationId() throws Throwable {
		String message = donationPageObjects.DonationCreateAlert.get().getText();

		Pattern pattern = Pattern.compile("NDON\\w+");
		Matcher matcher = pattern.matcher(message);

		if (matcher.find()) {
			String extractedDonationId = matcher.group();
			System.out.println("Extracted Donation ID: " + extractedDonationId);
			scenarioContext.set(ContextKeys.DonationId, extractedDonationId);
		} else {
			throw new RuntimeException("No donation ID found in message: " + message);
		}
	}

	@Then("^I search the created donation$")
	public void iSearchTheCreatedDonation() throws Throwable {
		String donationId=scenarioContext.get(ContextKeys.DonationId,String.class);
		elementHelper.scrollToTop();
		donationPageObjects.getButtonMapping("Advanced Search", null).click();
		donationPageObjects.ADVSearch_DonationId.get().sendKeys(donationId);
		donationPageObjects.getButtonMapping("Search", null).click();
	}
}
