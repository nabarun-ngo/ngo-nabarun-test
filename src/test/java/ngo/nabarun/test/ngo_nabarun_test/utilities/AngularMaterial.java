package ngo.nabarun.test.ngo_nabarun_test.utilities;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

/**
 * Centrally manages Angular Material specific locators used by the framework.
 * This class isolates framework-level locators from interaction logic.
 */
public class AngularMaterial {

    // Mat-Select / Mat-Option related

    // Mat-DatePicker related
    private static final String DATEPICKER_TOGGLE = "mat-datepicker-toggle button";
    private static final String CALENDAR_PERIOD_BUTTON = ".mat-calendar-period-button";
    private static final String CALENDAR_PREVIOUS_BUTTON = ".mat-calendar-previous-button";
    private static final String CALENDAR_NEXT_BUTTON = ".mat-calendar-next-button";

    public static Locator MatIcon(Locator parent, String iconName) {
        if (parent == null) {
            throw new RuntimeException("cannot find icon " + iconName + ". The Parent Locator is null");
        }
        return parent.locator("xpath=//mat-icon[text()='" + iconName + "']");
    }

    public static Locator MatIcon(Page page, String iconName) {
        if (page == null) {
            throw new RuntimeException("cannot find icon " + iconName + ". The Page is null");
        }
        return page.locator("xpath=//mat-icon[text()='" + iconName + "']");
    }

    public static Locator MatActiveOptions(Page page) {
        if (page == null) {
            throw new RuntimeException("cannot find active options. The Page is null");
        }
        return page.locator("mat-option, [role='option'], .mat-option");
    }

    public static Locator MatDatePickerToggle(Locator parent) {
        if (parent == null) {
            throw new RuntimeException("cannot find date picker toggle. The Parent Locator is null");
        }
        return parent.locator(DATEPICKER_TOGGLE);
    }

    public static Locator MatDatePickerPeriodButton(Page page) {
        if (page == null) {
            throw new RuntimeException("cannot find date picker toggle. The Page is null");
        }
        return page.locator(CALENDAR_PERIOD_BUTTON);
    }

    public static Locator MatDatePickerPreviousButton(Page page) {
        if (page == null) {
            throw new RuntimeException("cannot find date picker toggle. The Page is null");
        }
        return page.locator(CALENDAR_PREVIOUS_BUTTON);
    }

    public static Locator MatDatePickerNextButton(Page page) {
        if (page == null) {
            throw new RuntimeException("cannot find date picker toggle. The Page is null");
        }
        return page.locator(CALENDAR_NEXT_BUTTON);
    }

    /**
     * Build an XPath for a calendar cell that exactly contains the given value.
     */
    public static Locator MatCalendarCell(Page page, String content) {
        if (page == null) {
            throw new RuntimeException("cannot find date picker toggle. The Page is null");
        }
        return page.locator(
                "//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='" + content + "']");
    }

}
