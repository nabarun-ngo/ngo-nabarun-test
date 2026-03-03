package ngo.nabarun.test.ngo_nabarun_test.page_objects;

import java.util.function.Supplier;

import com.microsoft.playwright.Locator;

import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.utilities.AngularMaterial;

public class WorkflowPageObjects extends CommonPageObjects {

	public Supplier<Locator> ADVSearch_RequestId = () -> findLocator("#requestId");

	public WorkflowPageObjects(ScenarioContext scenarioContext) {
		super(scenarioContext);
	}

	@Override
	public Locator getButtonMapping(String elementName, Locator parent) {
		switch (elementName.toLowerCase()) {
			case "add icon (for others)" -> {
				Locator otherTab = super.scope(parent).locator("xpath=//app-delegated-requests-tab");
				return AngularMaterial.MatIcon(otherTab, "add");
			}
			default -> {
				return super.getButtonMapping(elementName, parent);
			}
		}
	}

}
