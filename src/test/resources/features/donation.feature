Feature: Donation Management
  This feature contains end-to-end test cases for the donation functionality.
  It covers guest and member donations, event/project attachments, payment methods (UPI, Cash, Net Banking),
  and the complete status lifecycle including reversals and cancellations.

  Background:
    Given I have opened to Nabarun's internal portal

  @donation @regression @smoke @donation01
  Scenario: Create and Update Guest Donation (No Event, UPI Payment)
    ## 1. Authentication & Navigation to Donations
    Then I login with "cashier@nabarun.com" user using Password option
    Then I handle all conditional post login screen if needed
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Donations" text at "Dashboard" page
    Then I must be landed to "DONATIONS" screen
    ## 2. Initiate Guest Donation Creation
    When I click on "Guest Donations" text at "Donation" page
    Then I click on "Add Icon" button at "Donation" page
    Then I store "{RandomName}" value as "DonorName" variable
    Then I store "{RandomEmail}" value as "DonorEmail" variable
    Then I store "9{RandomNumber:9}" value as "DonorPhone" variable
    Then I store "{RandomNumber:3}" value as "DonationAmount" variable
    Then I fill the following fields in the create accordion
      | Field_Name                            | Field_Type | Field_Action | Field_Value      |
      | Donor name                            | textbox    | enter        | {DonorName}      |
      | Donor email                           | textbox    | enter        | {DonorEmail}     |
      | Phone Number                          | textbox    | enter        | {DonorPhone}     |
      | Donation amount                       | textbox    | enter        | {DonationAmount} |
      | Is this any project related Donation? | radio      | select       | No               |
    Then I click "Confirm" button in the create accordion and collect "responsePayload.id" from response of "donation/create" and store as "DonationId"
    ## 3. Advanced Search to Locate the Created Donation
    Then I click on "Advanced Search" button at "Donation" page
    Then I perform advance search with the following fields
      | Field_Name      | Field_Type | Field_Action | Field_Value  |
      | Donation Number | textbox    | enter        | {DonationId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    ## 4. Verify Initial Donation Details (Status: Raised)
    Then The open accordion should have following values
      | Section_Name     | Field_Name         | Field_Value               |
      | Donor Details    | Donor name         | {DonorName}               |
      | Donor Details    | Donor email        | {DonorEmail}              |
      | Donor Details    | Phone Number       |          +91-{DonorPhone} |
      | Donation Details | Donation number    | {DonationId}              |
      | Donation Details | Donation type      | One Time                  |
      | Donation Details | Donation amount    | ₹ {DonationAmount}        |
      | Donation Details | Donation status    | Raised                    |
      | Donation Details | Donation raised on | {SystemDate [dd/MM/yyyy]} |
    ## 5. Update Donation Amount
    Then I click "Update" button in the opened accordion
    Then I store "{RandomNumber:3}" value as "DonationAmount" variable
    Then I fill the following fields in the opened accordion
      | Field_Name      | Field_Type | Field_Action | Field_Value      |
      | Donation amount | textbox    | enter        | {DonationAmount} |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    ## 6. Update Donation Status to 'Paid' (UPI Payment)
    Then I click "Update" button in the opened accordion
    Then I store "{SystemDate [dd/MM/yyyy] -2}" value as "PaidOn" variable
    Then I fill the following fields in the opened accordion
      | Field_Name       | Field_Type | Field_Action | Field_Value             |
      | Donation status  | dropdown   | select       | Paid                    |
      | Donation paid on | datepicker | select       | {PaidOn}                |
      | Donation paid to | dropdown   | select       | Treasurer TestUser      |
      | Payment method   | dropdown   | select       | UPI                     |
      | UPI name         | dropdown   | select       | Google Pay              |
      | Remarks          | textarea   | enter        | Test Test               |
      | Upload           | fileinput  | upload       | test_files/test_pdf.pdf |
    Then I click "Confirm" button in the opened accordion and collect "responsePayload.transactionRef" from response of "donation/{DonationId}/update" and store as "TransactionRef"
    ## 7. Verify Updated Donation & Transaction Reference
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name            | Field_Value               |
      | Donation Details | Donation status       | Paid                      |
      | Donation Details | Payment method        | UPI                       |
      | Donation Details | UPI name              | Google Pay                |
      | Donation Details | Donation paid on      | {PaidOn}                  |
      | Donation Details | Donation confirmed on | {SystemDate [dd/MM/yyyy]} |
      | Transaction Ref  | Transaction Ref       | {TransactionRef}          |
      | Donation Details | Remarks               | Test Test                 |
    Then I click on "Back to Dashboard" link at "Donation" page
    ## 8. Financial Verification - Navigate to Accounts
    When I click on "Accounts" text at "Dashboard" page
    Then I must be landed to "Accounts" screen
    Then I click on "Manage Accounts" text at "Accounts" page
    Then I click on "Advanced Search" button at "Accounts" page
    ## 9. Search for Transaction in Main Account
    Then I perform advance search with the following fields
      | Field_Name   | Field_Type | Field_Action | Field_Value             |
      | Account Type | dropdown   | select       | Main Account (Treasure) |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "View Transactions" button in the opened accordion
    Then I must be landed to "Transactions" screen
    Then I click on "Advanced Search" button at "Transactions" page
    ## 10. Final Verification of Inward Transaction (Status: SUCCESS)
    Then I perform advance search with the following fields
      | Field_Name               | Field_Type | Field_Action | Field_Value      |
      | Transaction Reference Id | textbox    | enter        | {TransactionRef} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name       | Field_Name                   | Field_Value        |
      | Transaction Detail | Transaction Reference Number | {TransactionRef}   |
      | Transaction Detail | Transaction Type             | IN                 |
      | Transaction Detail | Transaction Amount           | ₹ {DonationAmount} |
      | Transaction Detail | Transaction Date             | {PaidOn}           |
      | Transaction Detail | Transaction Status           | SUCCESS            |
      | Transaction Detail | Transaction Ref Id           | {DonationId}       |
      | Transaction Detail | Transaction Ref Type         | DONATION           |
    Then I click on "Back to Accounts" link at "Transactions" page
    Then I click on "Back to Dashboard" link at "Accounts" page
    Then I logout from current session

  @donation @regression @donation02
  Scenario: Create and Update Guest Donation (With Event, Cash Payment, Status Transitions)
    ## 1. Authentication & Navigation to Donations
    Then I login with "cashier@nabarun.com" user using Password option
    Then I handle all conditional post login screen if needed
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Donations" text at "Dashboard" page
    Then I must be landed to "DONATIONS" screen
    ## 2. Initiate Guest Donation Creation Linked to Project Event
    When I click on "Guest Donations" text at "Donation" page
    Then I click on "Add Icon" button at "Donation" page
    Then I store "{SystemDate [dd/MM/yyyy]}" value as "PaidOn" variable
    Then I store "{RandomName}" value as "DonorName" variable
    Then I store "{RandomNumber:3}" value as "DonationAmount" variable
    Then I fill the following fields in the create accordion
      | Field_Name                            | Field_Type | Field_Action | Field_Value      |
      | Donor name                            | textbox    | enter        | {DonorName}      |
      | Donation amount                       | textbox    | enter        | {DonationAmount} |
      | Is this any project related Donation? | radio      | select       | Yes              |
    Then I enter "Test Project" on "Project" autocomplete at "Donation" page
    Then I enter "Test Activity" on "Activity" autocomplete at "Donation" page
    Then I click on "Select" button at "Donation" page
    Then I wait for "This is donation will be recorded for 'Test Activity' activity under 'Test Project' project" text to be visible at "Donation" page
    Then I click "Confirm" button in the create accordion and collect "responsePayload.id" from response of "donation/create" and store as "DonationId"
    ## 3. Search and Initial Verification
    Then I click on "Advanced Search" button at "Donation" page
    Then I perform advance search with the following fields
      | Field_Name      | Field_Type | Field_Action | Field_Value  |
      | Donation Number | textbox    | enter        | {DonationId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name         | Field_Value               |
      | Donor Details    | Donor name         | {DonorName}               |
      | Donor Details    | Donor email        | -                         |
      | Donor Details    | Phone Number       | -                         |
      | Donation Details | Donation number    | {DonationId}              |
      | Donation Details | Donation type      | One Time                  |
      | Donation Details | Donation amount    | ₹ {DonationAmount}        |
      | Donation Details | Donation status    | Raised                    |
      | Donation Details | Donation raised on | {SystemDate [dd/MM/yyyy]} |
    ## 4. Update Status to 'Paid' (Cash Payment)
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name       | Field_Type | Field_Action | Field_Value        |
      | Donation status  | dropdown   | select       | Paid               |
      | Donation paid on | datepicker | select       | {PaidOn}           |
      | Donation paid to | dropdown   | select       | Treasurer TestUser |
      | Payment method   | dropdown   | select       | Cash               |
      | Remarks          | textarea   | enter        | Cash donation Test |
    Then I click "Confirm" button in the opened accordion and collect "responsePayload.transactionRef" from response of "donation/{DonationId}/update" and store as "TransactionRef"
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name            | Field_Value               |
      | Donation Details | Donation status       | Paid                      |
      | Donation Details | Payment method        | Cash                      |
      | Donation Details | Donation paid on      | {PaidOn}                  |
      | Donation Details | Donation confirmed on | {SystemDate [dd/MM/yyyy]} |
      | Transaction Ref  | Transaction Ref       | {TransactionRef}          |
      | Donation Details | Remarks               | Cash donation Test        |
    Then I click on "Back to Dashboard" link at "Donation" page
    ## 5. Verify Inward Transaction in Main Account
    When I click on "Accounts" text at "Dashboard" page
    Then I must be landed to "Accounts" screen
    Then I click on "Manage Accounts" text at "Accounts" page
    Then I click on "Advanced Search" button at "Accounts" page
    Then I perform advance search with the following fields
      | Field_Name   | Field_Type | Field_Action | Field_Value             |
      | Account Type | dropdown   | select       | Main Account (Treasure) |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "View Transactions" button in the opened accordion
    Then I must be landed to "Transactions" screen
    Then I click on "Advanced Search" button at "Transactions" page
    Then I perform advance search with the following fields
      | Field_Name               | Field_Type | Field_Action | Field_Value      |
      | Transaction Reference Id | textbox    | enter        | {TransactionRef} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name       | Field_Name                   | Field_Value        |
      | Transaction Detail | Transaction Reference Number | {TransactionRef}   |
      | Transaction Detail | Transaction Type             | IN                 |
      | Transaction Detail | Transaction Amount           | ₹ {DonationAmount} |
      | Transaction Detail | Transaction Date             | {PaidOn}           |
      | Transaction Detail | Transaction Status           | SUCCESS            |
      | Transaction Detail | Transaction Ref Id           | {DonationId}       |
      | Transaction Detail | Transaction Ref Type         | DONATION           |
    Then I click on "Back to Accounts" link at "Transactions" page
    Then I click on "Back to Dashboard" link at "Accounts" page
    ## 6. Re-navigate to Donations and Perform 'Wrong Payment Update'
    When I click on "Donations" text at "Dashboard" page
    Then I must be landed to "DONATIONS" screen
    When I click on "Guest Donations" text at "Donation" page
    Then I click on "Advanced Search" button at "Donation" page
    Then I perform advance search with the following fields
      | Field_Name      | Field_Type | Field_Action | Field_Value  |
      | Donation Number | textbox    | enter        | {DonationId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name      | Field_Type | Field_Action | Field_Value          |
      | Donation status | dropdown   | select       | Wrong Payment Update |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value          |
      | Donation Details | Donation status | Wrong Payment Update |
    Then I click on "Back to Dashboard" link at "Donation" page
    ## 7. Verify Transaction Reversal (In/Out) in Accounts
    When I click on "Accounts" text at "Dashboard" page
    Then I must be landed to "Accounts" screen
    Then I click on "Manage Accounts" text at "Accounts" page
    Then I click on "Advanced Search" button at "Accounts" page
    Then I perform advance search with the following fields
      | Field_Name   | Field_Type | Field_Action | Field_Value             |
      | Account Type | dropdown   | select       | Main Account (Treasure) |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "View Transactions" button in the opened accordion
    Then I must be landed to "Transactions" screen
    Then I click on "Advanced Search" button at "Transactions" page
    Then I perform advance search with the following fields
      | Field_Name               | Field_Type | Field_Action | Field_Value      |
      | Transaction Reference Id | textbox    | enter        | {TransactionRef} |
    Then The accordions should have exactly 2 rows
    # Check Reversal (OUT transaction)
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name       | Field_Name                   | Field_Value               |
      | Transaction Detail | Transaction Reference Number | {TransactionRef}          |
      | Transaction Detail | Transaction Type             | OUT                       |
      | Transaction Detail | Transaction Amount           | ₹ {DonationAmount}        |
      | Transaction Detail | Transaction Date             | {SystemDate [dd/MM/yyyy]} |
      | Transaction Detail | Transaction Status           | SUCCESS                   |
      | Transaction Detail | Transaction Ref Type         | TXN_REVERSE               |
    Then I close the currently opened accordion
    # Check Original (Now RESERSED status)
    Then I open the 2nd accordion
    Then The open accordion should have following values
      | Section_Name       | Field_Name                   | Field_Value      |
      | Transaction Detail | Transaction Reference Number | {TransactionRef} |
      | Transaction Detail | Transaction Type             | IN               |
      | Transaction Detail | Transaction Status           | REVERSED         |
    Then I click on "Back to Accounts" link at "Transactions" page
    Then I click on "Back to Dashboard" link at "Accounts" page
    ## 8. Transition Status back to 'Pending'
    When I click on "Donations" text at "Dashboard" page
    Then I must be landed to "DONATIONS" screen
    When I click on "Guest Donations" text at "Donation" page
    Then I click on "Advanced Search" button at "Donation" page
    Then I perform advance search with the following fields
      | Field_Name      | Field_Type | Field_Action | Field_Value  |
      | Donation Number | textbox    | enter        | {DonationId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name      | Field_Type | Field_Action | Field_Value |
      | Donation status | dropdown   | select       | Pending     |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value |
      | Donation Details | Donation status | Pending     |
    ## 9. Final Status Transition to 'Cancelled'
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name        | Field_Type | Field_Action | Field_Value |
      | Donation status   | dropdown   | select       | Cancelled   |
      | Reason for cancel | textarea   | enter        | Test Test   |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value |
      | Donation Details | Donation status | Cancelled   |
    Then I logout from current session

  @donation @regression @donation03
  Scenario: Create and Update Member Onetime Donation (With Event, Net Banking)
    ## 1. Authentication & Navigation to Member Donations
    Then I login with "cashier@nabarun.com" user using Password option
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Donations" text at "Dashboard" page
    Then I must be landed to "DONATIONS" screen
    When I click on "Member Donations" text at "Donation" page
    Then I wait for "Select Member" text to be visible at "Donation" page
    #Then I refresh the current page
    #Then I wait for "Select Member" text to be visible at "Donation" page
    ## 2. Select Member and Initiate Onetime Donation
    Then I enter "Member TestUser" on "Member Search" autocomplete at "Donation" page
    Then I click on "Select" button at "Donation" page
    Then I click on "Add Icon" button at "Donation" page
    Then I store "{RandomNumber:3}" value as "DonationAmount" variable
    Then I fill the following fields in the create accordion
      | Field_Name                            | Field_Type | Field_Action | Field_Value      |
      | Donation type                         | dropdown   | select       | One Time         |
      | Donation amount                       | textbox    | enter        | {DonationAmount} |
      | Is this any project related Donation? | radio      | select       | Yes              |
    Then I enter "Test Project" on "Project" autocomplete at "Donation" page
    Then I enter "Test Activity" on "Activity" autocomplete at "Donation" page
    Then I click on "Select" button at "Donation" page
    Then I wait for "This is donation will be recorded for 'Test Activity' activity under 'Test Project' project" text to be visible at "Donation" page
    Then I click "Confirm" button in the create accordion and collect "responsePayload.id" from response of "donation/create" and store as "DonationId"
    ## 3. Verify and Update Status to 'Paid' (Net Banking)
    Then I click on "Advanced Search" button at "Donation" page
    Then I perform advance search with the following fields
      | Field_Name      | Field_Type | Field_Action | Field_Value  |
      | Donation Number | textbox    | enter        | {DonationId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name         | Field_Value               |
      | Donation Details | Donation number    | {DonationId}              |
      | Donation Details | Donation type      | One Time                  |
      | Donation Details | Donation amount    | ₹ {DonationAmount}        |
      | Donation Details | Donation status    | Raised                    |
      | Donation Details | Donation raised on | {SystemDate [dd/MM/yyyy]} |
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name       | Field_Type | Field_Action | Field_Value             |
      | Donation status  | dropdown   | select       | Paid                    |
      | Donation paid on | datepicker | select       | {SystemDate}            |
      | Donation paid to | dropdown   | select       | Treasurer TestUser      |
      | Payment method   | dropdown   | select       | Net Banking             |
      | Remarks          | textarea   | enter        | Net Banking test        |
      | Upload           | fileinput  | upload       | test_files/test_pdf.pdf |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name       | Field_Value               |
      | Donation Details | Donation number  | {DonationId}              |
      | Donation Details | Donation type    | One Time                  |
      | Donation Details | Donation amount  | ₹ {DonationAmount}        |
      | Donation Details | Donation status  | Paid                      |
      | Donation Details | Donation paid on | {SystemDate [dd/MM/yyyy]} |
      | Donation Details | Payment method   | Net Banking               |
      | Donation Details | Remarks          | Net Banking test          |
    Then I logout from current session

  @donation @donation04 @regression
  Scenario: Create and Update Member Regular Donation (Full Status Flow, UPI)
    ## 1. Authentication & Pre-Cleanup
    Then I login with "cashier@nabarun.com" user using Password option
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Donations" text at "Dashboard" page
    Then I must be landed to "DONATIONS" screen
    # Ensure no duplicate regular donation exists for the test month
    Then I check and delete regular donation raised for "Member TestUser" this month
    ## 2. Initiate Member Regular Donation for Current Month
    When I click on "Member Donations" text at "Donation" page
    Then I enter "Member TestUser" on "Member Search" autocomplete at "Donation" page
    Then I click on "Select" button at "Donation" page
    Then I click on "Add Icon" button at "Donation" page
    Then I store "{RandomNumber:3}" value as "DonationAmount" variable
    Then I store "{FirstOfCurrentMonth}" value as "DonationStartDate" variable
    Then I store "{LastOfCurrentMonth}" value as "DonationEndDate" variable
    Then I fill the following fields in the create accordion
      | Field_Name          | Field_Type | Field_Action | Field_Value         |
      | Donation type       | dropdown   | select       | Regular             |
      | Donation amount     | textbox    | enter        | {DonationAmount}    |
      | Donation start date | datepicker | select       | {DonationStartDate} |
      | Donation end date   | datepicker | select       | {DonationEndDate}   |
    Then I click "Confirm" button in the create accordion and collect "responsePayload.id" from response of "donation/create" and store as "DonationId"
    ## 3. Transition through Various Statuses (Pending -> Payment Failed)
    Then I click on "Advanced Search" button at "Donation" page
    Then I perform advance search with the following fields
      | Field_Name      | Field_Type | Field_Action | Field_Value  |
      | Donation Number | textbox    | enter        | {DonationId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name          | Field_Value               |
      | Donation Details | Donation number     | {DonationId}              |
      | Donation Details | Donation type       | Regular                   |
      | Donation Details | Donation amount     | ₹ {DonationAmount}        |
      | Donation Details | Donation status     | Raised                    |
      | Donation Details | Donation raised on  | {SystemDate [dd/MM/yyyy]} |
      | Donation Details | Donation start date | {DonationStartDate}       |
      | Donation Details | Donation end date   | {DonationEndDate}         |
    # Status: Pending
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name      | Field_Type | Field_Action | Field_Value |
      | Donation status | dropdown   | select       | Pending     |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value |
      | Donation Details | Donation status | Pending     |
    # Status: Payment Failed
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name              | Field_Type | Field_Action | Field_Value    |
      | Donation status         | dropdown   | select       | Payment Failed |
      | Payment failure details | textarea   | enter        | Demo failed    |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value    |
      | Donation Details | Donation status | Payment Failed |
    ## 4. Update Status to 'Paid' (UPI Payment)
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name       | Field_Type | Field_Action | Field_Value             |
      | Donation status  | dropdown   | select       | Paid                    |
      | Donation paid on | datepicker | select       | {SystemDate}            |
      | Donation paid to | dropdown   | select       | Treasurer TestUser      |
      | Payment method   | dropdown   | select       | UPI                     |
      | UPI name         | dropdown   | select       | PhonePe                 |
      | Remarks          | textarea   | enter        | Test Test               |
      | Upload           | fileinput  | upload       | test_files/test_pdf.pdf |
    Then I click "Confirm" button in the opened accordion and collect "responsePayload.transactionRef" from response of "donation/{DonationId}/update" and store as "TransactionRef"
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name            | Field_Value               |
      | Donation Details | Donation status       | Paid                      |
      | Donation Details | Payment method        | UPI                       |
      | Donation Details | UPI name              | PhonePe                   |
      | Donation Details | Donation paid on      | {SystemDate [dd/MM/yyyy]} |
      | Donation Details | Donation confirmed on | {SystemDate [dd/MM/yyyy]} |
      | Transaction Ref  | Transaction Ref       | {TransactionRef}          |
      | Donation Details | Remarks               | Test Test                 |
    Then I click on "Back to Dashboard" link at "Donation" page
    ## 5. Verify Inward Transaction in Main Account
    When I click on "Accounts" text at "Dashboard" page
    Then I must be landed to "Accounts" screen
    Then I click on "Manage Accounts" text at "Accounts" page
    Then I click on "Advanced Search" button at "Accounts" page
    Then I perform advance search with the following fields
      | Field_Name   | Field_Type | Field_Action | Field_Value             |
      | Account Type | dropdown   | select       | Main Account (Treasure) |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "View Transactions" button in the opened accordion
    Then I must be landed to "Transactions" screen
    Then I click on "Advanced Search" button at "Transactions" page
    Then I perform advance search with the following fields
      | Field_Name               | Field_Type | Field_Action | Field_Value      |
      | Transaction Reference Id | textbox    | enter        | {TransactionRef} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name       | Field_Name                   | Field_Value               |
      | Transaction Detail | Transaction Reference Number | {TransactionRef}          |
      | Transaction Detail | Transaction Type             | IN                        |
      | Transaction Detail | Transaction Amount           | ₹ {DonationAmount}        |
      | Transaction Detail | Transaction Date             | {SystemDate [dd/MM/yyyy]} |
      | Transaction Detail | Transaction Status           | SUCCESS                   |
      | Transaction Detail | Transaction Ref Id           | {DonationId}              |
      | Transaction Detail | Transaction Ref Type         | DONATION                  |
    Then I click on "Back to Accounts" link at "Transactions" page
    Then I click on "Back to Dashboard" link at "Accounts" page
    ## 6. Re-navigate to Donations and Perform 'Wrong Payment Update'
    When I click on "Donations" text at "Dashboard" page
    Then I must be landed to "DONATIONS" screen
    When I click on "Member Donations" text at "Donation" page
    Then I enter "Member TestUser" on "Member Search" autocomplete at "Donation" page
    Then I click on "Select" button at "Donation" page
    Then I click on "Advanced Search" button at "Donation" page
    Then I perform advance search with the following fields
      | Field_Name      | Field_Type | Field_Action | Field_Value  |
      | Donation Number | textbox    | enter        | {DonationId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    ## 5. Status Rollback Flow (Wrong Payment Update -> Raised)
    # Status: Wrong Payment Update
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name      | Field_Type | Field_Action | Field_Value          |
      | Donation status | dropdown   | select       | Wrong Payment Update |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value          |
      | Donation Details | Donation status | Wrong Payment Update |
    Then I click on "Back to Dashboard" link at "Donation" page
    ## 7. Verify Transaction Reversal (In/Out) in Accounts
    When I click on "Accounts" text at "Dashboard" page
    Then I must be landed to "Accounts" screen
    Then I click on "Manage Accounts" text at "Accounts" page
    Then I click on "Advanced Search" button at "Accounts" page
    Then I perform advance search with the following fields
      | Field_Name   | Field_Type | Field_Action | Field_Value             |
      | Account Type | dropdown   | select       | Main Account (Treasure) |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "View Transactions" button in the opened accordion
    Then I must be landed to "Transactions" screen
    Then I click on "Advanced Search" button at "Transactions" page
    Then I perform advance search with the following fields
      | Field_Name               | Field_Type | Field_Action | Field_Value      |
      | Transaction Reference Id | textbox    | enter        | {TransactionRef} |
    Then The accordions should have exactly 2 rows
    # Check Reversal (OUT transaction)
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name       | Field_Name                   | Field_Value               |
      | Transaction Detail | Transaction Reference Number | {TransactionRef}          |
      | Transaction Detail | Transaction Type             | OUT                       |
      | Transaction Detail | Transaction Amount           | ₹ {DonationAmount}        |
      | Transaction Detail | Transaction Date             | {SystemDate [dd/MM/yyyy]} |
      | Transaction Detail | Transaction Status           | SUCCESS                   |
      | Transaction Detail | Transaction Ref Type         | TXN_REVERSE               |
    Then I close the currently opened accordion
    # Check Original (Now REVERSED status)
    Then I open the 2nd accordion
    Then The open accordion should have following values
      | Section_Name       | Field_Name                   | Field_Value      |
      | Transaction Detail | Transaction Reference Number | {TransactionRef} |
      | Transaction Detail | Transaction Type             | IN               |
      | Transaction Detail | Transaction Status           | REVERSED         |
    Then I click on "Back to Accounts" link at "Transactions" page
    Then I click on "Back to Dashboard" link at "Accounts" page
    ## 8. Transition Status back to 'Pending'
    When I click on "Donations" text at "Dashboard" page
    Then I must be landed to "DONATIONS" screen
    When I click on "Member Donations" text at "Donation" page
    Then I enter "Member TestUser" on "Member Search" autocomplete at "Donation" page
    Then I click on "Select" button at "Donation" page
    Then I click on "Advanced Search" button at "Donation" page
    Then I perform advance search with the following fields
      | Field_Name      | Field_Type | Field_Action | Field_Value  |
      | Donation Number | textbox    | enter        | {DonationId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name      | Field_Type | Field_Action | Field_Value |
      | Donation status | dropdown   | select       | Pending     |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value |
      | Donation Details | Donation status | Pending     |
    ## 6. Transition to 'Pay Later' and finally 'Cancelled'
    # Status: Pay Later
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name              | Field_Type | Field_Action | Field_Value         |
      | Donation status         | dropdown   | select       | Pay Later           |
      | Reason for paying later | textarea   | enter        | Test Test Pay Later |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value |
      | Donation Details | Donation status | Pay Later   |
    # Status: Cancelled
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name        | Field_Type | Field_Action | Field_Value |
      | Donation status   | dropdown   | select       | Cancelled   |
      | Reason for cancel | textarea   | enter        | Test Test   |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then The open accordion should have following values
      | Section_Name     | Field_Name      | Field_Value |
      | Donation Details | Donation status | Cancelled   |
    Then I logout from current session
