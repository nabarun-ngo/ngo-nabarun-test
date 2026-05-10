Feature: API Tests for Donation Endpoints

  @api @donation_api
  Scenario: Create a ONETIME donation via API successfully
    Given I set the base API URL to "http://localhost:8080/api"
    When I send a POST request to "/donation/create" with payload:
      """
      {
        "type": "ONETIME",
        "amount": 500,
        "forEventId": "EVT123",
        "donorId": "DONOR123"
      }
      """
    Then The API response status code should be 201
    And The API response should contain a valid donation ID

  @api @donation_api
  Scenario: Create a REGULAR donation via API successfully
    Given I set the base API URL to "http://localhost:8080/api"
    When I send a POST request to "/donation/create" with payload:
      """
      {
        "type": "REGULAR",
        "amount": 1000,
        "startDate": "2026-06-01T00:00:00.000Z",
        "endDate": "2027-06-01T00:00:00.000Z"
      }
      """
    Then The API response status code should be 201
    And The API response should contain a valid donation ID

  @api @donation_api
  Scenario: Validation fails when amount is missing
    Given I set the base API URL to "http://localhost:8080/api"
    When I send a POST request to "/donation/create" with payload:
      """
      {
        "type": "ONETIME",
        "forEventId": "EVT123",
        "donorId": "DONOR123"
      }
      """
    Then The API response status code should be 400
    And The API response should contain error message "amount must not be empty"

  @api @donation_api
  Scenario: Validation fails when type is missing
    Given I set the base API URL to "http://localhost:8080/api"
    When I send a POST request to "/donation/create" with payload:
      """
      {
        "amount": 500,
        "forEventId": "EVT123",
        "donorId": "DONOR123"
      }
      """
    Then The API response status code should be 400
    And The API response should contain error message "type must not be empty"
