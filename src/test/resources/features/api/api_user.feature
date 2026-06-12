Feature: API Tests for User Management

  @api @api_regression @api_user @api_user01
  Scenario Outline: API_Member Onboarding — Create, Update, Role Assign, Profile Verify
    Given I login with "<LoginUser>" user using API
    ## 1. Get current member list (baseline)
    Then I send a GET request to "/users?page=0&size=10"
    Then The API response status code should be 200
    ## 2. Load reference data (roles, statuses)
    Then I send a GET request to "/users/static/referenceData"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 3. Create new member
    Then I store "{RandomName}" value as "NewFirstName" variable
    Then I store "AutoTest" value as "NewLastName" variable
    Then I store "{RandomEmail}" value as "NewEmail" variable
    Then I store "9{RandomNumber:9}" value as "NewPhone" variable
    Then I send a POST request to "/users" with payload:
      """
      {
        "firstName": "{NewFirstName}",
        "lastName": "{NewLastName}",
        "email": "{NewEmail}",
        "isTemporary": false,
        "phoneNumber": {
          "code": "+91",
          "number": "{NewPhone}",
          "fullNumber": "+91{NewPhone}"
        }
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "NewUserId"
    And The API response should have the following attributes
      | Attribute             | Value      |
      | status                | SUCCESS    |
      | responsePayload.email | {NewEmail} |
    ## 4. Verify new member profile by ID
    Then I send a GET request to "/users/{NewUserId}"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute          | Value       |
      | responsePayload.id | {NewUserId} |
    ## 5. Update member profile (Admin)
    Then I send a PUT request to "/users/{NewUserId}" with payload:
      """
      {
        "firstName": "{NewFirstName}",
        "lastName": "Updated",
        "about": "Test member created by automation",
        "gender": "MALE",
        "isPublicProfile": true
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 6. Assign MEMBER role to new user
    Then I send a POST request to "/users/{NewUserId}/assign-role" with payload:
      """
      {
        "roleCode": "MEMBER",
        "roleName": "Member",
        "description": "Assigned by automation test"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 7. DB — verify user row exists in user_profiles
    Then The database table "user_profiles" record with id "{NewUserId}" should have:
      | Field | Value      |
      | email | {NewEmail} |

    Examples:
      | LoginUser                  |
      | cashier@nabarun.com        |
      | president@nabarun.com      |

  @api @api_regression @api_user @api_user02
  Scenario Outline: API_Self Profile Update and Metrics
    Given I login with "<LoginUser>" user using API
    ## 1. Get logged-in user profile
    Then I send a GET request to "/users/profile/me"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "MyUserId"
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 2. Update own profile
    Then I send a PUT request to "/users/profile/me" with payload:
      """
      {
        "firstName": "<FirstName>",
        "lastName": "<LastName>",
        "title": "Mr",
        "gender": "MALE",
        "dateOfBirth": "1990-01-01",
        "primaryNumber": {
          "code": "+91",
          "number": "9000000001",
          "fullNumber": "+919000000001"
        },
        "presentAddress": {
          "line1": "123 Test Street",
          "city": "Kolkata",
          "state": "WB",
          "country": "IN",
          "pinCode": "700001"
        }
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                       | Value   |
      | status                          | SUCCESS |
      | responsePayload.profile_updated | true    |
    ## 3. Load KPI metrics
    Then I send a GET request to "/users/profile/metrics"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |

    Examples:
      | LoginUser                  | FirstName | LastName |
      | cashier@nabarun.com        | Cashier   | TestUser |
      | president@nabarun.com      | President | TestUser |
