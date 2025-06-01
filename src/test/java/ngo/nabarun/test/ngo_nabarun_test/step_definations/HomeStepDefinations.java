package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.HomePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.DBUtility;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class HomeStepDefinations {
	
	private HomePageObjects pageObject;
	private ElementHelper elementHelper;
	private ScenarioContext scenarioContext;

	public HomeStepDefinations(HomePageObjects pageObject,ScenarioContext scenarioContext,ElementHelper elementHelper) {
		this.pageObject=pageObject;
		this.elementHelper=elementHelper;
		this.scenarioContext=scenarioContext;
	}


	@Then("^I retrieve the OTP from database and enter it on OTP textbox$")
	public void iRetrieveTheOTPFromDatabaseAndEnterItOnOTPTextbox() throws Throwable {
		String email =pageObject.Join_Email_Text.get().getText();
		scenarioContext.set(ContextKeys.New_User_Email, email);
		Document otp_detail=DBUtility.findOTPDetails(email);
		elementHelper.scrollIntoView(pageObject.Join_OTP.get());
		pageObject.Join_OTP.get().sendKeys(otp_detail.getString("oneTimePassword"));
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
