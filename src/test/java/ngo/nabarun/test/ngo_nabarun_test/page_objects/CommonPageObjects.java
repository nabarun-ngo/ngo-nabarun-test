package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class CommonPageObjects {

	protected WebDriver driver;
	protected WebDriverWait elementWait;

	public CommonPageObjects(ScenarioContext scenarioContext) {
		this.driver = scenarioContext.getDriver();
		this.elementWait = new WebDriverWait(driver, Duration.ofSeconds(60));
	}

	public By PageLoaderLocator = By.xpath(".//*[normalize-space(text())='Please wait, Things are getting ready...']");
	public Supplier<WebElement> PageHeader = () -> driver.findElement(By.xpath("//app-page-title//span"));
	public Function<Integer, WebElement> Accordion = (i) -> driver
			.findElement(By.xpath("(//mat-expansion-panel)[" + i + "]"));

	public WebElement getButtonMapping(String elementName, SearchContext parentContext) {
		return switch (elementName) {
		default -> getSearchContext(parentContext)
				.findElement(By.xpath(".//button[normalize-space(string())=\"" + elementName + "\"]"));
		};
	}

	public WebElement getLinkMapping(String elementName, SearchContext parentContext) {
		return switch (elementName) {
		default -> getSearchContext(parentContext)
				.findElement(By.xpath(".//a[normalize-space(text())=\"" + elementName + "\"]"));
		};
	}

	public WebElement getTextMapping(String elementName, SearchContext parentContext) {
		return switch (elementName) {
		default -> getSearchContext(parentContext)
				.findElement(By.xpath(".//*[normalize-space(text())=\"" + elementName + "\"]"));
		};
	}

	protected SearchContext getSearchContext(SearchContext parentContext) {
		return parentContext == null ? driver : parentContext;
	}

	public WebElement getTextBoxMapping(String elementName, SearchContext parentContext, boolean isTextArea) {
		return switch (elementName) {
		default -> getSearchContext(parentContext).findElement(By.xpath(".//*[normalize-space(text())='" + elementName
				+ "']/following-sibling::*" + (isTextArea ? "//textarea" : "//input")));
		};
	}

	public WebElement getDropdownMapping(String elementName, SearchContext parentContext) {
		return switch (elementName) {
		default -> getSearchContext(parentContext).findElement(
				By.xpath(".//*[normalize-space(text())='" + elementName + "']/following-sibling::*//mat-select"));
		};
	}

	public WebElement getRadioMapping(String elementName, SearchContext parentContext) {
		return switch (elementName) {
		default -> getSearchContext(parentContext)
				.findElement(By.xpath(".//*[normalize-space(text())='" + elementName + "']/following-sibling::*"));
		};
	}

	public WebElement getDatePickerMapping(String elementName, SearchContext parentContext) {
		return switch (elementName) {
		default -> getSearchContext(parentContext)
				.findElement(By.xpath(".//*[normalize-space(text())='" + elementName + "']/following-sibling::*"));
		};
	}

	public WebElement getFileInputMapping(String elementName, SearchContext parentContext) {
		return switch (elementName) {
		default -> getSearchContext(parentContext)
				.findElement(By.xpath(".//*[normalize-space(text())='" + elementName + "']/following-sibling::*//input[@type='file']"));
		};
	}
}
