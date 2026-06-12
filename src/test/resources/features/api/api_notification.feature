Feature: API Tests for Notification Management

  @api @api_regression @api_notification @api_notification01
  Scenario Outline: API_Bulk Notification Send to Member
    Given I login with "<LoginUser>" user using API
    ## 1. Get at least one user to use as notification recipient
    Then I send a GET request to "/users?page=0&size=100"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.content[0].id" and store it as "RecipientId"
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 2. Send a bulk push notification to the recipient
    Then I send a POST request to "/notifications/bulk" with payload:
      """
      {
        "title": "Automation Test Announcement",
        "message": "This is a test notification sent by the automation suite.",
        "recipientIds": ["{RecipientId}"],
        "channel": "PUSH"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | president@nabarun.com |

  @api @api_regression @api_notification @api_notification02
  Scenario Outline: API_FCM Token Lifecycle — List and Admin View
    Given I login with "<LoginUser>" user using API
    ## 1. List all registered FCM tokens (admin view)
    Then I send a GET request to "/notifications/fcm/list?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | president@nabarun.com |
