package ngo.nabarun.test.ngo_nabarun_test.utilities;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utils.CommonUtils;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages UI interactions and complex orchestration of actions.
 * Centralizes common Playwright patterns and provides high-level user actions.
 */
public class ControlActions {
    private static final Logger logger = LogManager.getLogger(ControlActions.class);
    private final ScenarioContext scenarioContext;
    private static final SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat("dd/MM/yyyy");

    public ControlActions(ScenarioContext scenarioContext) {
        this.scenarioContext = scenarioContext;
    }

    /**
     * Executes a high-level UI action based on an action name and target element.
     * 
     * @param actionName  The action to perform (e.g., "ENTER", "SELECT", "CLICK",
     *                    "UPLOAD")
     * @param locator     The target Playwright locator
     * @param elementType The semantic type of the element (e.g., "DROPBOX",
     *                    "DATEPICKER", "RADIO")
     * @param value       The value to provide (if applicable)
     * @throws Exception if the action cannot be performed or is unknown
     */
    public void executeAction(String actionName, Locator locator, String elementType, String fieldValue) {
        String resolvedValue = DataUtils.resolveData(fieldValue, scenarioContext);
        logger.debug("Executing Action: {} on Element Type: {} with Value: {}", actionName, elementType, resolvedValue);
        switch (actionName.toUpperCase()) {
            case "ENTER" -> {
                locator.clear();
                locator.fill(resolvedValue);
            }
            case "SELECT" -> {
                switch (elementType.toLowerCase()) {
                    case "dropdown" -> selectMatOption(locator, resolvedValue);
                    case "multiselect", "dropdown-multi" -> selectMatOptions(locator, resolvedValue);
                    case "datepicker" -> selectMatDate(locator, resolvedValue);
                    default -> {
                        logger.error("Cannot SELECT on unknown element type: {}", elementType);
                        throw new IllegalArgumentException("Cannot SELECT on element type: " + elementType);
                    }
                }
            }
            case "CLICK" -> {
                if ("radio".equalsIgnoreCase(elementType)) {
                    clickRadioOption(locator, resolvedValue);
                } else {
                    locator.click();
                }
            }
            case "UPLOAD" -> uploadFileByFileChooser(locator, resolvedValue);
            case "SCROLL" -> locator.scrollIntoViewIfNeeded();
            case "CLICK AND HOLD" -> locator.hover();
            default -> {
                logger.error("Unknown high-level action requested: {}", actionName);
                throw new UnsupportedOperationException("Unknown action: " + actionName);
            }
        }
    }

    /*
     * Scroll the element into view
     */
    public void scrollIntoView(Locator element) {
        element.scrollIntoViewIfNeeded();
    }

    /*
     * Wait for the element to be visible
     */
    public void elementWait(Locator element, int timeoutSeconds) {
        element.waitFor(new Locator.WaitForOptions().setTimeout(timeoutSeconds * 1000));
    }

    /**
     * Selects a single option from a Mat-Select dropdown.
     */
    public void selectMatOption(Locator selectEl, String value) {
        selectEl.waitFor();
        selectEl.click();

        Page page = this.scenarioContext.getPage();
        Locator optionsLocator = AngularMaterial.MatActiveOptions(page);

        List<Locator> options = optionsLocator.all();
        for (Locator option : options) {
            String text = option.textContent().toLowerCase().trim();
            if (text.contains(value.toLowerCase().trim())) {
                option.click();
                return;
            }
        }
        throw new UnsupportedOperationException("Option not found: " + value);
    }

    /**
     * Selects multiple options in a Mat-Select. Value is comma-separated (e.g. "A,
     * B, C").
     */
    public void selectMatOptions(Locator selectEl, String commaSeparatedValues) {
        List<String> values = Arrays.stream(commaSeparatedValues.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
        if (values.isEmpty())
            return;

        selectEl.waitFor();
        selectEl.click();

        Page page = this.scenarioContext.getPage();
        Locator optionsLocator = AngularMaterial.MatActiveOptions(page);

        for (String val : values) {
            List<Locator> options = optionsLocator.all();
            for (Locator option : options) {
                if (option.textContent().toLowerCase().trim().contains(val.toLowerCase().trim())) {
                    option.click();
                    break;
                }
            }
        }
    }

    /**
     * Clicks a specific radio option within a container.
     */
    public void clickRadioOption(Locator element, String value) {
        element.getByText(value).click();
    }

    public void scrollToTop() {
        this.scenarioContext.getPage().evaluate("window.scrollTo(0, 0)");
    }

    /**
     * Orchestrates selection in a Mat-DatePicker using the calendar UI.
     */
    public void selectMatDate(Locator element, String value) {
        try {
            Date date = DEFAULT_SDF.parse(value);
            selectMatDate(element, date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + value, e);
        }
    }

    public void selectMatDate(Locator element, Date value) {
        Page page = this.scenarioContext.getPage();
        AngularMaterial.MatDatePickerToggle(element).click();

        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DATE);
        String monthCode = getMonthCode(cal.get(Calendar.MONTH));

        AngularMaterial.MatDatePickerPeriodButton(page).click();

        List<Locator> dateCheck = AngularMaterial.MatCalendarCell(page, String.valueOf(year)).all();

        while (dateCheck.isEmpty()) {
            AngularMaterial.MatDatePickerPreviousButton(page).click();
            dateCheck = AngularMaterial.MatCalendarCell(page, String.valueOf(year)).all();
        }

        AngularMaterial.MatCalendarCell(page, String.valueOf(year)).click();
        AngularMaterial.MatCalendarCell(page, monthCode).click();
        AngularMaterial.MatCalendarCell(page, String.valueOf(day)).click();
    }

    /**
     * Uploads a file using the Playwright FileChooser approach.
     */
    public void uploadFileByFileChooser(Locator uploadTrigger, String fileName) {
        String filePath = CommonUtils.getFileFromResources(fileName);
        FileChooser fileChooser = scenarioContext.getPage().waitForFileChooser(uploadTrigger::click);
        fileChooser.setFiles(Path.of(filePath));
    }

    /**
     * Uploads a file using OS-level Robot class (fallback/legacy).
     */
    public void uploadFileByRobot(Locator element, String path) throws Exception {
        element.click();
        Thread.sleep(2000);
        Robot robot = new Robot();
        robot.delay(2000);

        StringSelection filePath = new StringSelection(path);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePath, null);

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(1000);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(2000);
    }

    /**
     * Basic click action on a locator.
     */
    public void click(Locator locator) {
        logger.debug("Performing click operation.");
        locator.click();
    }

    public boolean isElementPresent(Locator locator, int timeout) {
        try {
            locator.waitFor(new Locator.WaitForOptions().setTimeout(timeout * 1000));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if an element is present using a CSS/XPath selector.
     */
    public boolean isElementPresent(String selector, int timeout) {
        Locator locator = scenarioContext.getPage().locator(selector);
        return isElementPresent(locator, timeout);
    }

    private String getMonthCode(int calendarMonth) {
        return switch (calendarMonth) {
            case Calendar.JANUARY -> "JAN";
            case Calendar.FEBRUARY -> "FEB";
            case Calendar.MARCH -> "MAR";
            case Calendar.APRIL -> "APR";
            case Calendar.MAY -> "MAY";
            case Calendar.JUNE -> "JUN";
            case Calendar.JULY -> "JUL";
            case Calendar.AUGUST -> "AUG";
            case Calendar.SEPTEMBER -> "SEP";
            case Calendar.OCTOBER -> "OCT";
            case Calendar.NOVEMBER -> "NOV";
            case Calendar.DECEMBER -> "DEC";
            default -> "";
        };
    }

    public void waitUntilDisappear(String selector) {
        Page page = this.scenarioContext.getPage();
        page.waitForSelector(selector, new Page.WaitForSelectorOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
    }
}