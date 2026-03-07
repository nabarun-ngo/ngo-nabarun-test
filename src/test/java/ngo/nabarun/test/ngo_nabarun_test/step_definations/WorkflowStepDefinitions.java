package ngo.nabarun.test.ngo_nabarun_test.step_definations;

import java.util.List;

import io.cucumber.java.en.Then;
import ngo.nabarun.test.ngo_nabarun_test.helpers.DataProvider;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext;
import ngo.nabarun.test.ngo_nabarun_test.helpers.ScenarioContext.ContextKeys;
import ngo.nabarun.test.ngo_nabarun_test.models.db.UserDBModel;
import ngo.nabarun.test.ngo_nabarun_test.page_objects.WorkflowPageObjects;
import ngo.nabarun.test.ngo_nabarun_test.utilities.ControlActions;
import ngo.nabarun.test.ngo_nabarun_test.utils.DataUtils;

public class WorkflowStepDefinitions {

	private final ScenarioContext scenarioContext;
	// private final ControlActions controlActions;
	// private final WorkflowPageObjects workflowPageObjects;
	private final DataProvider dataProvider;

	public WorkflowStepDefinitions(ScenarioContext scenarioContext, ControlActions ca, DataProvider dp,
			WorkflowPageObjects workflowPageObjects) {
		this.scenarioContext = scenarioContext;
		// this.controlActions = ca;
		// this.workflowPageObjects = workflowPageObjects;
		this.dataProvider = dp;
	}

	@Then("I wait for Task assignment to be completed for {string} workflow up to {int} seconds")
	public void I_wait_for_Task_assignment_to_be_completed_for_workflow_up_to_seconds(String workflowId, int timeout)
			throws InterruptedException {
		String loginIdType = scenarioContext.get(ContextKeys.Login_Id_Type, String.class);
		String loginId = scenarioContext.get(ContextKeys.Login_Id, String.class);
		String email;
		if (loginIdType.equalsIgnoreCase("role")) {
			List<UserDBModel> users = dataProvider.getUsersByRole(loginId);
			email = users.stream().findFirst()
					.orElseThrow(() -> new RuntimeException("Unable to find users with role " + loginId)).getEmail();
		} else {
			email = DataUtils.resolveData(loginId, scenarioContext);
		}
		UserDBModel user = dataProvider.getUserByEmail(email);
		int count = 0;
		int waitingTime = 0;
		do {
			workflowId = DataUtils.resolveData(workflowId, scenarioContext);
			count = dataProvider.getAssignmentCountByWorkflowId(workflowId, user.getId());
			if (count > 0) {
				System.out.println("Workflow task assignment completed for: " + workflowId);
				break;
			}
			System.out.println("Waiting for task assignment for workflow " + workflowId + " for " + waitingTime
					+ " seconds.");
			Thread.sleep(5000);
			waitingTime += 5;
		} while (count == 0 && waitingTime <= timeout);
		if (count == 0) {
			throw new RuntimeException(
					"Wait for task assignment failed for workflow " + workflowId + " after " + timeout + " seconds.");
		}
	}
}
