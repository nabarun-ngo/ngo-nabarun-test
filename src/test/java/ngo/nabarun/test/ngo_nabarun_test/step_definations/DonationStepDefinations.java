package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.models.db.DonationDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.UserDBModel;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DonationPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

public class DonationStepDefinations {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private DonationPageObjects donationPageObjects;
	private ScenarioContext scenarioContext;
	private ControlActions controlActions;
	private DataProvider dataProvider;

	public DonationStepDefinations(ScenarioContext sc, ControlActions ca,
			DataProvider dp,
			DonationPageObjects donationPageObjects) {
		this.donationPageObjects = donationPageObjects;
		this.scenarioContext = sc;
		this.controlActions = ca;
		this.dataProvider = dp;
	}

	@Then("^I capture and store the donation id$")
	public void iCaptureAndStoreTheDonationId() throws Throwable {
		String message = donationPageObjects.DonationCreateAlert.get().textContent();
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
		controlActions.scrollToTop();
		Locator parent = null;
		if (tab.toLowerCase().contains("member")) {
			controlActions.click(donationPageObjects.getButtonMapping("Filter", null));
			parent = donationPageObjects.Popup_Container();
		} else {
			controlActions.click(donationPageObjects.getButtonMapping("Advanced Search", null));
		}
		donationPageObjects.ADVSearch_DonationId.get().fill(donationId);
		controlActions.click(donationPageObjects.getButtonMapping("Search", parent));

	}

	@Then("^I check if transaction is (created|reverted) for this donation$")
	public void iCheckIfTransactionIsReverted(String type) throws Throwable {
	}

	@Then("^I search for member \"([^\"]*)\" under \"([^\"]*)\" tab$")
	public void iSearchForMemberUnderTab(String memberName, String tab) throws Throwable {
		Locator selectorModal = donationPageObjects.getTextMapping("Select Members", null);
		assertThat(selectorModal).isVisible(
				new LocatorAssertions.IsVisibleOptions().setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
		donationPageObjects.Modal_UserSearch.get().fill(memberName);
		Locator selectBtn = donationPageObjects.getButtonMapping("Select", null);
		controlActions.click(selectBtn);
	}

	@Then("^I check and delete regular donation raised for \"([^\"]*)\" this month$")
	public void iCheckAndDeleteRegularDonationRaisedForThisMonth(String memberName) throws Throwable {
		String firstName = memberName.split(" ")[0];
		String lastName = memberName.split(" ")[1];
		UserDBModel user = dataProvider.findUserByName(firstName, lastName);
		Date startDate = dateFormat.parse(DataUtils.firstDayOfCurrentMonth());
		Date endDate = dateFormat.parse(DataUtils.lastDayOfCurrentMonth());
		String id = user.getId();
		List<DonationDBModel> donations = dataProvider.findDonationsBetweenDates(startDate, endDate, id, "REGULAR");
		for (DonationDBModel donation : donations) {
			String donationId = donation.getId();
			dataProvider.deleteDonationById(donationId);
		}
	}

	@Then("I start listening to network calls")
	public void I_start_listening_to_network_calls() throws Throwable {
		scenarioContext.getPage().onResponse(response -> {

			System.out.println("[DONATION] " + response.url() + " (" + response.request().resourceType() + ") --> "
					+ response.status());
			if (response.url().contains("donation/create")
					&& response.request().resourceType().equals("xhr")
					&& response.status() == 201) {

				try {
					ObjectMapper mapper = new ObjectMapper();
					JsonNode json = mapper.readTree(response.text());
					String donationId = json.get("responsePayload").get("id").asText();
					scenarioContext.set(ContextKeys.DonationId, donationId);

					System.out.println("Captured Donation ID: " + donationId);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
