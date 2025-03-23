Feature: Requests & Worklist
  This fearure covers all scenarios if request procession workflows

  @regression @smoke @workflow01
  Scenario: Workflow - Create and fullfill Join Request - Happy Path
    Given I have opened to Nabarun's web portal
    Then I click on "Join Us" link at "Home" page
    Then I enter "{RandomFirstName}" on "Your First Name" textbox at "Home" page
    Then I enter "{RandomLastName}" on "Your Last Name" textbox at "Home" page
    Then I enter "{RandomEmail}" on "Your Email (JoinUs)" textbox at "Home" page
    Then I enter "{RandomNumber:10}" on "Your Mobile Number (JoinUs)" textbox at "Home" page
    Then I enter "{RandomLocation}" on "Where are you from?" textbox at "Home" page
    Then I enter "{RandomText}" on "How did you know about Nabarun?" textarea at "Home" page
    Then I enter "{RandomText}" on "Why you want to join Nabarun?" textarea at "Home" page
    Then I click on "Join Now" button at "Home" page
    Then I wait for following text to display at "Home" page
      | Expected_Content      |
      | Rules and Regulations |
    Then I click on "Agree and Continue" button at "Home" page
    Then I wait for following text to display at "Home" page
      | Expected_Content |
      | Login Details    |
    Then I scroll on "Login Password" textbox at "Home" page
    Then I enter "Password@01" on "Login Password" textbox at "Home" page
    Then I scroll on "Confirm Login Password" textbox at "Home" page
    Then I enter "Password@01" on "Confirm Login Password" textbox at "Home" page
    Then I click on "Proceed" button at "Home" page
    Then I wait for following text to display at "Home" page
      | Expected_Content  |
      | Verify and Submit |
    Then I retrieve the OTP from database and enter it on OTP textbox
    Then I click on "Submit" button at "Home" page
    Then I wait for following text to display at "Home" page
      | Expected_Content  |
      | Request Submitted |
    Then I capture and store the request id
    Then I wait for 5 seconds
    Given I have opened to Nabarun's web portal
    Then I click and hold on "More" link at "Home" page
    Then I click on "Login" link at "Home" page
    Then I switch to the new tab
    # Login With President
    Then I login with "president@nabarun.com" user using Password option
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Tasks" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "My Tasks" screen
    Then I search the created request under "Pending Tasks" tab
    And I wait for loading to complete
    Then I opened the accordion of index 1 at "Tasks" page
    Then I click on "Update" button at "Tasks" page
    Then I select "APPROVE" on "Decision" dropdown at "Tasks" page
    Then I enter "Ok Approved" on "Remarks" textarea at "Tasks" page
    Then I click on "Confirm" button at "Tasks" page
    And I wait for loading to complete
    Then I wait for 2 seconds
    Then I click on "Back to Dashboard" link at "Tasks" page
    Then I logout from current session
    # Login With President
    Then I login with "secretary@nabarun.com" user using Password option
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Tasks" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "My Tasks" screen
    Then I search the created request under "Pending Works" tab
    And I wait for loading to complete
    Then I opened the accordion of index 1 at "Tasks" page
    Then I click on "Update" button at "Tasks" page
    Then I select "APPROVE" on "Decision" dropdown at "Tasks" page
    Then I enter "Ok Approved" on "Remarks" textarea at "Tasks" page
    Then I click on "Confirm" button at "Tasks" page
    And I wait for loading to complete
    Then I wait for 2 seconds
    Then I click on "Back to Dashboard" link at "Tasks" page
    Then I logout from current session
    # Login With New User 1st time
    Then I login with "{NewUserEmail}" user using Password option
    And I check if user consent screen appeared or not
    Then I must be landed to "COMPLETE PROFILE" screen
    Then I select "Mr" on "Title" dropdown at "Profile" page
    Then I select "Male" on "Gender" dropdown at "Profile" page
    Then I select "24/12/2017" on "Date of Birth" datepicker at "Profile" page
    Then I enter "9123899870" on "Phone Number (WhatsApp)" textbox at "Profile" page
    Then I enter "{RandomText}" on "Descrive something about you" textarea at "Profile" page
    Then I click on "Permanent Address same as Present Address" text at "Profile" page
    And I wait for loading to complete
    Then I enter "{RandomLocation}" on "Address Line 1" textbox at "Profile" page
    Then I enter "{RandomLocation}" on "Address Line 2" textbox at "Profile" page
    Then I enter "{RandomLocation}" on "Address Line 3" textbox at "Profile" page
    Then I enter "{RandomLocation}" on "Hometown" textbox at "Profile" page
    Then I select "India" on "Country" dropdown at "Profile" page
    And I wait for loading to complete
    Then I select "West Bengal" on "State" dropdown at "Profile" page
    And I wait for loading to complete
    Then I select "North 24 Parganas" on "District" dropdown at "Profile" page
    Then I click on "Update" button at "Profile" page
    And I wait for loading to complete
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    Then I logout from current session
    # Login With New User 2nd time
    Then I login with "{NewUserEmail}" user using Password option
    And I check if user consent screen appeared or not
    Then I must be landed to "WELCOME TO NABARUN'S SECURED DASHBOARD" screen
    When I click on "Tasks" text at "Dashboard" page
    And I wait for loading to complete
    Then I must be landed to "My Tasks" screen
    Then I search the created request under "Pending Tasks" tab
    And I wait for loading to complete
    Then I wait for following text to display at "Tasks" page
      | Expected_Content  |
      | No records found. |
    Then I logout from current session
