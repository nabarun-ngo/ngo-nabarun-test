Feature: Finance - Accounts
  E2E scenarios for account list and navigation.
  FE routes: /secured/finance/accounts, /secured/finance/account/:id/transactions.

  Background:
    Given I have opened to Nabarun's web portal
    Then I click on "Login" link at "Home" page and wait for new window to load
    Then I login with "cashier@nabarun.com" user using Password option
    Then I handle all conditional post login screen if it appeared
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen

  @account @smoke @regression
  Scenario: Navigate to Accounts from dashboard (AC01)
    When I click on "Accounts" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "ACCOUNTS" screen
    Then I perform advance search with the following fields
      | Field_Name   | Field_Type | Field_Value       |
      | Account Name | Textbox    | Test Account 1234 |

  @account @regression
  Scenario: Open account list and see list (AC02)
    When I click on "Accounts" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "ACCOUNTS" screen
    Then the "Add Icon" button should be displayed at "Account" page

  @account @regression
  Scenario: Create new account (AC03)
    When I click on "Accounts" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "ACCOUNTS" screen
    Then the "Add Icon" button should be displayed at "Account" page
    Then I click on "Add Icon" button at "Account" page
    And I wait for loading to complete
    Then I click on "Back to Dashboard" link at "Account" page
