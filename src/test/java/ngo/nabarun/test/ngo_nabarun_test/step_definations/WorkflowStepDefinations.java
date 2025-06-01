package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.WorkflowPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ElementHelper;

public class WorkflowStepDefinations {

	private ScenarioContext scenarioContext;
	private ElementHelper elementHelper;
	private WorkflowPageObjects workflowPageObjects;

	public WorkflowStepDefinations(ScenarioContext scenarioContext,ElementHelper elementHelper,WorkflowPageObjects workflowPageObjects) {
		this.scenarioContext=scenarioContext;
		this.elementHelper=elementHelper;
		this.workflowPageObjects=workflowPageObjects;
	}

	@Then("^I search the created request under \"([^\"]*)\" tab$")
	public void iSearchTheCreatedRequestUnderTab(String tab) throws Throwable {
		String requestId = scenarioContext.get(ContextKeys.RequestId, String.class);
		elementHelper.scrollToTop();
		elementHelper.click(workflowPageObjects.getButtonMapping("Advanced Search", null));
		workflowPageObjects.ADVSearch_RequestId.get().sendKeys(requestId);
		elementHelper.click(workflowPageObjects.getButtonMapping("Search", null));
	}
}
