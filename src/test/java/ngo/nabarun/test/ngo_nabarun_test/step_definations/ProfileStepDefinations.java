package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import org.openqa.selenium.WebElement;

import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.ProfilePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

public class ProfileStepDefinations {
	
	private ElementHelper elementHelper;
	private ProfilePageObjects profilePageObjects;
	private CommonStepDefinitions commonStepDefination;

	public ProfileStepDefinations(ScenarioContext scenarioContext,ElementHelper elementHelper,DataProvider dataProvider,ProfilePageObjects profilePageObjects,CommonStepDefinitions commonStepDefination) {
		this.elementHelper = elementHelper;
		this.commonStepDefination = commonStepDefination;
		this.profilePageObjects=profilePageObjects;
	}
	
	public void fillCompleteProfileForm() throws Exception {
		WebElement title = profilePageObjects.getDropdownMapping("Title", null);
		elementHelper.selectMatOption(title, "Mr");

		WebElement gender = profilePageObjects.getDropdownMapping("Gender", null);
		elementHelper.selectMatOption(gender, "Male");

		WebElement dateOfBirth = profilePageObjects.getDatePickerMapping("Date of Birth", null);
		elementHelper.selectMatDate(dateOfBirth, DataUtils.getDateWithOffset(-365*19));

		WebElement number1 = profilePageObjects.getTextBoxMapping("Phone Number (WhatsApp)",null,false);
		if(number1.getAttribute("value") != null && number1.getAttribute("value").isEmpty()) {
			number1.clear();
			number1.sendKeys(DataUtils.generateRandomNumber(10));
		}
		
		WebElement number2 = profilePageObjects.getTextBoxMapping("Phone Number (Alternative)",null,false);
		if(number2.getAttribute("value") != null && number2.getAttribute("value").isEmpty()) {
			number2.clear();
			number2.sendKeys(DataUtils.generateRandomNumber(10));
		}


		WebElement description = profilePageObjects.getTextBoxMapping("Descrive something about you", null,true);
		description.sendKeys(DataUtils.replacePlaceholders("{RandomText}"));

		WebElement presentAddress = profilePageObjects.presentAddress.get();
		WebElement presentAddress_AddressLine1 = profilePageObjects.getTextBoxMapping("Address Line 1", presentAddress, false);
		presentAddress_AddressLine1.sendKeys(DataUtils.replacePlaceholders("{RandomLocation}"));
		WebElement presentAddress_AddressLine2 = profilePageObjects.getTextBoxMapping("Address Line 2", presentAddress, false);
		presentAddress_AddressLine2.sendKeys(DataUtils.replacePlaceholders("{RandomLocation}"));
		WebElement presentAddress_AddressLine3 = profilePageObjects.getTextBoxMapping("Address Line 3", presentAddress, false);
		presentAddress_AddressLine3.sendKeys(DataUtils.replacePlaceholders("{RandomLocation}"));
		
		WebElement presentAddress_Hometown = profilePageObjects.getTextBoxMapping("Hometown", presentAddress, false);
		presentAddress_Hometown.sendKeys(DataUtils.replacePlaceholders("{RandomLocation}"));
		
		WebElement presentAddress_Country = profilePageObjects.getDropdownMapping("Country", presentAddress);
		elementHelper.selectMatOption(presentAddress_Country, "India");
		commonStepDefination.i_wait_for_loading_to_complete();
		WebElement presentAddress_State = profilePageObjects.getDropdownMapping("State", presentAddress);
		elementHelper.selectMatOption(presentAddress_State, "West Bengal");
		commonStepDefination.i_wait_for_loading_to_complete();
		WebElement presentAddress_District = profilePageObjects.getDropdownMapping("District", presentAddress);
		elementHelper.selectMatOption(presentAddress_District, "North 24 Parganas");
		
		
		WebElement permanentAddress = profilePageObjects.permanentAddress.get();
		
		WebElement permanentAddress_AddressLine1 = profilePageObjects.getTextBoxMapping("Address Line 1", permanentAddress, false);
		permanentAddress_AddressLine1.sendKeys(DataUtils.replacePlaceholders("{RandomLocation}"));
		WebElement permanentAddress_AddressLine2 = profilePageObjects.getTextBoxMapping("Address Line 2", permanentAddress, false);
		permanentAddress_AddressLine2.sendKeys(DataUtils.replacePlaceholders("{RandomLocation}"));
		WebElement permanentAddress_AddressLine3 = profilePageObjects.getTextBoxMapping("Address Line 3", permanentAddress, false);
		permanentAddress_AddressLine3.sendKeys(DataUtils.replacePlaceholders("{RandomLocation}"));
		WebElement permanentAddress_Hometown = profilePageObjects.getTextBoxMapping("Hometown", permanentAddress, false);
		permanentAddress_Hometown.sendKeys(DataUtils.replacePlaceholders("{RandomLocation}"));
		
		WebElement permanentAddress_Country = profilePageObjects.getDropdownMapping("Country", permanentAddress);
		elementHelper.selectMatOption(permanentAddress_Country, "India");
		commonStepDefination.i_wait_for_loading_to_complete();
		WebElement permanentAddress_State = profilePageObjects.getDropdownMapping("State", permanentAddress);
		elementHelper.selectMatOption(permanentAddress_State, "West Bengal");
		commonStepDefination.i_wait_for_loading_to_complete();
		WebElement permanentAddress_District = profilePageObjects.getDropdownMapping("District", permanentAddress);
		elementHelper.selectMatOption(permanentAddress_District, "Nadia");

		elementHelper.click(profilePageObjects.getButtonMapping("Update", null));
		commonStepDefination.i_wait_for_loading_to_complete();
	}



}
