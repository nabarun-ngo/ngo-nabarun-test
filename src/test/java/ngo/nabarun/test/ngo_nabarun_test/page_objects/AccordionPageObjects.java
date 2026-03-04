package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import com.microsoft.playwright.Locator;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class AccordionPageObjects extends CommonPageObjects {

    public AccordionPageObjects(ScenarioContext scenarioContext) {
        super(scenarioContext);
    }

    public Locator getAccordions(Locator parent, int index) {
        if (index == -1) {
            return scope(parent).locator("//mat-expansion-panel");
        }
        return findLocator("(//mat-expansion-panel)" + "[" + index + "]", parent, FindBy.XPATH);
    }

    @Override
    public Locator getFormMapping(String sectionName, Locator parent) {
        return findLocator("//*[normalize-space()='" + sectionName + "']/ancestor::form",
                parent, FindBy.XPATH);
    }

    public Locator getReadOnlyField(String fieldName, Locator section) {
        return findLocator("//*[normalize-space()='" + fieldName + "']/following-sibling::*",
                section, FindBy.XPATH).first();
    }

}
