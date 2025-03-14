Feature: Donation
  This feature will contain testcases for donation feature

  Background: 
    Given I have opened to Nabarun's web portal
    Then I click and hold on "More" link at "Home" page
    Then I click on "Login" link at "Home" page
    Then I switch to the new tab
    Then I login with "cashier@nabarun.com" user using Password option
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Donations" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "DONATION DASHBOARD" screen

  @Donation @Regression @smoke
  Scenario: Create and Update Guest Donation E2E
  	This scenario is covering creating and updating guest donation without attaching event
  	Updating status to PAID >> 
  
    When I click on "Guest Donations" text at "Donation" page
    And I wait for loading to complete
    Then the "Add Guest Donation" button should be displayed at "Donation" page
    Then I click on "Add Guest Donation" button at "Donation" page
    Then I map create donation accordion as "Create_Donation" accordion
    Then I enter "Souvik" on "Name" textbox at "Create_Donation" accordion
    Then I enter "Souvik@gmail.com" on "Email address" textbox at "Create_Donation" accordion
    Then I enter "+91 9123899870" on "Contact number" textbox at "Create_Donation" accordion
    Then I wait for 3 seconds
    Then I select "One Time" on "Donation type" dropdown at "Create_Donation" accordion
    Then I enter "150" on "Donation amount" textbox at "Create_Donation" accordion
    Then I click "No" on "Is this donation made for any events?" radio at "Create_Donation" accordion
    Then I click on "Create" button at "Create_Donation" accordion
    And I wait for loading to complete
    Then I capture and store the donation id
    Then I wait for 5 seconds
    Then I search the created donation
    And I wait for loading to complete
    Then I opened the accordion at index 1
    # Updating Amount 
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I enter "200" on "Donation amount" textbox at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    # Updating Status to PAID
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Paid" on "Donation status" dropdown at "Donation" page
    And I wait for loading to complete
    Then I select "14/03/2025" on "Donation paid on" datepicker at "Donation" page 
    Then I select "Cashier TestUser" on "Donation paid to" dropdown at "Donation" page
    Then I select "UPI" on "Payment method" dropdown at "Donation" page
    Then I select "Google Pay" on "UPI name" dropdown at "Donation" page
    Then I enter "Test Test" on "Remarks" textarea at "Donation" page
    Then I upload "file:///C:/Users/Souvik/Downloads/PS_PPBS_75.pdf" on "Upload document(s)" fileinput at "Donation" page
    Then I wait for 5 seconds
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    
    
    
    
    
   
    
    
    
    
    
    
    
    
    Then I wait for 10 seconds
    Then I click on "Back to Dashboard" link at "Donation" page
    Then I logout from current session
