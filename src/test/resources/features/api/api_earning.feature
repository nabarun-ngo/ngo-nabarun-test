Feature: API Tests for Earning Management

  @api @api_regression @api_earning @api_earning01
  Scenario Outline: API_Earning Create, Update, and Mark Received with Database Verify
    Given I login with "<LoginUser>" user using API
    ## 1. Load earning reference data
    Then I send a GET request to "/earning/static/referenceData"
    Then The API response status code should be 200
    ## 2. Baseline earning count
    Then I send a GET request to "/earning/list?pageIndex=0&pageSize=10"
    Then The API response status code should be 200
    ## 3. Create a new earning record
    Then I store "{RandomName} Fund" value as "EarningSource" variable
    Then I send a POST request to "/earning/create" with payload:
      """
      {
        "amount": 5000,
        "category": "GRANT",
        "source": "{EarningSource}",
        "currency": "INR",
        "description": "Test earning created by automation"
      }
      """
    #Bug https://ngonabarun.atlassian.net/browse/NAB-245
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "EarningId"
    And The API response should have the following attributes
      | Attribute                   | Value                              |
      | responsePayload.amount      | 5000                               |
      | responsePayload.category    | GRANT                              |
      | responsePayload.currency    | INR                                |
      | responsePayload.description | Test earning created by automation |
      | responsePayload.status      | PENDING                            |
    ## 4. Fetch the earning by ID
    Then I send a GET request to "/earning/{EarningId}"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                   | Value                              |
      | responsePayload.id          | {EarningId}                        |
      | responsePayload.amount      | 5000                               |
      | responsePayload.category    | GRANT                              |
      | responsePayload.currency    | INR                                |
      | responsePayload.description | Test earning created by automation |
      | responsePayload.status      | PENDING                            |
    ## 5. Update earning amount and description
    # Bug https://ngonabarun.atlassian.net/browse/NAB-246
    Then I send a PUT request to "/earning/{EarningId}/update" with payload:
      """
      {
        "description": "Updated by automation test",
        "amount": 5500,
        "category": "INTEREST"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                   | Value                      |
      | responsePayload.amount      | 5500                       |
      | responsePayload.description | Updated by automation test |
      | responsePayload.category    | INTEREST                   |
    ## 6. Verify updated earning appears
    Then I send a GET request to "/earning/{EarningId}"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                   | Value                      |
      | responsePayload.amount      | 5500                       |
      | responsePayload.description | Updated by automation test |
    ## 7. DB — confirm updated values persisted in the earnings table
    Then The database table "earnings" record with "id" equals "{EarningId}" should have:
      | Field       | Value                      |
      | amount      | 5500.00                    |
      | description | Updated by automation test |
      | category    | INTEREST                   |
    ## 8. Update earning status to RECEIVED
    Then I store "{SystemDate [yyyy-MM-dd]}" value as "EarningDate" variable
    Then I send a PUT request to "/earning/{EarningId}/update" with payload:
      """
      {
        "earningDate": "{EarningDate}",
        "status": "RECEIVED",
        "accountId": "ACC_CASHIER_DONATION"
      }
      """
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.transactionId" and store it as "TransactionRef"
    And The API response should have the following attributes
      | Attribute                   | Value                       |
      | responsePayload.accountId   | ACC_CASHIER_DONATION        |
      | responsePayload.status      | RECEIVED                    |
      | responsePayload.earningDate | {EarningDate}T00:00:00.000Z |
    Then The database table "earnings" record with "id" equals "{EarningId}" should have:
      | Field     | Value                |
      | accountid | ACC_CASHIER_DONATION |
      | status    | RECEIVED             |

    ## 3. Verify Inward Transaction in Main Account
    Given I login with "treasurer@nabarun.com" user using API
    Then I send a GET request to "/account/ACC_CASHIER_DONATION/transactions?pageIndex=0&pageSize=10&transactionRef={TransactionRef}"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                                 | Value            |
      | responsePayload.content[0].transactionRef | {TransactionRef} |
      | responsePayload.content[0].txnType        | IN               |
      | responsePayload.content[0].txnAmount      | 5500             |
      | responsePayload.content[0].txnStatus      | SUCCESS          |
      | responsePayload.content[0].txnRefId       | {EarningId}      |
      | responsePayload.content[0].txnRefType     | EARNING          |
    Examples:
      | LoginUser                 |
      | president@nabarun.com     |
      | vicepresident@nabarun.com |


  @api @api_regression @api_earning @api_earning02
  Scenario Outline: API_Earning Create, Update, and Mark Cancelled with Database Verify
    Given I login with "<LoginUser>" user using API
    ## 3. Create a new earning record
    Then I store "{RandomName} Fund" value as "EarningSource" variable
    Then I send a POST request to "/earning/create" with payload:
      """
      {
        "amount": 200,
        "category": "SPONSORSHIP",
        "source": "{EarningSource}",
        "currency": "INR",
        "description": "Test earning created by automation for cancellation"
      }
      """
    #Bug https://ngonabarun.atlassian.net/browse/NAB-245
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "EarningId"
    And The API response should have the following attributes
      | Attribute                   | Value                                               |
      | responsePayload.amount      | 200                                                 |
      | responsePayload.category    | SPONSORSHIP                                         |
      | responsePayload.currency    | INR                                                 |
      | responsePayload.description | Test earning created by automation for cancellation |
      | responsePayload.status      | PENDING                                             |
    ## 5. Update earning amount and description
    # Bug https://ngonabarun.atlassian.net/browse/NAB-246
    Then I send a PUT request to "/earning/{EarningId}/update" with payload:
      """
      {
        "status": "CANCELLED"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute              | Value     |
      | responsePayload.status | CANCELLED |
    ## 7. DB — confirm updated values persisted in the earnings table
    Then The database table "earnings" record with "id" equals "{EarningId}" should have:
      | Field  | Value     |
      | amount | 200.00    |
      | status | CANCELLED |

    Examples:
      | LoginUser                 |
      | president@nabarun.com     |
      | vicepresident@nabarun.com |

  @api @api_regression @api_earning @api_earning03
  Scenario Outline: API_Earning Negative Testing_Create Earning
    Given I login with "<LoginUser>" user using API
    ## 3. Create a new earning record
    Then I store "{RandomName} Fund" value as "EarningSource" variable
    Then I send a POST request to "/earning/create" with payload:
      """
      {
        "amount": 200,
        "category": "SPONSORSHIP",
        "source": "{EarningSource}",
        "currency": "INR",
        "description": "Test earning created by automation for cancellation"
      }
      """
    Then The API response status code should be 403
    Examples:
      | LoginUser          |
      | member@nabarun.com |