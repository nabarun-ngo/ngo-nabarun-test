Feature: Public facing pages
  This fearure covers all scenarios for public facing pages

  Background:
    Given I have opened to Nabarun's web portal

  @regression @smoke @public01
  Scenario Outline: Public - Create and fullfill Join Request - Happy Path
    Then I click on "Join Us" link at "Home" page
    Then I enter "{RandomFirstName}" on "Your First Name" textbox at "Home" page
    Then I enter "{RandomLastName}" on "Your Last Name" textbox at "Home" page
    Then I enter "{RandomEmail}" on "Your Email (JoinUs)" textbox at "Home" page
    Then I enter "{RandomNumber:10}" on "Your Mobile Number (JoinUs)" textbox at "Home" page
    Then I enter "{RandomLocation}" on "Where are you from?" textbox at "Home" page
    Then I enter "From my Friend" on "How did you hear about us?" textarea at "Home" page
    Then I click on "I agree with the Rules and Regulations of Nabarun" text at "Home" page
    Then I click on "Join Now" button at "Home" page and collect "id" from response of "/signup" and store as "JoinRequestId"
    Then I wait for following text to display at "Home" page
      | Expected_Content                                                        |
      | Thank you for showing interest! Please check your email for next steps. |
    Then I click on "Login" link at "Home" page and wait for new window to load
    Then I login with "groupcoordinator@nabarun.com" user using Password option
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Tasks" text at "Dashboard" page
    Then I must be landed to "My Tasks" screen
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
    Then I login with "president@nabarun.com" user using Password option
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    Then I click on "Tasks" text at "Dashboard" page
    Then I must be landed to "My Tasks" screen
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
    Then I wait for 10 seconds
