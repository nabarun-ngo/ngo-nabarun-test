package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class ProfilePageObjects extends CommonPageObjects {

	public ProfilePageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}
	
	public Supplier<WebElement> presentAddress = () -> driver.findElement(By.xpath("(//*[normalize-space(text())='Present Address']/following-sibling::*)[1]"));
	public Supplier<WebElement> permanentAddress = () -> driver.findElement(By.xpath("(//*[normalize-space(text())='Permanent Address']/following-sibling::*)[1]"));
}
