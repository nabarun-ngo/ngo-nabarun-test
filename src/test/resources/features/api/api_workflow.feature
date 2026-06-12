Feature: API Tests for Workflow and Task Approval

  @api @api_regression @api_workflow @api_workflow01
  Scenario Outline: API_Workflow Create, Task Inspect, and Task Approval
    Given I login with "<LoginUser>" user using API
    ## 1. Get logged-in user ID (used as requestedFor)
    Then I send a GET request to "/users/profile/me"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "MyUserId"
    ## 2. Load workflow reference data (types)
    Then I send a GET request to "/workflows/static/referenceData"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 3. Load additional field descriptors for JOIN_REQUEST workflow type
    Then I send a GET request to "/workflows/static/additionalFields?type=JOIN_REQUEST"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 4. Create a JOIN_REQUEST workflow
    Then I send a POST request to "/workflows/create" with payload:
      """
      {
        "type": "JOIN_REQUEST",
        "requestedFor": "{MyUserId}",
        "forExternalUser": false,
        "data": {
          "reason": "Joining as new member via automation test",
          "referredBy": ""
        }
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "WorkflowId"
    And The API response should have the following attributes
      | Attribute            | Value        |
      | status               | SUCCESS      |
      | responsePayload.type | JOIN_REQUEST |
    ## 5. Verify workflow appears in "by me" list
    Then I send a GET request to "/workflows/instances/byMe?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 6. Fetch workflow instance and extract current task ID
    Then I send a GET request to "/workflows/{WorkflowId}/instance"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.currentTask.id" and store it as "TaskId"
    And The API response should have the following attributes
      | Attribute            | Value        |
      | responsePayload.type | JOIN_REQUEST |
    ## 7. Get tasks assigned to me
    Then I send a GET request to "/workflows/tasks/forMe?completed=false&page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 8. Update the current task to IN_PROGRESS
    Then I send a PUT request to "/workflows/{WorkflowId}/tasks/{TaskId}/update" with payload:
      """
      {
        "status": "IN_PROGRESS",
        "remarks": "Reviewing the request — automation test",
        "resultData": {}
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 9. Process the task (advance workflow state machine)
    Then I send a POST request to "/workflows/{WorkflowId}/tasks/{TaskId}/processTask" with payload:
      """
      {
        "action": "APPROVE",
        "remarks": "Approved by automation test"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | president@nabarun.com |

  @api @api_regression @api_workflow @api_workflow02
  Scenario Outline: API_Workflow Cancellation
    Given I login with "<LoginUser>" user using API
    ## 1. Get logged-in user ID
    Then I send a GET request to "/users/profile/me"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "MyUserId"
    ## 2. Create a JOIN_REQUEST workflow to be cancelled
    Then I send a POST request to "/workflows/create" with payload:
      """
      {
        "type": "JOIN_REQUEST",
        "requestedFor": "{MyUserId}",
        "forExternalUser": false,
        "data": {
          "reason": "Cancellation test — automation",
          "referredBy": ""
        }
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "WorkflowToCancelId"
    ## 3. Cancel the workflow
    Then I send a POST request to "/workflows/{WorkflowToCancelId}/cancel" with payload:
      """
      {
        "reason": "Cancelled by automation test"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 4. Verify workflow status is CANCELLED
    Then I send a GET request to "/workflows/{WorkflowToCancelId}/instance"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute              | Value     |
      | responsePayload.status | CANCELLED |

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | president@nabarun.com |

  @api @api_regression @api_workflow @api_workflow03
  Scenario Outline: API_Task Reassignment
    Given I login with "<LoginUser>" user using API
    ## 1. Get logged-in user ID
    Then I send a GET request to "/users/profile/me"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "MyUserId"
    ## 2. Find another user to reassign to
    Then I send a GET request to "/users?page=0&size=10"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.content[0].id" and store it as "OtherUserId"
    ## 3. Create a workflow
    Then I send a POST request to "/workflows/create" with payload:
      """
      {
        "type": "JOIN_REQUEST",
        "requestedFor": "{MyUserId}",
        "forExternalUser": false,
        "data": {
          "reason": "Task reassignment test — automation",
          "referredBy": ""
        }
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "ReassignWorkflowId"
    ## 4. Get the current task ID
    Then I send a GET request to "/workflows/{ReassignWorkflowId}/instance"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.currentTask.id" and store it as "ReassignTaskId"
    ## 5. Reassign the task to another user
    Then I send a POST request to "/workflows/{ReassignWorkflowId}/tasks/{ReassignTaskId}/reassign?assigneeId={OtherUserId}" with payload:
      """
      {}
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 6. Verify assignee updated on the workflow instance
    Then I send a GET request to "/workflows/{ReassignWorkflowId}/instance"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | president@nabarun.com |
