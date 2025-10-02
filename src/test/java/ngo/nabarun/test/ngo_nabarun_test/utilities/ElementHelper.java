package ngo.nabarun.test.ngo_nabarun_test.utilities;

import com.microsoft.playwright.*;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;



import ngo.nabarun.test.ngo_nabarun_test.configs.Configs;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utils.CommonUtils;

public class ElementHelper {

    private final Page page;

    public ElementHelper(ScenarioContext scenarioContext) {
        this.page = scenarioContext.getPage();
    }

    public void scrollIntoView(Locator element) {
        element.scrollIntoViewIfNeeded();
    }

    public void elementWait(Locator element, int timeoutSeconds) {
        element.waitFor(new Locator.WaitForOptions().setTimeout(timeoutSeconds * 1000));
    }

    public void selectMatOption(Locator selectEl, String value) throws Exception {
        selectEl.waitFor();
        selectEl.click();
        List<Locator> options = page.locator("xpath=//mat-option").all();
       // options.waitFor();
        for (Locator option : options) {
            if (option.textContent().trim().equalsIgnoreCase(value.trim())) {
                option.click();
                return;
            }
        }
        throw new Exception("Option not found: " + value);
    }

    public void click(Locator element) {
        element.click();
    }

    public void clickRadioOption(Locator element, String value) throws Exception {
        // scrollIntoView(element);
        Locator radioOpt = element.locator(".//*[normalize-space()=\"" + value + "\"]");
        radioOpt.click();
    }

    public void scrollToTop() {
        page.evaluate("window.scrollTo(0, 0)");
    }

    public void selectMatDate(Locator element, Date value) throws Exception {
        element.locator(".//mat-datepicker-toggle//button").click();
        //elementWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath("//mat-calendar")));
        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        int year = cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DATE);
        String monthCode = switch (cal.get(Calendar.MONTH)) {
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
        page.locator(".mat-calendar-period-button").click();
        List<Locator> dateCheck = page.locator(
                "//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='" + year + "']").all();
        while (dateCheck.isEmpty()) {
            page.locator(".mat-calendar-previous-button").click();
            dateCheck = page.locator("//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='" + year
                    + "']").all();
        }
        page.locator(
                        "//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='" + year + "']")
                .click();
        page.locator("//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='"
                + monthCode + "']").click();
        page.locator(
                        "//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='" + day + "']")
                .click();

    }

    public void uploadFile(Locator element, String value) throws Exception {
        element.click();

        Thread.sleep(2000);
        // Use Robot class to handle OS-level file upload
        Robot robot = new Robot();
        robot.delay(2000);

        // Copy file path to clipboard
        StringSelection filePath = new StringSelection(value);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(filePath, null);

        // Press CTRL + V to paste
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        Thread.sleep(1000);

        // Press Enter to confirm
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(2000);
    }

    public void uploadFileFromResource(Locator element, String value) {
        String filePath = CommonUtils.getFileFromResources(value);
        element.locator(".//input[@type='file']").setInputFiles(Path.of(filePath));
    }

    public void click(Locator element, int attempt) throws Exception {
        //
        try {
            switch (attempt) {
                case 0:
                case 1:
                case 2:
                    element.click();
                    break;
                case 3:
                    page.evaluate("arguments[0].click();", element);
                    break;
                default:
                    throw new RuntimeException("Failed to click after " + (attempt - 1) + " attempt.");
            }
        } catch (Exception e) {
            Thread.sleep(2000);
            attempt++;
            scrollIntoView(element);
            click(element, attempt);
        }
    }

    public boolean isElementPresent(Locator locator, int timeout) {
//		try {
//			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
//			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
//			return true;
//		} catch (Exception e) {
//			return false;
//		} finally {
//			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Configs.IMPLICIT_WAIT));
//		}
        return true;
    }
}