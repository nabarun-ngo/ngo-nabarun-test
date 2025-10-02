package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import com.microsoft.playwright.*;
import java.util.function.Supplier;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class CommonPageObjects {

    public enum FindBy {
        TEXT, LABEL, XPATH, CSS,ANY
    }

    private final ScenarioContext scenarioContext;

    public CommonPageObjects(ScenarioContext scenarioContext) {
		this.scenarioContext =scenarioContext ;
	}

	public String PageLoaderSelector = "//*[normalize-space(text())='Please wait, Things are getting ready...']";
	public String PageHeaderSelector = "//app-page-title//h1";
	public Locator PageHeader() {
		return findLocator(PageHeaderSelector);
	}
	public Locator Popup_Container() {
		return findLocator("//mat-dialog-container");
	}

	public Locator getAccordion(int i, Locator parent) {
		return findLocator("(//mat-expansion-panel)[" + i + "]",parent,FindBy.XPATH);
	}

    protected Locator findLocator(String selector, Locator parent,FindBy selectBy) {
        Page root = scenarioContext.getPage();
        //parent = root.locator("body");
        return switch (selectBy) {
            case TEXT -> parent != null ? parent.getByText(selector) : root.getByText(selector);
            case LABEL -> parent != null ? parent.getByLabel(selector) : root.getByLabel(selector);
            case XPATH -> parent != null ? parent.locator("xpath=" + selector) :root.locator("xpath=" + selector);
            case CSS -> parent != null ? parent.locator("css=" + selector) : root.locator("css=" + selector);
            case ANY -> parent != null ? parent.locator(selector) : root.locator(selector);
        };
    }

    protected Locator findLocator(String selector) {
        return findLocator(selector,null,FindBy.ANY);
    }

	public Locator getButtonMapping(String elementName,Locator parent) {
		return findLocator("//button[normalize-space(string())='" + elementName + "']", parent,FindBy.XPATH);
	}

	public Locator getLinkMapping(String elementName,Locator parent) {
		return findLocator("//a[normalize-space(text())='" + elementName + "']",parent,FindBy.XPATH);
	}

	public Locator getTextMapping(String elementName,Locator parent) {
		return findLocator("//*[normalize-space(text())='" + elementName + "']",parent,FindBy.XPATH);
	}

	public Locator getTextBoxMapping(String elementName,Locator parent, boolean isTextArea) {
		String selector = "//*[normalize-space(text())='" + elementName + "']/following-sibling::*"
				+ (isTextArea ? "//textarea" : "//input");
		return findLocator(selector,parent,FindBy.XPATH);
	}

	public Locator getDropdownMapping(String elementName,Locator parent) {
		return findLocator("//*[normalize-space(text())='" + elementName + "']/following-sibling::*//mat-select",parent,FindBy.XPATH);
	}

	public Locator getRadioMapping(String elementName,Locator parent) {
		return findLocator("//*[normalize-space(text())='" + elementName + "']/following-sibling::*",parent,FindBy.XPATH);
	}

	public Locator getDatePickerMapping(String elementName,Locator parent) {
		return findLocator("//*[normalize-space(text())='" + elementName + "']/following-sibling::*",parent,FindBy.XPATH);
	}

	public Locator getFileInputMapping(String elementName,Locator parent) {
		return findLocator("//*[normalize-space(text())='" + elementName + "']/following-sibling::*//input[@type='file']",parent,FindBy.XPATH);
	}
}
