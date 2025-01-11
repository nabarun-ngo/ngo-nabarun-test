Feature: Donation
  This feature will contain testcases for donation feature

  Background: 
    Given that I am on Nabarun "HOME" page
    And I click and hold on "More" button
    And I click on "Login" button
    Then I switch to the new tab
    And I login with "CASHIER" role using Password option
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Donations" button
    And I wait for loading to complete
    Then I must be landed to "DONATION DASHBOARD" screen

  @Donation @Regression @smoke
  Scenario Outline: Create and Update Guest Donation E2E
    When I click on "<name>" button

    Examples: 
      | name            | test              |
      | Guest Donations | test1sssasssasasa |
