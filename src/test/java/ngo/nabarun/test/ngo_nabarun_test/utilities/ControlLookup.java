package ngo.nabarun.test.ngo_nabarun_test.utilities;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.*;

import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DashboardPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DonationPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.HomePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.LoginPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.ProfilePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.WorkflowPageObjects;

public class ControlLookup {

	private final Map<String, Locator> accordionMapping = new HashMap<>();
	private final LoginPageObjects loginPageObjects;
	private final HomePageObjects homePageObjects;
	private final DonationPageObjects donationPageObjects;
	private final DashboardPageObjects dashboardPageObjects;
	private final CommonPageObjects commonPageObjects;
	private final WorkflowPageObjects workflowPageObjects;
	private final ProfilePageObjects profilePageObjects;

	public ControlLookup(CommonPageObjects commonPageObjects, LoginPageObjects loginPageObjects,
			HomePageObjects homePageObjects, DonationPageObjects donationPageObjects,
			DashboardPageObjects dashboardPageObjects,WorkflowPageObjects workflowPageObjects,ProfilePageObjects profilePageObjects) {
		this.commonPageObjects = commonPageObjects;
		this.loginPageObjects = loginPageObjects;
		this.homePageObjects = homePageObjects;
		this.donationPageObjects = donationPageObjects;
		this.dashboardPageObjects = dashboardPageObjects;
		this.workflowPageObjects=workflowPageObjects;
		this.profilePageObjects=profilePageObjects;
	}

	private static final String LOGIN_PAGE = "login";
	private static final String HOME_PAGE = "home";
	private static final String DASHBOARD_PAGE = "dashboard";
	private static final String DONATION_PAGE = "donation";
	private static final String ACCORDION = "accordion";
	private static final String WORKLIST_PAGE = "tasks";
	private static final String REQUEST_PAGE = "request";
	private static final String PROFILE_PAGE = "profile";

	

	public Locator getLookupElement(String elementName, String elementType, String pageName, String pageType) {
		Locator parent = null;
		if (pageType.equalsIgnoreCase("accordion")) {
			parent = accordionMapping.get(pageName);
			pageName = ACCORDION;
		}
		return switch (elementType.toLowerCase()) {
		case "button" -> getButtonLookUp(pageName, elementName, parent);
		case "link" -> getLinkLookUp(pageName, elementName, parent);
		case "text" -> getTextLookUp(pageName, elementName, parent);
		case "textbox" -> getTextBoxLookUp(pageName, elementName, parent, false);
		case "dropdown" -> getDropdownLookUp(pageName, elementName, parent);
		case "radio" -> getRadioLookUp(pageName, elementName, parent);
		case "datepicker" -> getDatePickerLookUp(pageName, elementName, parent);
		case "textarea" -> getTextBoxLookUp(pageName, elementName, parent, true);
		case "fileinput" -> getFileInputLookUp(pageName, elementName, parent);
		default -> throw new IllegalArgumentException("Unknown element type: " + elementType);
		};
	}

	private Locator getFileInputLookUp(String pageName, String elementName, Locator parentContext) {
		return switch (pageName.toLowerCase()) {
		case ACCORDION -> commonPageObjects.getFileInputMapping(elementName, parentContext);
		case HOME_PAGE -> homePageObjects.getFileInputMapping(elementName, parentContext);
		case LOGIN_PAGE -> loginPageObjects.getFileInputMapping(elementName, parentContext);
		case DASHBOARD_PAGE -> dashboardPageObjects.getFileInputMapping(elementName, parentContext);
		case DONATION_PAGE -> donationPageObjects.getFileInputMapping(elementName, parentContext);
		case WORKLIST_PAGE,REQUEST_PAGE -> workflowPageObjects.getFileInputMapping(elementName, parentContext);
		case PROFILE_PAGE -> profilePageObjects.getFileInputMapping(elementName, parentContext);

		default -> throw new RuntimeException("Invalid page " + pageName);
		};
	}

	private Locator getDatePickerLookUp(String pageName, String elementName, Locator parentContext) {
		return switch (pageName.toLowerCase()) {
		case ACCORDION -> commonPageObjects.getDatePickerMapping(elementName, parentContext);
		case HOME_PAGE -> homePageObjects.getDatePickerMapping(elementName, parentContext);
		case LOGIN_PAGE -> loginPageObjects.getDatePickerMapping(elementName, parentContext);
		case DASHBOARD_PAGE -> dashboardPageObjects.getDatePickerMapping(elementName, parentContext);
		case DONATION_PAGE -> donationPageObjects.getDatePickerMapping(elementName, parentContext);
		case WORKLIST_PAGE,REQUEST_PAGE -> workflowPageObjects.getDatePickerMapping(elementName, parentContext);
		case PROFILE_PAGE -> profilePageObjects.getDatePickerMapping(elementName, parentContext);
		default -> throw new RuntimeException("Invalid page " + pageName);
		};
	}

	private Locator getRadioLookUp(String pageName, String elementName, Locator parentContext) {
		return switch (pageName.toLowerCase()) {
		case ACCORDION -> commonPageObjects.getRadioMapping(elementName, parentContext);
		case HOME_PAGE -> homePageObjects.getRadioMapping(elementName, parentContext);
		case LOGIN_PAGE -> loginPageObjects.getRadioMapping(elementName, parentContext);
		case DASHBOARD_PAGE -> dashboardPageObjects.getRadioMapping(elementName, parentContext);
		case DONATION_PAGE -> donationPageObjects.getRadioMapping(elementName, parentContext);
		case WORKLIST_PAGE,REQUEST_PAGE -> workflowPageObjects.getRadioMapping(elementName, parentContext);
		case PROFILE_PAGE -> profilePageObjects.getRadioMapping(elementName, parentContext);
		default -> throw new RuntimeException("Invalid page " + pageName);
		};
	}

	private Locator getDropdownLookUp(String pageName, String elementName, Locator parentContext) {
		return switch (pageName.toLowerCase()) {
		case ACCORDION -> commonPageObjects.getDropdownMapping(elementName, parentContext);
		case HOME_PAGE -> homePageObjects.getDropdownMapping(elementName, parentContext);
		case LOGIN_PAGE -> loginPageObjects.getDropdownMapping(elementName, parentContext);
		case DASHBOARD_PAGE -> dashboardPageObjects.getDropdownMapping(elementName, parentContext);
		case DONATION_PAGE -> donationPageObjects.getDropdownMapping(elementName, parentContext);
		case WORKLIST_PAGE,REQUEST_PAGE -> workflowPageObjects.getDropdownMapping(elementName, parentContext);
		case PROFILE_PAGE -> profilePageObjects.getDropdownMapping(elementName, parentContext);
		default -> throw new RuntimeException("Invalid page " + pageName);
		};
	}

	private Locator getTextBoxLookUp(String pageName, String elementName, Locator parentContext,
			boolean isTextArea) {
		return switch (pageName.toLowerCase()) {
		case ACCORDION -> commonPageObjects.getTextBoxMapping(elementName, parentContext, isTextArea);
		case HOME_PAGE -> homePageObjects.getTextBoxMapping(elementName, parentContext, isTextArea);
		case LOGIN_PAGE -> loginPageObjects.getTextBoxMapping(elementName, parentContext, isTextArea);
		case DASHBOARD_PAGE -> dashboardPageObjects.getTextBoxMapping(elementName, parentContext, isTextArea);
		case DONATION_PAGE -> donationPageObjects.getTextBoxMapping(elementName, parentContext, isTextArea);
		case WORKLIST_PAGE, REQUEST_PAGE -> workflowPageObjects.getTextBoxMapping(elementName, parentContext,isTextArea);
        case PROFILE_PAGE -> profilePageObjects.getTextBoxMapping(elementName, parentContext,isTextArea);
		default -> throw new RuntimeException("Invalid page " + pageName);
		};
	}

	private Locator getTextLookUp(String pageName, String elementName, Locator parentContext) {
		return switch (pageName.toLowerCase()) {
		case ACCORDION -> commonPageObjects.getTextMapping(elementName, parentContext);
		case HOME_PAGE -> homePageObjects.getTextMapping(elementName, parentContext);
		case LOGIN_PAGE -> loginPageObjects.getTextMapping(elementName, parentContext);
		case DASHBOARD_PAGE -> dashboardPageObjects.getTextMapping(elementName, parentContext);
		case DONATION_PAGE -> donationPageObjects.getTextMapping(elementName, parentContext);
		case WORKLIST_PAGE, REQUEST_PAGE -> workflowPageObjects.getTextMapping(elementName, parentContext);
        case PROFILE_PAGE -> profilePageObjects.getTextMapping(elementName, parentContext);
		default -> throw new RuntimeException("Invalid page " + pageName);
		};
	}

	public Locator getLinkLookUp(String pageName, String elementName, Locator parentContext) {
		return switch (pageName.toLowerCase()) {
		case ACCORDION -> commonPageObjects.getLinkMapping(elementName, parentContext);
		case HOME_PAGE -> homePageObjects.getLinkMapping(elementName, parentContext);
		case LOGIN_PAGE -> loginPageObjects.getLinkMapping(elementName, parentContext);
		case DASHBOARD_PAGE -> dashboardPageObjects.getLinkMapping(elementName, parentContext);
		case DONATION_PAGE -> donationPageObjects.getLinkMapping(elementName, parentContext);
		case WORKLIST_PAGE, REQUEST_PAGE -> workflowPageObjects.getLinkMapping(elementName, parentContext);
        case PROFILE_PAGE -> profilePageObjects.getLinkMapping(elementName, parentContext);
		default -> throw new RuntimeException("Invalid page " + pageName);
		};
	}

	public Locator getButtonLookUp(String pageName, String elementName, Locator parentContext) {
		return switch (pageName.toLowerCase()) {
		case ACCORDION -> commonPageObjects.getButtonMapping(elementName, parentContext);
		case HOME_PAGE -> homePageObjects.getButtonMapping(elementName, parentContext);
		case LOGIN_PAGE -> loginPageObjects.getButtonMapping(elementName, parentContext);
		case DASHBOARD_PAGE -> dashboardPageObjects.getButtonMapping(elementName, parentContext);
		case DONATION_PAGE -> donationPageObjects.getButtonMapping(elementName, parentContext);
		case WORKLIST_PAGE, REQUEST_PAGE -> workflowPageObjects.getButtonMapping(elementName, parentContext);
        case PROFILE_PAGE -> profilePageObjects.getButtonMapping(elementName, parentContext);
		default -> throw new RuntimeException("Invalid page " + pageName);
		};
	}

	public void setAccordionMapping(String accordionName, Locator locator) {
		accordionMapping.put(accordionName, locator);
	}
	
	public Locator getAccordionMapping(String accordionName) {
		return accordionMapping.get(accordionName);
	}

}
