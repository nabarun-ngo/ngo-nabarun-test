Feature: Accounts
  This feature contains end-to-end test cases for the Account functionality, covering account management, account management, account creeatiing, deleting, and updating., event attachments, and status updates.

  Background:
    Given I have opened to Nabarun's web portal
    Then I click and hold on "More" link at "Home" page
    Then I click on "Login" link at "Home" page
    Then I switch to the new tab
    
    
 Scenario Outline: Create and Update Account
    Then I login with "treasurer@nabarun.com" user using Password option
    Then I handle all conditional post login screen if it appeared
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Accounts & Finance" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "ACCOUNTS & FINANCE" screen
    Then I click on "Manage Accounts & Finance" button at "ACCOUNTS & FINANCE" screen
    Then I must be landed to "CREATE ACCOUNT" screen
    When I fill in the following fields:
      | accountName     | <accountName>     |
      | accountType     | <accountType>     |
      | accountNumber   | <accountNumber>   |
      | accountBalance  | <accountBalance>  |
    Examples:
	    | accountName | accountType | accountNumber | accountBalance |
        | Test Account 1 | Savings     | 1234567890    | 1000           |
        | Test Account 2 | Checking    | 0987654321    | 2000           |
    
