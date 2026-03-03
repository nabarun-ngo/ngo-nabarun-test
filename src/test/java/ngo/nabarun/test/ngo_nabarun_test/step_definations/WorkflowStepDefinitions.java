package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.WorkflowPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;

public class WorkflowStepDefinitions {

	private final ScenarioContext scenarioContext;
	private final ControlActions controlActions;
	private final WorkflowPageObjects workflowPageObjects;

	public WorkflowStepDefinitions(ScenarioContext scenarioContext, ControlActions ca, DataProvider dp,
			WorkflowPageObjects workflowPageObjects) {
		this.scenarioContext = scenarioContext;
		this.controlActions = ca;
		this.workflowPageObjects = workflowPageObjects;
	}

	@Then("^I search the created request under \"([^\"]*)\" tab$")
	public void iSearchTheCreatedRequestUnderTab(String tab) throws Throwable {
		String requestId = scenarioContext.get(ContextKeys.RequestId, String.class);
		controlActions.scrollToTop();
		controlActions.click(workflowPageObjects.getButtonMapping("Advanced Search", null));
		workflowPageObjects.ADVSearch_RequestId.get().fill(requestId);
		controlActions.click(workflowPageObjects.getButtonMapping("Search", null));
	}
}
