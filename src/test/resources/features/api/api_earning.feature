Feature: API Tests for Earning Management

  @api @api_regression @api_earning @api_earning01
  Scenario Outline: API_Earning Create, Update, and Database Verify
    Given I login with "<LoginUser>" user using API
    ## 1. Load earning reference data
    Then I send a GET request to "/earning/static/referenceData"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 2. Baseline earning count
    Then I send a GET request to "/earning/list?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 3. Create a new earning record
    Then I store "{RandomName} Fund" value as "EarningSource" variable
    Then I send a POST request to "/earning/create" with payload:
      """
      {
        "amount": 5000,
        "category": "GRANT",
        "source": "{EarningSource}",
        "currency": "INR",
        "description": "Test earning created by automation"
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "EarningId"
    And The API response should have the following attributes
      | Attribute                | Value   |
      | status                   | SUCCESS |
      | responsePayload.amount   | 5000    |
      | responsePayload.category | GRANT   |
    ## 4. Fetch the earning by ID
    Then I send a GET request to "/earning/{EarningId}"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                | Value       |
      | responsePayload.id       | {EarningId} |
      | responsePayload.category | GRANT       |
    ## 5. Update earning amount and description
    Then I send a PUT request to "/earning/{EarningId}/update" with payload:
      """
      {
        "description": "Updated by automation test",
        "amount": 5500
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute              | Value   |
      | status                 | SUCCESS |
      | responsePayload.amount | 5500    |
    ## 6. Verify updated earning appears in the list
    Then I send a GET request to "/earning/list?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 7. DB — confirm updated values persisted in the earnings table
    Then The database table "earnings" record with id "{EarningId}" should have:
      | Field       | Value                      |
      | amount      | 5500                       |
      | category    | GRANT                      |
      | description | Updated by automation test |
    ## 8. DB — confirm at least one earning row exists
    Then The database table "earnings" should contain at least 1 records

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | treasurer@nabarun.com |
