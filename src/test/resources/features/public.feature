Feature: Public facing pages
  This fearure covers all scenarios for public facing pages

  Background:
    Given I have opened to Nabarun's public portal

  @regression @smoke @public01
  Scenario: Public - Create and fullfill Join Request - Happy Path
    ## Create Join Request from public portal
    Then I click on "Join Us" link at "Home" page
    Then I store "{RandomFirstName}" value as "NewUserFirstName" variable
    Then I store "{RandomLastName}" value as "NewUserLastName" variable
    Then I store "{RandomEmail}" value as "NewUserEmail" variable
    Then I enter "{NewUserFirstName}" on "Your First Name" textbox at "Home" page
    Then I enter "{NewUserLastName}" on "Your Last Name" textbox at "Home" page
    Then I enter "{NewUserEmail}" on "Your Email (JoinUs)" textbox at "Home" page
    Then I enter "9{RandomNumber:9}" on "Your Mobile Number (JoinUs)" textbox at "Home" page
    Then I enter "{RandomLocation}" on "Where are you from?" textbox at "Home" page
    Then I enter "From my Friend" on "How did you hear about us?" textarea at "Home" page
    Then I click on "I agree with the Rules and Regulations of Nabarun" text at "Home" page
    Then I click on "Join Now" button at "Home" page and collect "id" from response of "/signup" and store as "JoinRequestId"
    Then I wait for following text to display at "Home" page
      | Expected_Content                                                        |
      | Thank you for showing interest! Please check your email for next steps. |
    Then I click on "Login" link at "Home" page and wait for new window to load
    ## Approval of 'Join Request' workflow by Group Coordinator
    Then I login with "groupcoordinator@nabarun.com" user using Password option
    Then I handle all conditional post login screen if needed
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Tasks" text at "Dashboard" page
    Then I must be landed to "My Tasks" screen
    Then I wait for Task assignment to be completed for "{JoinRequestId}" workflow up to 90 seconds
    Then I click on "Advanced Search" button at "Tasks" page
    Then I perform advance search with the following fields
      | Field_Name  | Field_Type | Field_Action | Field_Value     |
      | Workflow ID | textbox    | enter        | {JoinRequestId} |
    Then The accordions should have exactly 2 rows
    Then I open the 1st accordion
    Then I click "Accept" button in the opened accordion
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name                                                                                   | Field_Type | Field_Action | Field_Value  |
      | Task Status                                                                                  | dropdown   | select       | Completed    |
      | Remarks                                                                                      | textarea   | enter        | Task is done |
      | Is there any data correction needed? (if needed, update the remarks field with correct data) | dropdown   | select       | No           |
    Then I click "Confirm" button in the opened accordion
    Then I open the 1st accordion
    Then I click "Accept" button in the opened accordion
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name                                            | Field_Type | Field_Action | Field_Value  |
      | Task Status                                           | dropdown   | select       | Completed    |
      | Remarks                                               | textarea   | enter        | Task is done |
      | Did requester agree to Nabarun’s Rules & Regulations? | dropdown   | select       | Yes          |
    Then I click "Confirm" button in the opened accordion
    Then The accordions should have exactly 0 rows
    Then I click on "Completed Tasks" text at "Tasks" page
    Then I perform advance search with the following fields
      | Field_Name  | Field_Type | Field_Action | Field_Value     |
      | Workflow ID | textbox    | enter        | {JoinRequestId} |
    Then The accordions should have exactly 2 rows
    Then I logout from current session
    ## Approval of 'Join Request' workflow by President
    Then I login with "president@nabarun.com" user using Password option
    Then I handle all conditional post login screen if needed
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    Then I click on "Tasks" text at "Dashboard" page
    Then I must be landed to "My Tasks" screen
    Then I wait for Task assignment to be completed for "{JoinRequestId}" workflow up to 90 seconds
    Then I click on "Advanced Search" button at "Tasks" page
    Then I perform advance search with the following fields
      | Field_Name  | Field_Type | Field_Action | Field_Value     |
      | Workflow ID | textbox    | enter        | {JoinRequestId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "Accept" button in the opened accordion
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name                                       | Field_Type | Field_Action | Field_Value  |
      | Task Status                                      | dropdown   | select       | Completed    |
      | Remarks                                          | textarea   | enter        | Task is done |
      | Do you approve the onboarding of the new member? | dropdown   | select       | Approve      |
    Then I click "Confirm" button in the opened accordion
    Then The accordions should have exactly 0 rows
    Then I click on "Completed Tasks" text at "Tasks" page
    Then I perform advance search with the following fields
      | Field_Name  | Field_Type | Field_Action | Field_Value     |
      | Workflow ID | textbox    | enter        | {JoinRequestId} |
    Then The accordions should have exactly 1 rows
    Then I logout from current session
    ## Verification of 'Join Request' workflow completion
    ## New User Login, Email Verification and Password Change
    Then I change "{NewUserEmail}" user's password to default password
    Then I login with "{NewUserEmail}" user using Password option
    Then I should see text "Check your email" at "Login" page
    Then I should see text "Please enter the 4-digit verification code that was sent to {NewUserEmail}. The code is valid for 10 minutes." at "Login" page
    Then I change "{NewUserEmail}" user's email as verified
    Then I have opened to Nabarun's internal portal
    Then I click on "Login with Password" button at "Login" page
    Then I handle change password screen
    ##### Temp arrangement to avoid issue with invalid state error
    #Then I have opened to Nabarun's internal portal
    #Then I login with "{NewUserEmail}" user using Password option
    ####
    Then I handle user consent screen if needed
    Then I wait for 5 seconds
    Then I refresh the current page
    Then I wait for 5 seconds
    Then I login with "{NewUserEmail}" user using Password option
    Then I must be landed to "COMPLETE PROFILE" screen
    Then I fillup the "Complete Profile" form with the following fields at "Profile" page
      | Field_Name                                | Field_Type | Field_Action | Field_Value           |
      | Title                                     | dropdown   | select       | Mr                    |
      | Middle Name                               | textbox    | enter        | Kumar                 |
      | Gender                                    | dropdown   | select       | Male                  |
      | Date of Birth                             | datepicker | select       |            27/02/1995 |
      | Permanent Address same as Present Address | checkbox   | click        |                       |
      | Address Line 1                            | textbox    | enter        | {RandomStreetAddress} |
      | Address Line 2                            | textbox    | enter        | {RandomStreetAddress} |
      | Address Line 3                            | textbox    | enter        | {RandomStreetAddress} |
      | Hometown                                  | textbox    | enter        | {RandomCity}          |
      | Country                                   | dropdown   | select       | India                 |
      | State                                     | dropdown   | select       | West Bengal           |
      | District                                  | dropdown   | select       | North 24 Parganas     |
      | Pin code                                  | textbox    | enter        |                700110 |
    Then I click on "Update" button at "Profile" page
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    Then I click on "Requests" text at "Dashboard" page
    Then I must be landed to "Requests" screen
    Then I click on "Add Icon" button at "Requests" page
    ## Create Exit Request from internal portal
    Then I fill the following fields in the create accordion
      | Field_Name          | Field_Type | Field_Action | Field_Value     |
      | Request Type        | dropdown   | select       | Exit Request    |
      | Reason for Exit     | textarea   | enter        | For XYZ reason. |
      | Suggestion/Comments | textarea   | enter        | No suggestions. |
    Then I click "Create" button in the create accordion and collect "responsePayload.id" from response of "workflows/create" and store as "WorkflowId"
    Then I click on "Advanced Search" button at "Requests" page
    Then I perform advance search with the following fields
      | Field_Name | Field_Type | Field_Action | Field_Value  |
      | Request ID | textbox    | enter        | {WorkflowId} |
    Then The accordions should have exactly 1 rows
    Then I logout from current session
    ## Approval of 'Exit Request' workflow by Group Coordinator
    Then I login with "groupcoordinator@nabarun.com" user using Password option
    Then I handle all conditional post login screen if needed
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    Then I click on "Tasks" text at "Dashboard" page
    Then I must be landed to "My Tasks" screen
    Then I wait for Task assignment to be completed for "{WorkflowId}" workflow up to 90 seconds
    Then I click on "Advanced Search" button at "Tasks" page
    Then I perform advance search with the following fields
      | Field_Name  | Field_Type | Field_Action | Field_Value  |
      | Workflow ID | textbox    | enter        | {WorkflowId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "Accept" button in the opened accordion
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name                                      | Field_Type | Field_Action | Field_Value  |
      | Task Status                                     | dropdown   | select       | Completed    |
      | Remarks                                         | textarea   | enter        | Task is done |
      | Should we proceed with the termination process? | dropdown   | select       | Yes          |
      | Exit Discussion Notes                           | textarea   | enter        | ok to exit   |
    Then I click "Confirm" button in the opened accordion
    Then The accordions should have exactly 0 rows
    Then I logout from current session
    ## Approval of 'Exit Request' workflow by Cashier
    Then I login with "cashier@nabarun.com" user using Password option
    Then I handle all conditional post login screen if needed
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    Then I click on "Tasks" text at "Dashboard" page
    Then I must be landed to "My Tasks" screen
    Then I wait for Task assignment to be completed for "{WorkflowId}" workflow up to 90 seconds
    Then I click on "Advanced Search" button at "Tasks" page
    Then I perform advance search with the following fields
      | Field_Name  | Field_Type | Field_Action | Field_Value  |
      | Workflow ID | textbox    | enter        | {WorkflowId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "Accept" button in the opened accordion
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name                              | Field_Type | Field_Action | Field_Value  |
      | Task Status                             | dropdown   | select       | Completed    |
      | Remarks                                 | textarea   | enter        | Task is done |
      | Has financial clearance been completed? | dropdown   | select       | Yes          |
      | Financial Clearance Notes               | textarea   | enter        | cleared      |
    Then I click "Confirm" button in the opened accordion
    Then The accordions should have exactly 0 rows
    Then I logout from current session
    ## Approval of 'Exit Request' workflow by President
    Then I login with "president@nabarun.com" user using Password option
    Then I handle all conditional post login screen if needed
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    Then I click on "Members" text at "Dashboard" page
    Then I must be landed to "MEMBERS" screen
    Then I click on "Advanced Search" button at "Members" page
    Then I perform advance search with the following fields
      | Field_Name | Field_Type | Field_Action | Field_Value    |
      | Email      | textbox    | enter        | {NewUserEmail} |
    Then I should see 1 member profile with the following details
      | Name                                 | Role   |
      | {NewUserFirstName} {NewUserLastName} | Member |
    Then I wait for 5 seconds
    Then I click on "Back to Dashboard" text at "Members" page
    Then I click on "Tasks" text at "Dashboard" page
    Then I must be landed to "My Tasks" screen
    Then I wait for Task assignment to be completed for "{WorkflowId}" workflow up to 90 seconds
    Then I click on "Advanced Search" button at "Tasks" page
    Then I perform advance search with the following fields
      | Field_Name  | Field_Type | Field_Action | Field_Value  |
      | Workflow ID | textbox    | enter        | {WorkflowId} |
    Then The accordions should have exactly 1 rows
    Then I open the 1st accordion
    Then I click "Accept" button in the opened accordion
    Then I open the 1st accordion
    Then I click "Update" button in the opened accordion
    Then I fill the following fields in the opened accordion
      | Field_Name                   | Field_Type | Field_Action | Field_Value  |
      | Task Status                  | dropdown   | select       | Completed    |
      | Remarks                      | textarea   | enter        | Task is done |
      | Approve Termination Request? | dropdown   | select       | Approve      |
      | Final Remarks                | textarea   | enter        | ok approved  |
    Then I click "Confirm" button in the opened accordion
    Then The accordions should have exactly 0 rows
    Then I click on "Back to Dashboard" text at "Tasks" page
    ## Verification existing member is removed from list
    Then I click on "Members" text at "Dashboard" page
    Then I must be landed to "MEMBERS" screen
    Then I wait for 60 seconds
    Then I click on "Advanced Search" button at "Members" page
    Then I perform advance search with the following fields
      | Field_Name | Field_Type | Field_Action | Field_Value    |
      | Email      | textbox    | enter        | {NewUserEmail} |
    Then I wait for "No member found." text to be visible at "Members" page
    Then I wait for 5 seconds
    Then I logout from current session
