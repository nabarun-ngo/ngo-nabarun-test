package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class DonationPageObjects extends CommonPageObjects {

	public DonationPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	public Supplier<WebElement> DonationCreateAccordion = () -> driver.findElement(By.id("createDonation"));
	public Supplier<WebElement> DonationCreateAlert = () -> driver
			.findElement(By.xpath("//app-alert//*[@id='description']"));
	public Supplier<WebElement> ADVSearch_DonationId = () -> driver.findElement(By.id("donationId"));
	public Supplier<WebElement> ADVSearch_FirstName = () -> driver.findElement(By.id("firstName"));
	public Supplier<WebElement> ADVSearch_LastName = () -> driver.findElement(By.id("lastName"));
	public Supplier<WebElement> Accordion_AddIcon = () -> driver.findElement(By.xpath("//mat-icon[text()='add']"));

	@Override
	public WebElement getButtonMapping(String elementName, SearchContext parentContext) {
		return switch (elementName) {
		case "Add Icon" -> Accordion_AddIcon.get();
		default -> super.getButtonMapping(elementName, parentContext);
		};
	}

}
