package ngo.nabarun.test.ngo_nabarun_test.utilities;

import java.util.HashMap;
import java.util.Map;

import com.microsoft.playwright.Locator;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.AccountsPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.CommonPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DashboardPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.DonationPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.HomePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.LoginPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.ProfilePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.WorkflowPageObjects;

/**
 * Central lookup for UI elements by page and element name.
 * Uses a page registry so adding a new page only requires registering it here
 * (one place).
 * When {@code SELF_HEALING=true}, returned locators are wrapped in
 * {@link SelfHealingLocator}
 * for Healenium-like runtime healing on action failure.
 */
public class ControlLookup {

	private final ScenarioContext scenarioContext;
	private final CommonPageObjects commonPageObjects;
	private final Map<String, CommonPageObjects> pageRegistry = new HashMap<>();

	public ControlLookup(ScenarioContext scenarioContext, CommonPageObjects commonPageObjects,
			LoginPageObjects loginPageObjects, HomePageObjects homePageObjects,
			DonationPageObjects donationPageObjects, DashboardPageObjects dashboardPageObjects,
			WorkflowPageObjects workflowPageObjects, ProfilePageObjects profilePageObjects,
			AccountsPageObjects accountsPageObjects) {
		this.scenarioContext = scenarioContext;
		this.commonPageObjects = commonPageObjects;
		register("login", loginPageObjects);
		register("home", homePageObjects);
		register("dashboard", dashboardPageObjects);
		register("donation", donationPageObjects);
		register("tasks", workflowPageObjects);
		register("requests", workflowPageObjects);
		register("profile", profilePageObjects);
		register("members", profilePageObjects);
		register("accounts", accountsPageObjects);
		register("transactions", accountsPageObjects);
	}

	/**
	 * Register a page so steps can use "at \"PageName\" page". Add new pages here.
	 */
	private void register(String pageName, CommonPageObjects pageObjects) {
		pageRegistry.put(pageName.toLowerCase(), pageObjects);
	}

	/**
	 * Returns a self-healing locator wrapper. Use
	 * {@link SelfHealingLocator#getLocator()} when
	 * you need a raw {@link Locator} (e.g. for ControlActions or assertions).
	 */
	public SelfHealingLocator getLookupElement(String elementName, String elementType, String pageName) {
		if (pageName == null) {
			throw new RuntimeException("PageName cannot be null");
		}
		return getLookupElement(elementName, elementType, pageName, null);
	}

	/**
	 * Returns a self-healing locator wrapper. Use
	 * {@link SelfHealingLocator#getLocator()} when
	 * you need a raw {@link Locator} (e.g. for ControlActions or assertions).
	 */
	public SelfHealingLocator getLookupElement(String elementName, String elementType, Locator parent) {
		if (parent == null) {
			throw new RuntimeException("Parent cannot be null");
		}
		return getLookupElement(elementName, elementType, null, parent);
	}

	/**
	 * Returns a self-healing locator wrapper. Use
	 * {@link SelfHealingLocator#getLocator()} when
	 * you need a raw {@link Locator} (e.g. for ControlActions or assertions).
	 */
	private SelfHealingLocator getLookupElement(String elementName, String elementType, String pageName,
			Locator parent) {
		CommonPageObjects pageObj = pageName == null ? commonPageObjects : pageRegistry.get(pageName.toLowerCase());
		if (pageObj == null) {
			throw new RuntimeException("Unknown page: " + pageName + ". Register it in ControlLookup#register().");
		}
		Locator locator = switch (elementType.toLowerCase()) {
			case "button" -> pageObj.getButtonMapping(elementName, parent);
			case "link" -> pageObj.getLinkMapping(elementName, parent);
			case "text" -> pageObj.getTextMapping(elementName, parent);
			case "section" -> pageObj.getTextMapping(elementName, parent);
			case "textbox" -> pageObj.getTextBoxMapping(elementName, parent, false);
			case "dropdown", "multiselect" -> pageObj.getDropdownMapping(elementName, parent);
			case "radio" -> pageObj.getRadioMapping(elementName, parent);
			case "datepicker" -> pageObj.getDatePickerMapping(elementName, parent);
			case "textarea" -> pageObj.getTextBoxMapping(elementName, parent, true);
			case "fileinput" -> pageObj.getFileInputMapping(elementName, parent);
			case "checkbox" -> pageObj.getCheckboxMapping(elementName, parent);
			case "timepicker" -> pageObj.getTimePickerMapping(elementName, parent);
			default -> throw new IllegalArgumentException("Unknown element type: " + elementType);
		};
		return new SelfHealingLocator(
				scenarioContext.getPage(), parent, locator, elementType, elementName);
	}

	public Locator getLookupForm(String formName, String pageName) {
		CommonPageObjects pageObj = pageName == null ? commonPageObjects : pageRegistry.get(pageName.toLowerCase());
		if (pageObj == null) {
			throw new RuntimeException("Unknown page: " + pageName + ". Register it in ControlLookup#register().");
		}
		return pageObj.getFormMapping(formName, null);
	}

}
