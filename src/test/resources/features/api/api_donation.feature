Feature: API Tests for Donation

  @api @api_donation @api_donation001
  Scenario: API_Create and Update Guest Donation (No Event, UPI Payment)
    Given I login with "cashier@nabarun.com" user using API
    Then I store "{RandomName}" value as "DonorName" variable
    Then I store "{RandomEmail}" value as "DonorEmail" variable
    Then I store "9{RandomNumber:9}" value as "DonorPhone" variable
    Then I store "{RandomInt:100,999}" value as "DonationAmount" variable
    Then I send a POST request to "/donation/create/guest" with payload:
      """
      {
        "amount": "{DonationAmount}",
        "forEventId": null,
        "donorEmail": "{DonorEmail}",
        "donorName": "{DonorName}",
        "donorNumber": "{DonorPhone}"
      }
      """
    Then The API response status code should be 201
    And The API response should contain a valid donation ID
