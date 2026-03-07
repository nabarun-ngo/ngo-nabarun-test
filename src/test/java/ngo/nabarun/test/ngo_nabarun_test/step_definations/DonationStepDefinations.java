package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.models.db.DonationDBModel;
import ngo.nabarun.test.ngo_nabarun_test.models.db.UserDBModel;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DonationPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

public class DonationStepDefinations {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	private DonationPageObjects donationPageObjects;
	// private ScenarioContext scenarioContext;
	private ControlActions controlActions;
	private DataProvider dataProvider;

	public DonationStepDefinations(ScenarioContext sc, ControlActions ca,
			DataProvider dp,
			DonationPageObjects donationPageObjects) {
		this.donationPageObjects = donationPageObjects;
		// this.scenarioContext = sc;
		this.controlActions = ca;
		this.dataProvider = dp;
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

		// scenarioContext.getPage()
	}
}
