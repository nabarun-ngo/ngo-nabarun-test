package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.openqa.selenium.WebElement;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DonationPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DBUtility;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class DonationStepDefinations {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private DonationPageObjects donationPageObjects;
	private ScenarioContext scenarioContext;
	private ElementHelper elementHelper;

	public DonationStepDefinations(ScenarioContext scenarioContext,
			ElementHelper elementHelper, DonationPageObjects donationPageObjects) {
		this.donationPageObjects = donationPageObjects;
		this.scenarioContext = scenarioContext;
		this.elementHelper = elementHelper;
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

	@Then("^I search the created donation under \"([^\"]*)\" tab$")
	public void iSearchTheCreatedDonation(String tab) throws Throwable {
		String donationId = scenarioContext.get(ContextKeys.DonationId, String.class);
		elementHelper.scrollToTop();
		WebElement parent = null;
		if (tab.toLowerCase().contains("member")) {
			elementHelper.click(donationPageObjects.getButtonMapping("Filter", null));
			parent = donationPageObjects.Popup_Container.get();
		} else {
			elementHelper.click(donationPageObjects.getButtonMapping("Advanced Search", null));
		}
		donationPageObjects.ADVSearch_DonationId.get().sendKeys(donationId);
		elementHelper.click(donationPageObjects.getButtonMapping("Search", parent));

	}

	@Then("^I check if transaction is (created|reverted) for this donation$")
	public void iCheckIfTransactionIsReverted(String type) throws Throwable {
	}

	@Then("^I search for member \"([^\"]*)\" under \"([^\"]*)\" tab$")
	public void iSearchForMemberUnderTab(String memberName, String tab) throws Throwable {
		String firstName = memberName.split(" ")[0];
		String lastName = memberName.split(" ")[1];
		elementHelper.scrollToTop();
		elementHelper.click(donationPageObjects.getButtonMapping("Advanced Search", null));
		donationPageObjects.ADVSearch_FirstName.get().sendKeys(firstName);
		donationPageObjects.ADVSearch_LastName.get().sendKeys(lastName);
		elementHelper.click(donationPageObjects.getButtonMapping("Search", null));
	}

	@Then("^I check and delete regular donation raised for \"([^\"]*)\" this month$")
	public void iCheckAndDeleteRegularDonationRaisedForThisMonth(String memberName) throws Throwable {
		String firstName = memberName.split(" ")[0];
		String lastName = memberName.split(" ")[1];
		Document user =DBUtility.findUserByName(firstName, lastName);
		Date startDate = dateFormat.parse(DataProvider.firstDayOfCurrentMonth());
		Date endDate = dateFormat.parse(DataProvider.lastDayOfCurrentMonth());
		String id = user.getString("_id");
		List<Document> donations=DBUtility.findDonationsBetweenDates(startDate,endDate,id,"REGULAR");
		for(Document donation:donations) {
			String donationId=donation.getString("_id");
			DBUtility.deleteDonationById(donationId);
		}
	}
}
