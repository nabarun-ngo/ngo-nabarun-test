Feature: API Tests for Project and Activity Management

  @api @api_regression @api_project @api_project01
  Scenario Outline: API_Project Create, Activity Add, Status Update, and Database Verify
    Given I login with "<LoginUser>" user using API
    ## 1. Get logged-in user ID (used as managerId and organizerId)
    Then I send a GET request to "/users/profile/me"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "ManagerUserId"
    ## 2. Load project reference data
    Then I send a GET request to "/projects/static/referenceData"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 3. List projects (baseline)
    Then I send a GET request to "/projects?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 4. Create a new project
    Then I store "{RandomName} Project" value as "ProjectName" variable
    Then I store "PRJ-{RandomInt:1000,9999}" value as "ProjectCode" variable
    Then I send a POST request to "/projects/create" with payload:
      """
      {
        "name": "{ProjectName}",
        "code": "{ProjectCode}",
        "description": "Automation test project",
        "category": "EDUCATION",
        "budget": 500000,
        "currency": "INR",
        "startDate": "2026-01-01",
        "endDate": "2026-12-31",
        "managerId": "{ManagerUserId}",
        "status": "ACTIVE",
        "phase": "EXECUTION",
        "location": "Kolkata, WB",
        "targetBeneficiaryCount": 200,
        "tags": ["education", "automation-test"]
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "ProjectId"
    And The API response should have the following attributes
      | Attribute                | Value     |
      | status                   | SUCCESS   |
      | responsePayload.category | EDUCATION |
      | responsePayload.status   | ACTIVE    |
    ## 5. Fetch project by ID
    Then I send a GET request to "/projects/{ProjectId}"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute          | Value       |
      | responsePayload.id | {ProjectId} |
    ## 6. Create an activity under the project
    Then I store "{RandomName} Workshop" value as "ActivityName" variable
    Then I send a POST request to "/projects/{ProjectId}/activity" with payload:
      """
      {
        "name": "{ActivityName}",
        "type": "WORKSHOP",
        "scale": "EVENT",
        "priority": "HIGH",
        "description": "Automation test activity",
        "startDate": "2026-03-15",
        "endDate": "2026-03-15",
        "location": "Community Hall",
        "expectedParticipants": 50,
        "estimatedCost": 10000,
        "currency": "INR",
        "organizerId": "{ManagerUserId}",
        "tags": ["workshop", "automation-test"]
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "ActivityId"
    And The API response should have the following attributes
      | Attribute                | Value    |
      | status                   | SUCCESS  |
      | responsePayload.type     | WORKSHOP |
      | responsePayload.priority | HIGH     |
    ## 7. Verify activity appears in the project's activity list
    Then I send a GET request to "/projects/{ProjectId}/activities?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                     | Value        |
      | status                        | SUCCESS      |
      | responsePayload.content[0].id | {ActivityId} |
    ## 8. Update the activity's expected participants
    Then I send a PUT request to "/projects/{ProjectId}/activity/{ActivityId}" with payload:
      """
      {
        "description": "Updated activity description",
        "expectedParticipants": 75
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 9. Update the project status to ON_HOLD
    Then I send a PUT request to "/projects/{ProjectId}/update" with payload:
      """
      {
        "status": "ON_HOLD",
        "phase": "MONITORING",
        "description": "Project placed on hold during automation test"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 10. DB — confirm project status and phase persisted
    Then The database table "projects" record with id "{ProjectId}" should have:
      | Field  | Value      |
      | status | ON_HOLD    |
      | phase  | MONITORING |
    ## 11. DB — confirm activity expected participants updated
    Then The database table "activities" record with id "{ActivityId}" should have:
      | Field                | Value |
      | expectedParticipants | 75    |

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | president@nabarun.com |
