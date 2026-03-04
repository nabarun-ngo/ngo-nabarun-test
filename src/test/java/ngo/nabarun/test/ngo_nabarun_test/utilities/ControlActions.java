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
import ngo.nabarun.test.ngo_nabarun_test.page_objects.BasePageObjects;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.BasePageObjects.HighlightMode;
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
                if ("autocomplete".equalsIgnoreCase(elementType)) {
                    selectAutocompleteOption(locator, resolvedValue);
                } else {
                    BasePageObjects.highlight(locator, HighlightMode.ENTER);
                    locator.clear();
                    locator.fill(resolvedValue);
                }
            }
            case "SELECT" -> {
                switch (elementType.toLowerCase()) {
                    case "dropdown" -> selectMatOption(locator, resolvedValue);
                    case "multiselect", "dropdown-multi" -> selectMatOptions(locator, resolvedValue);
                    case "datepicker" -> selectMatDate(locator, resolvedValue);
                    case "radio" -> clickRadioOption(locator, resolvedValue);
                    case "autocomplete" -> selectAutocompleteOption(locator, resolvedValue);
                    default -> {
                        logger.error("Cannot SELECT on unknown element type: {}", elementType);
                        throw new IllegalArgumentException("Cannot SELECT on element type: " + elementType);
                    }
                }
            }
            case "CLICK" -> {
                BasePageObjects.highlight(locator, HighlightMode.CLICK);
                if ("radio".equalsIgnoreCase(elementType)) {
                    clickRadioOption(locator, resolvedValue);
                } else {
                    locator.click();
                }
            }
            case "UPLOAD" -> {
                BasePageObjects.highlight(locator, HighlightMode.UPLOAD);
                uploadFileByFileChooser(locator, resolvedValue);
            }
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
        logger.info("Attempting to select Mat-Option: '{}'", value);
        selectEl.scrollIntoViewIfNeeded();
        selectEl.waitFor(
                new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

        Page page = this.scenarioContext.getPage();
        boolean opened = false;

        // Retry loop for opening the dropdown if it fails to appear
        for (int i = 0; i < 3; i++) {
            BasePageObjects.highlight(selectEl, HighlightMode.SELECT);
            selectEl.click();
            try {
                // Wait for any option to become visible as a sign that the dropdown is open
                page.locator("mat-option, [role='option']").first().waitFor(new Locator.WaitForOptions()
                        .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                        .setTimeout(3000));
                opened = true;
                break;
            } catch (Exception e) {
                logger.warn("Dropdown overlay did not appear, retrying click... ({}/3)", i + 1);
            }
        }

        if (!opened) {
            throw new RuntimeException("Failed to open dropdown after 3 attempts.");
        }

        // Give a tiny moment for the layout to stabilize
        page.waitForTimeout(300);

        // We use Playwright's getByText on the common option roles.
        // This is usually more robust than manual regex filtering for Material
        // components.
        Locator targetOption = page.locator("mat-option, [role='option']")
                .getByText(value, new Locator.GetByTextOptions().setExact(false))
                .first();

        try {
            // Using a shorter timeout for the primary attempt to allow for fallback if
            // needed
            targetOption.waitFor(new Locator.WaitForOptions()
                    .setState(com.microsoft.playwright.options.WaitForSelectorState.ATTACHED)
                    .setTimeout(5000));

            BasePageObjects.highlight(targetOption, HighlightMode.SELECT);
            targetOption.scrollIntoViewIfNeeded();
            targetOption.click(new Locator.ClickOptions().setForce(true));
            logger.info("Successfully selected option: '{}'", value);
        } catch (Exception e) {
            // Fallback: iterate and find by text content if getByText failed
            List<Locator> allOptions = page.locator("mat-option, [role='option']").all();
            for (Locator opt : allOptions) {
                if (opt.textContent().toLowerCase().contains(value.toLowerCase().trim())) {
                    BasePageObjects.highlight(opt, HighlightMode.SELECT);
                    opt.scrollIntoViewIfNeeded();
                    opt.click(new Locator.ClickOptions().setForce(true));
                    logger.info("Selected option '{}' via fallback iteration.", value);
                    return;
                }
            }
            throw new UnsupportedOperationException("Option '" + value + "' not found among options: " +
                    allOptions.stream().map(Locator::textContent).collect(java.util.stream.Collectors.toList()), e);
        }
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

        selectEl.scrollIntoViewIfNeeded();
        selectEl.waitFor(
                new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        BasePageObjects.highlight(selectEl, HighlightMode.SELECT);
        selectEl.click();

        // Give a brief moment for Material animation/panel to attach
        this.scenarioContext.getPage().waitForTimeout(500);

        Page page = this.scenarioContext.getPage();
        Locator optionsLocator = AngularMaterial.MatActiveOptions(page);

        for (String val : values) {
            Locator targetOption = optionsLocator.filter(new Locator.FilterOptions().setHasText(
                    java.util.regex.Pattern.compile(java.util.regex.Pattern.quote(val.trim()),
                            java.util.regex.Pattern.CASE_INSENSITIVE)))
                    .locator("visible=true")
                    .first();
            try {
                targetOption.waitFor(new Locator.WaitForOptions()
                        .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                        .setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));
                BasePageObjects.highlight(targetOption, HighlightMode.SELECT);
                targetOption.click(new Locator.ClickOptions().setForce(true));
            } catch (Exception e) {
                logger.warn("Option '{}' not found or visible in multi-select dropdown.", val);
            }
        }
        // Closing dropdown if it stays open (common for multi-select)
        page.keyboard().press("Escape");
    }

    /**
     * Clicks a specific radio option within a container.
     */
    public void clickRadioOption(Locator element, String value) {
        BasePageObjects.highlight(element.getByText(value), HighlightMode.CLICK);
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
            BasePageObjects.highlight(element, HighlightMode.SELECT);
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

        Locator periodButton = AngularMaterial.MatDatePickerPeriodButton(page);
        periodButton.waitFor(
                new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        periodButton.click();

        Locator yearCell = AngularMaterial.MatCalendarCell(page, String.valueOf(year));

        // Wait for years to be visible, might need to navigate
        while (!yearCell.isVisible()) {
            AngularMaterial.MatDatePickerPreviousButton(page).click();
        }

        yearCell.click();

        Locator monthCell = AngularMaterial.MatCalendarCell(page, monthCode);
        monthCell.waitFor(
                new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        monthCell.click();

        Locator dayCell = AngularMaterial.MatCalendarCell(page, String.valueOf(day));
        dayCell.waitFor(
                new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        dayCell.click();
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
        BasePageObjects.highlight(locator, HighlightMode.CLICK);
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
        try {
            Page page = this.scenarioContext.getPage();
            page.waitForSelector(selector, new Page.WaitForSelectorOptions().setTimeout(timeout * 1000));
            Locator locator = page.locator(selector);
            return locator.isVisible();
        } catch (Exception e) {
            return false;
        }
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

    /**
     * Selects an option from an autocomplete field.
     */
    public void selectAutocompleteOption(Locator inputEl, String value) {
        logger.info("Attempting to select Autocomplete Option: '{}'", value);
        inputEl.scrollIntoViewIfNeeded();

        // 1. Fill the input to trigger suggestions
        inputEl.click();
        inputEl.fill("");
        inputEl.pressSequentially(value, new Locator.PressSequentiallyOptions().setDelay(150));

        Page page = this.scenarioContext.getPage();

        Locator targetOption = page.locator("mat-option, [role='option']")
                .getByText(value, new Locator.GetByTextOptions().setExact(false))
                .first();

        try {
            targetOption.waitFor(new Locator.WaitForOptions()
                    .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE)
                    .setTimeout(Configs.GLOBAL_EXPLICIT_WAIT));

            BasePageObjects.highlight(targetOption, HighlightMode.SELECT);
            targetOption.click(new Locator.ClickOptions().setForce(true));
            logger.info("Clicked on autocomplete suggestion: '{}'", value);
        } catch (Exception e) {
            logger.error("Failed to click autocomplete option '{}'. Trying fallback click.", value);
            page.locator("mat-option, [role='option']").getByText(value, new Locator.GetByTextOptions().setExact(false))
                    .first().click(new Locator.ClickOptions().setForce(true));
        }

    }
}