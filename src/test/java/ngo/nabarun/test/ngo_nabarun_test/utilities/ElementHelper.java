package ngo.nabarun.test.ngo_nabarun_test.utilities;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ngo.nabarun.test.ngo_nabarun_test.helpers.CommonHelpers;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class ElementHelper {

	private WebDriver driver;

	public ElementHelper(ScenarioContext scenarioContext) {
		this.driver = scenarioContext.getDriver();
	}

	public void scrollIntoView(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	public WebDriverWait elementWait() {
		return elementWait(60);
	}

	public WebDriverWait elementWait(int timeout) {
		return new WebDriverWait(driver, Duration.ofSeconds(timeout));
	}

	public void selectMatOption(WebElement selectEl, String value) throws Exception {
		selectEl.click();

		selectOpt(value, 0);
	}

	private void selectOpt(String value, int attempt) throws Exception {
		try {
			List<WebElement> options = elementWait()
					.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//mat-option")));
			Thread.sleep(2000);
			Optional<WebElement> matchingOption = options.stream()
					.filter(option -> option.getText().strip().equalsIgnoreCase(value.strip())).findFirst();

			if (matchingOption.isPresent()) {
				matchingOption.get().click();
			} else {
				List<String> availableOptions = options.stream().map(WebElement::getText).toList();
				throw new RuntimeException("No option '" + value + "' found. Available options: " + availableOptions);
			}

		} catch (StaleElementReferenceException e) {
			if (attempt > 5) {
				throw new RuntimeException("Failed to select after 5 attempt.");
			}
			Thread.sleep(2000);
			attempt++;
			selectOpt(value, attempt);
		}
	}

	public void clickRadioOption(WebElement element, String value) throws Exception {
		WebElement radioOpt = element.findElement(By.xpath(".//*[normalize-space()=\"" + value + "\"]"));
		radioOpt.click();
	}

	public void scrollToTop() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, 0);");
	}

	public void selectMatDate(WebElement element, Date value) throws Exception {
		element.findElement(By.xpath(".//mat-datepicker-toggle//button")).click();
		elementWait().until(ExpectedConditions.presenceOfElementLocated(By.xpath("//mat-calendar")));
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
		driver.findElement(By.cssSelector(".mat-calendar-period-button")).click();
		driver.findElement(By.xpath(
				"//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='" + year + "']"))
				.click();
		driver.findElement(By.xpath("//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='"
				+ monthCode + "']")).click();
		driver.findElement(By.xpath(
				"//button[contains(@class,'mat-calendar-body-cell') and normalize-space(string())='" + day + "']"))
				.click();

	}

	public void uploadFile(WebElement element, String value) throws Exception {
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

	public void uploadFileFromResource(WebElement element, String value) {
		String filePath = CommonHelpers.getFileFromResources(value);
		element.findElement(By.xpath(".//input[@type='file']")).sendKeys(filePath);
	}

	public void click(WebElement element, int attempt) throws Exception {
		try {
			switch (attempt) {
			case 0:
			case 1:
			case 2:
				elementWait().until(ExpectedConditions.elementToBeClickable(element)).click();
				break;
			case 3:
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].click();", element);
				break;
			default:
				throw new RuntimeException("Failed to click after " + (attempt - 1) + " attempt.");
			}
		} catch (Exception e) {
			Thread.sleep(Duration.ofSeconds(2));
			attempt++;
			click(element, attempt);
		}
	}

	public void click(WebElement element) throws Exception {
		click(element, 0);
	}
}
