package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import com.microsoft.playwright.Locator;


import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;

public class WorkflowPageObjects extends CommonPageObjects{

	public Supplier<Locator> ADVSearch_RequestId = () -> findLocator("#requestId");

	public WorkflowPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

}
