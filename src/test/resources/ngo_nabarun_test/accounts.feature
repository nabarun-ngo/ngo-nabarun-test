Feature: Account Management

  Background:
    Given I have opened to Nabarun's web portal
    Then I click and hold on "More" link at "Home" page
    Then I click on "Login" link at "Home" page and wait for new window to load

  @accounts
  @regression
  @smoke
 Scenario Outline: Create & Update New Account, View transactions_Treasurer
    Then I login with "<User_Email>" user using Password option
    Then I handle all conditional post login screen if it appeared
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Accounts" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "Accounts" screen
    When I click on "Manage Accounts" text at "Accounts" page
    Then the "Add Icon" button should be displayed at "Accounts" page
    When I click on "Add Icon" button at "Accounts" page
    Then I map "#create" element as "Create_Account" accordion
    Then I select "Treasure" on "Account Type" dropdown at "Create_Account" accordion
    Then I enter "Treasurer TestUser" on "Account Holder" textbox at "Create_Account" accordion
    Then I enter "500" on "Opening Balance" textbox at "Create_Account" accordion
    Then I click on "Confirm" button at "Create_Account" accordion


   @accounts01
   Examples:
   | User_Email            |
   | treasurer@nabarun.com |