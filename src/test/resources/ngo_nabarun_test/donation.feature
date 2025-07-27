Feature: Donation Management
  This feature contains end-to-end test cases for the donation functionality, covering guest and member donations, event attachments, and status updates.

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

  @donation @regression @smoke @donation01
  Scenario: Create and Update Guest Donation (No Event, UPI Payment)
    When I click on "Guest Donations" text at "Donation" page
    And I wait for loading to complete
    Then the "Add Guest Donation" button should be displayed at "Donation" page
    Then I click on "Add Guest Donation" button at "Donation" page
    Then I map "//*[@id='createDonation']" element as "Create_Donation" accordion
    Then I enter "{RandomName}" on "Name" textbox at "Create_Donation" accordion
    Then I enter "{RandomEmail}" on "Email address" textbox at "Create_Donation" accordion
    Then I enter "+91{RandomNumber:10}" on "Contact number" textbox at "Create_Donation" accordion
    Then I wait for 3 seconds
    Then I select "One Time" on "Donation type" dropdown at "Create_Donation" accordion
    Then I enter "{RandomNumber:3}" on "Donation amount" textbox at "Create_Donation" accordion
    Then I click "No" on "Is this donation made for any events?" radio at "Create_Donation" accordion
    Then I click on "Create" button at "Create_Donation" accordion
    And I wait for loading to complete
    Then I capture and store the donation id
    Then I wait for 5 seconds
    Then I search the created donation under "Guest Donation" tab
    And I wait for loading to complete
    Then I opened the accordion of index 1 at "Donation" page
    And I wait for loading to complete
    # Update Amount
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I enter "{RandomNumber:3}" on "Donation amount" textbox at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    # Update Status to PAID
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Paid" on "Donation status" dropdown at "Donation" page
    And I wait for loading to complete
    Then I select "{SystemDate}" on "Donation paid on" datepicker at "Donation" page
    Then I select "Cashier TestUser" on "Donation paid to" dropdown at "Donation" page
    Then I select "UPI" on "Payment method" dropdown at "Donation" page
    Then I select "Google Pay" on "UPI name" dropdown at "Donation" page
    Then I enter "Test Test" on "Remarks" textarea at "Donation" page
    Then I upload "test_files/test_pdf.pdf" on "Upload document(s)" fileinput at "Donation" page
    Then I wait for 2 seconds
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    Then I check if transaction is created for this donation
    And I wait for loading to complete
    Then I wait for 2 seconds
    Then I click on "Back to Dashboard" link at "Donation" page
    Then I logout from current session

  @donation @donation02 @regression
  Scenario: Create and Update Guest Donation (With Event, Cash Payment, Status Transitions)
    When I click on "Guest Donations" text at "Donation" page
    And I wait for loading to complete
    Then the "Add Guest Donation" button should be displayed at "Donation" page
    Then I click on "Add Guest Donation" button at "Donation" page
    Then I map "//*[@id='createDonation']" element as "Create_Donation" accordion
    Then I enter "{RandomName}" on "Name" textbox at "Create_Donation" accordion
    Then I enter "{RandomEmail}" on "Email address" textbox at "Create_Donation" accordion
    Then I enter "+91{RandomNumber:10}" on "Contact number" textbox at "Create_Donation" accordion
    Then I wait for 3 seconds
    Then I select "One Time" on "Donation type" dropdown at "Create_Donation" accordion
    Then I enter "{RandomNumber:3}" on "Donation amount" textbox at "Create_Donation" accordion
    Then I click "Yes" on "Is this donation made for any events?" radio at "Create_Donation" accordion
    And I wait for loading to complete
    Then I select "Test Event" on "Select event" dropdown at "Create_Donation" accordion
    Then I click on "Create" button at "Create_Donation" accordion
    And I wait for loading to complete
    Then I capture and store the donation id
    Then I wait for 5 seconds
    Then I search the created donation under "Guest Donation" tab
    And I wait for loading to complete
    Then I opened the accordion of index 1 at "Donation" page
    And I wait for loading to complete
    # Update Amount
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I enter "{RandomNumber:3}" on "Donation amount" textbox at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    # Update Status to PAID
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Paid" on "Donation status" dropdown at "Donation" page
    And I wait for loading to complete
    Then I select "{SystemDate}" on "Donation paid on" datepicker at "Donation" page
    Then I select "Cashier TestUser" on "Donation paid to" dropdown at "Donation" page
    Then I select "Cash" on "Payment method" dropdown at "Donation" page
    Then I enter "Test Test" on "Remarks" textarea at "Donation" page
    Then I wait for 2 seconds
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    Then I check if transaction is created for this donation
    # Status: Wrong Payment Update
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Wrong Payment Update" on "Donation status" dropdown at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    Then I check if transaction is reverted for this donation
    # Status: Raised
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Raised" on "Donation status" dropdown at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    # Status: Cancelled
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Cancelled" on "Donation status" dropdown at "Donation" page
    Then I enter "Test Test" on "Reason for cancel" textarea at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    Then I wait for 2 seconds
    Then I click on "Back to Dashboard" link at "Donation" page
    Then I logout from current session

  @donation @donation03 @regression
  Scenario: Create and Update Member Onetime Donation (With Event, Net Banking)
    When I click on "Member Donations" text at "Donation" page
    And I wait for loading to complete
    Then I search for member "Member TestUser" under "Member Donation" tab
    And I wait for loading to complete
    Then I opened the accordion of index 1 at "Donation" page
    And I wait for loading to complete
    Then I click on "Add Icon" button at "Donation" page
    Then I map "//*[@id='createDonation']" element as "Create_Donation" accordion
    Then I select "One Time" on "Donation type" dropdown at "Create_Donation" accordion
    Then I enter "{RandomNumber:3}" on "Donation amount" textbox at "Create_Donation" accordion
    Then I click "Yes" on "Is this donation made for any events?" radio at "Create_Donation" accordion
    And I wait for loading to complete
    Then I select "Test Event" on "Select event" dropdown at "Create_Donation" accordion
    Then I click on "Create" button at "Create_Donation" accordion
    And I wait for loading to complete
    Then I capture and store the donation id
    Then I wait for 5 seconds
    Then I search the created donation under "Member Donation" tab
    And I wait for loading to complete
    Then I map "(//mat-expansion-panel)[1]" element as "Member_Details" accordion
    Then I opened the accordion of index 1 at "Member_Details" accordion
    And I wait for loading to complete
    # Update Status to PAID
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Paid" on "Donation status" dropdown at "Donation" page
    And I wait for loading to complete
    Then I select "{SystemDate}" on "Donation paid on" datepicker at "Donation" page
    Then I select "Cashier TestUser" on "Donation paid to" dropdown at "Donation" page
    Then I select "Net Banking" on "Payment method" dropdown at "Donation" page
    Then I enter "Test Test" on "Remarks" textarea at "Donation" page
    Then I upload "test_files/test_pdf.pdf" on "Upload document(s)" fileinput at "Donation" page
    Then I wait for 2 seconds
    Then I wait for 2 seconds
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    Then I check if transaction is created for this donation
    Then I wait for 2 seconds
    Then I click on "Back to Dashboard" link at "Donation" page
    Then I logout from current session

  @donation @donation04 @regression
  Scenario: Create and Update Member Regular Donation (Full Status Flow, UPI)
    When I click on "Member Donations" text at "Donation" page
    And I wait for loading to complete
    Then I search for member "Member TestUser" under "Member Donation" tab
    And I wait for loading to complete
    Then I opened the accordion of index 1 at "Donation" page
    And I wait for loading to complete
    Then I check and delete regular donation raised for "Member TestUser" this month
    Then I click on "Add Icon" button at "Donation" page
    Then I map "//*[@id='createDonation']" element as "Create_Donation" accordion
    Then I select "Regular" on "Donation type" dropdown at "Create_Donation" accordion
    Then I enter "{RandomNumber:3}" on "Donation amount" textbox at "Create_Donation" accordion
    Then I select "{FirstOfCurrentMonth}" on "Donation start date" datepicker at "Donation" page
    Then I select "{LastOfCurrentMonth}" on "Donation end date" datepicker at "Donation" page
    Then I click on "Create" button at "Create_Donation" accordion
    And I wait for loading to complete
    Then I capture and store the donation id
    Then I wait for 5 seconds
    Then I search the created donation under "Member Donation" tab
    And I wait for loading to complete
    Then I map "(//mat-expansion-panel)[1]" element as "Member_Details" accordion
    Then I opened the accordion of index 1 at "Member_Details" accordion
    And I wait for loading to complete
    # Status: Pending
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Pending" on "Donation status" dropdown at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    # Status: Payment Failed
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Payment Failed" on "Donation status" dropdown at "Donation" page
    Then I enter "Demo failed" on "Payment failure details" textarea at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    # Status: Paid
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Paid" on "Donation status" dropdown at "Donation" page
    And I wait for loading to complete
    Then I select "{SystemDate}" on "Donation paid on" datepicker at "Donation" page
    Then I select "Cashier TestUser" on "Donation paid to" dropdown at "Donation" page
    Then I select "UPI" on "Payment method" dropdown at "Donation" page
    Then I select "PhonePe" on "UPI name" dropdown at "Donation" page
    Then I enter "Test Test" on "Remarks" textarea at "Donation" page
    Then I upload "test_files/test_pdf.pdf" on "Upload document(s)" fileinput at "Donation" page
    Then I wait for 2 seconds
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    Then I check if transaction is created for this donation
    # Status: Wrong Payment Update
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Wrong Payment Update" on "Donation status" dropdown at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    Then I check if transaction is reverted for this donation
    # Status: Raised
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Raised" on "Donation status" dropdown at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    # Status: Pay Later
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Pay Later" on "Donation status" dropdown at "Donation" page
    Then I enter "Test Test Pay Later" on "Reason for paying later" textarea at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    # Status: Cancelled
    Then I click on "Update" button at "Donation" page
    And I wait for loading to complete
    Then I select "Cancelled" on "Donation status" dropdown at "Donation" page
    Then I enter "Test Test" on "Reason for cancel" textarea at "Donation" page
    Then I click on "Confirm" button at "Donation" page
    And I wait for loading to complete
    Then I wait for 2 seconds
    Then I click on "Back to Dashboard" link at "Donation" page
    Then I logout from current session
    
    

    
    
    
    