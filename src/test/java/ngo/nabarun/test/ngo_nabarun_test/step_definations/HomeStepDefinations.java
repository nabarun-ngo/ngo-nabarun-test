package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.models.db.TicketDBModel;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.HomePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class HomeStepDefinations {
	
	private HomePageObjects pageObject;
	private ElementHelper elementHelper;
	private ScenarioContext scenarioContext;
	private DataProvider dataProvider;

	public HomeStepDefinations(ScenarioContext scenarioContext,ElementHelper elementHelper,
			DataProvider dataProvider,HomePageObjects pageObject) {
		this.pageObject=pageObject;
		this.elementHelper=elementHelper;
		this.scenarioContext=scenarioContext;
		this.dataProvider = dataProvider;
	}


	@Then("^I retrieve the OTP from database and enter it on OTP textbox$")
	public void iRetrieveTheOTPFromDatabaseAndEnterItOnOTPTextbox() throws Throwable {
		String email =pageObject.Join_Email_Text.get().getText();
		scenarioContext.set(ContextKeys.New_User_Email, email);
		TicketDBModel otp_detail=dataProvider.findOTPDetails(email);
		elementHelper.scrollIntoView(pageObject.Join_OTP.get());
		pageObject.Join_OTP.get().sendKeys(otp_detail.getOneTimePassword());
	}
	
	@Then("^I capture and store the request id$")
	public void iCaptureAndStoreTheDonationId() throws Throwable {
		String message = pageObject.Request_Alert.get().getText();
		Pattern pattern = Pattern.compile("NWF\\w+");
		Matcher matcher = pattern.matcher(message);

		if (matcher.find()) {
			String extractedId = matcher.group();
			System.out.println("Extracted Request ID: " + extractedId);
			scenarioContext.set(ContextKeys.RequestId, extractedId);
		} else {
			throw new RuntimeException("No Request ID found in message: " + message);
		}

	}
}
