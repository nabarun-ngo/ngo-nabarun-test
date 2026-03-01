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
    Then I enter "{RandomText:5}" on "How did you hear about us?" textarea at "Home" page
    Then I click on "I agree with the Rules and Regulations of Nabarun" text at "Home" page
    Then I click on "Join Now" button at "Home" page
    Then I wait for following text to display at "Home" page
      | Expected_Content                                                        |
      | Thank you for showing interest! Please check your email for next steps. |
