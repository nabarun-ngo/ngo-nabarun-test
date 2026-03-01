package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import com.microsoft.playwright.Locator;

import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.ProfilePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

public class ProfileStepDefinations {

	private final ControlActions controlActions;
	private final ProfilePageObjects profilePageObjects;
	private final CommonStepDefinitions commonStepDefination;

	public ProfileStepDefinations(ScenarioContext scenarioContext, ControlActions ca,
			DataProvider dataProvider, ProfilePageObjects profilePageObjects,
			CommonStepDefinitions commonStepDefination) {
		this.controlActions = ca;
		this.commonStepDefination = commonStepDefination;
		this.profilePageObjects = profilePageObjects;
	}

	public void fillCompleteProfileForm() throws Exception {
		Locator title = profilePageObjects.getDropdownMapping("Title", null);
		controlActions.selectMatOption(title, "Mr");

		Locator gender = profilePageObjects.getDropdownMapping("Gender", null);
		controlActions.selectMatOption(gender, "Male");

		Locator dateOfBirth = profilePageObjects.getDatePickerMapping("Date of Birth", null);
		controlActions.selectMatDate(dateOfBirth, DataUtils.getDateWithOffset(-365 * 19));

		Locator number1 = profilePageObjects.getTextBoxMapping("Phone Number (WhatsApp)", null, false);
		if (number1.inputValue() != null && number1.inputValue().isEmpty()) {
			number1.clear();
			number1.fill(DataUtils.generateRandomNumber(10));
		}

		Locator number2 = profilePageObjects.getTextBoxMapping("Phone Number (Alternative)", null, false);
		if (number2.inputValue() != null && number2.inputValue().isEmpty()) {
			number2.clear();
			number2.fill(DataUtils.generateRandomNumber(10));
		}

		Locator description = profilePageObjects.getTextBoxMapping("Descrive something about you", null, true);
		description.fill(DataUtils.replacePlaceholders("{RandomText}"));

		Locator presentAddress = profilePageObjects.presentAddress.get();
		Locator presentAddress_AddressLine1 = profilePageObjects.getTextBoxMapping("Address Line 1", presentAddress,
				false);
		presentAddress_AddressLine1.fill(DataUtils.replacePlaceholders("{RandomLocation}"));
		Locator presentAddress_AddressLine2 = profilePageObjects.getTextBoxMapping("Address Line 2", presentAddress,
				false);
		presentAddress_AddressLine2.fill(DataUtils.replacePlaceholders("{RandomLocation}"));
		Locator presentAddress_AddressLine3 = profilePageObjects.getTextBoxMapping("Address Line 3", presentAddress,
				false);
		presentAddress_AddressLine3.fill(DataUtils.replacePlaceholders("{RandomLocation}"));

		Locator presentAddress_Hometown = profilePageObjects.getTextBoxMapping("Hometown", presentAddress, false);
		presentAddress_Hometown.fill(DataUtils.replacePlaceholders("{RandomLocation}"));

		Locator presentAddress_Country = profilePageObjects.getDropdownMapping("Country", presentAddress);
		controlActions.selectMatOption(presentAddress_Country, "India");
		commonStepDefination.i_wait_for_loading_to_complete();
		Locator presentAddress_State = profilePageObjects.getDropdownMapping("State", presentAddress);
		controlActions.selectMatOption(presentAddress_State, "West Bengal");
		commonStepDefination.i_wait_for_loading_to_complete();
		Locator presentAddress_District = profilePageObjects.getDropdownMapping("District", presentAddress);
		controlActions.selectMatOption(presentAddress_District, "North 24 Parganas");

		Locator permanentAddress = profilePageObjects.permanentAddress.get();

		Locator permanentAddress_AddressLine1 = profilePageObjects.getTextBoxMapping("Address Line 1", permanentAddress,
				false);
		permanentAddress_AddressLine1.fill(DataUtils.replacePlaceholders("{RandomLocation}"));
		Locator permanentAddress_AddressLine2 = profilePageObjects.getTextBoxMapping("Address Line 2", permanentAddress,
				false);
		permanentAddress_AddressLine2.fill(DataUtils.replacePlaceholders("{RandomLocation}"));
		Locator permanentAddress_AddressLine3 = profilePageObjects.getTextBoxMapping("Address Line 3", permanentAddress,
				false);
		permanentAddress_AddressLine3.fill(DataUtils.replacePlaceholders("{RandomLocation}"));
		Locator permanentAddress_Hometown = profilePageObjects.getTextBoxMapping("Hometown", permanentAddress, false);
		permanentAddress_Hometown.fill(DataUtils.replacePlaceholders("{RandomLocation}"));

		Locator permanentAddress_Country = profilePageObjects.getDropdownMapping("Country", permanentAddress);
		controlActions.selectMatOption(permanentAddress_Country, "India");
		commonStepDefination.i_wait_for_loading_to_complete();
		Locator permanentAddress_State = profilePageObjects.getDropdownMapping("State", permanentAddress);
		controlActions.selectMatOption(permanentAddress_State, "West Bengal");
		commonStepDefination.i_wait_for_loading_to_complete();
		Locator permanentAddress_District = profilePageObjects.getDropdownMapping("District", permanentAddress);
		controlActions.selectMatOption(permanentAddress_District, "Nadia");

		controlActions.click(profilePageObjects.getButtonMapping("Update", null));
		commonStepDefination.i_wait_for_loading_to_complete();
	}

}
