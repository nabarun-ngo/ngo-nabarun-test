package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import com.microsoft.playwright.Locator;

public interface ICommonPageObject {

    Locator PageHeader(String title);

    String PageLoaderSelector();

    Locator Popup_Container();

    Locator Search_Container();

    Locator getAccordions(Locator parent, int index);

    Locator getDropdownMapping(String elementName, Locator parent);

    Locator getDatePickerMapping(String elementName, Locator parent);

    Locator getTimePickerMapping(String elementName, Locator parent);

    Locator getButtonMapping(String elementName, Locator parent);

    Locator getLinkMapping(String elementName, Locator parent);

    Locator getTextMapping(String elementName, Locator parent);

    Locator getTextBoxMapping(String elementName, Locator parent, boolean isTextArea);

    Locator getRadioMapping(String elementName, Locator parent);

    Locator getFileInputMapping(String elementName, Locator parent);

    Locator getCheckboxMapping(String elementName, Locator parent);

    Locator getFormMapping(String formName, Locator parent);

}
