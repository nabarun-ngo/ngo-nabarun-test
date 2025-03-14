package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class DonationPageObjects extends CommonPageObjects{

	public DonationPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}
	
	public Supplier<WebElement> DonationCreateAccordion = () -> driver.findElement(By.id("createDonation"));
	public Supplier<WebElement> DonationCreateAlert = () -> driver.findElement(By.xpath("//app-alert//*[@id='description']"));
	public Supplier<WebElement> ADVSearch_DonationId = ()-> driver.findElement(By.id("donationId"));

	
	

}
