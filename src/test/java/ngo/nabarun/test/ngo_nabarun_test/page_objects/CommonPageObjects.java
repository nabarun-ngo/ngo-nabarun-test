package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;
import java.util.regex.Pattern;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utilities.AngularMaterial;

public class CommonPageObjects extends BasePageObjects implements ICommonPageObject {

	public final Supplier<Locator> Adv_Search_Submit = () -> findLocator("//button[normalize-space(text())='Search']",
			Search_Container(), FindBy.XPATH);
	public final Supplier<Locator> Simple_Search_Input = () -> findLocator("#simple-search", Search_Container(),
			FindBy.ANY);
	public final Supplier<Locator> Create_Accordion = () -> findLocator("//mat-expansion-panel[@id='create']");

	public CommonPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	@Override
	public Locator PageHeader(String title) {
		return page().locator("app-page-title h1")
				.filter(new Locator.FilterOptions()
						.setHasText(Pattern.compile(title, Pattern.CASE_INSENSITIVE)));
	}

	@Override
	public String PageLoaderSelector() {
		return "//*[normalize-space(text())='Please wait, Things are getting ready...']";
	}

	@Override
	public Locator Popup_Container() {
		return findLocator("//mat-dialog-container");
	}

	@Override
	public Locator Search_Container() {
		return findLocator("app-search-and-advanced-search-form");
	}

	@Override
	public Locator getAccordions(Locator parent, int index) {
		if (index == -1) {
			return scope(parent).locator("//mat-expansion-panel");
		}
		return findLocator("(//mat-expansion-panel)" + "[" + index + "]", parent, FindBy.XPATH);
	}

	@Override
	public Locator getDropdownMapping(String elementName, Locator parent) {
		return findLocator("//*[normalize-space(text())='" + elementName + "']/following-sibling::*//mat-select",
				parent, FindBy.XPATH);
	}

	@Override
	public Locator getDatePickerMapping(String elementName, Locator parent) {
		return findLocator(
				"//*[normalize-space(text())='" + elementName + "']/following-sibling::*",
				parent, FindBy.XPATH);
	}

	@Override
	public Locator getTimePickerMapping(String elementName, Locator parent) {
		return findLocator(
				"//*[normalize-space(text())='" + elementName + "']/following-sibling::*//mat-timepicker-toggle",
				parent, FindBy.XPATH);
	}

	@Override
	public Locator getButtonMapping(String elementName, Locator parent) {
		System.out.println("Element Name: " + elementName);
		return switch (elementName.toLowerCase()) {
			case "add icon" -> AngularMaterial.MatIcon(scope(parent), "add");
			default ->
				scope(parent).getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(elementName));
		};
	}

	@Override
	public Locator getLinkMapping(String elementName, Locator parent) {
		return switch (elementName) {
			default ->
				super.scope(parent).getByRole(AriaRole.LINK, new Locator.GetByRoleOptions().setName(elementName));
		};
	}

	@Override
	public Locator getTextMapping(String elementName, Locator parent) {
		return switch (elementName) {
			default ->
				super.scope(parent).getByText(elementName);
		};
	}

	@Override
	public Locator getTextBoxMapping(String elementName, Locator parent, boolean isTextArea) {
		String selector = "//*[normalize-space(text())='" + elementName + "']/following-sibling::*"
				+ (isTextArea ? "//textarea" : "//input");
		return findLocator(selector, parent, FindBy.XPATH);
	}

	@Override
	public Locator getRadioMapping(String elementName, Locator parent) {
		return findLocator("//*[normalize-space(text())='" + elementName + "']/following-sibling::*//mat-radio-group",
				parent, FindBy.XPATH);
	}

	@Override
	public Locator getFileInputMapping(String elementName, Locator parent) {
		return switch (elementName) {
			case "Upload" -> findLocator("app-file-upload label", parent, FindBy.ANY);
			default ->
				findLocator(
						"//*[normalize-space(text())='" + elementName + "']/following-sibling::*//input[@type='file']",
						parent, FindBy.XPATH);
		};
	}

	@Override
	public Locator getCheckboxMapping(String elementName, Locator parent) {
		return switch (elementName) {
			default ->
				super.scope(parent).getByRole(AriaRole.CHECKBOX, new Locator.GetByRoleOptions().setName(elementName));
		};
	}

	@Override
	public Locator getFormMapping(String formName, Locator parent) {
		return super.scope(parent);
	}
}
