package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class AccountsPageObjects extends CommonPageObjects{

	public Supplier<WebElement> ADVSearch_RequestId = () -> driver.findElement(By.id("requestId"));

	public AccountsPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

}
