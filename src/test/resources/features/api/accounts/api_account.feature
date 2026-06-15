Feature: API Tests for Account and Transaction Management

  @api @api_regression @api_account @api_account01
  Scenario Outline: API_Account Create, Update, and Transaction View
    Given I login with "<LoginUser>" user using API
    ## 1. Get logged-in user ID to use as account holder
    Then I send a GET request to "/users/profile/me"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "MyUserId"
    ## 2. Load account reference data
    Then I send a GET request to "/account/static/referenceData"
    Then The API response status code should be 200
    ## 3. Create a new WALLET account
    Then I store "{RandomName} Wallet" value as "AccountName" variable
    Then I send a POST request to "/account/create" with payload:
      """
      {
        "name": "{AccountName}",
        "type": "WALLET",
        "currency": "INR",
        "initialBalance": 0,
        "description": "Test wallet account created by automation",
        "accountHolderId": "{MyUserId}"
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "AccountId"
    And The API response should have the following attributes
      | Attribute            | Value   |
      | status               | SUCCESS |
      | responsePayload.type | WALLET  |
    ## 4. Update the account name and description
    Then I send a PUT request to "/account/{AccountId}/update" with payload:
      """
      {
        "name": "{AccountName} Updated",
        "description": "Updated by automation test"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 5. Verify the account appears in the list
    Then I send a GET request to "/account/list?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 6. View transactions for the new account (should be empty initially)
    Then I send a GET request to "/account/{AccountId}/transactions?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 7. DB — verify account row persisted correctly
    Then The database table "accounts" record with id "{AccountId}" should have:
      | Field    | Value  |
      | type     | WALLET |
      | currency | INR    |
      | status   | ACTIVE |

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | treasurer@nabarun.com |

  @api @api_regression @api_account @api_account02
  Scenario Outline: API_Admin Fund Transfer Between Accounts
    Given I login with "<LoginUser>" user using API
    ## 1. Get logged-in user ID
    Then I send a GET request to "/users/profile/me"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "MyUserId"
    ## 2. Create a fresh source GENERAL account for the transfer
    Then I store "{RandomName} Source" value as "SourceAccountName" variable
    Then I send a POST request to "/account/create" with payload:
      """
      {
        "name": "{SourceAccountName}",
        "type": "GENERAL",
        "currency": "INR",
        "initialBalance": 0,
        "description": "Source account for transfer test",
        "accountHolderId": "{MyUserId}"
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "SourceAccountId"
    ## 3. Get payable target accounts list
    Then I send a GET request to "/account/payable-account"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload[0].id" and store it as "TargetAccountId"
    ## 4. Execute the fund transfer (Admin)
    Then I store "{RandomInt:10,99}" value as "TransferAmount" variable
    Then I send a POST request to "/account/{SourceAccountId}/transfer" with payload:
      """
      {
        "toAccountId": "{TargetAccountId}",
        "amount": "{TransferAmount}",
        "description": "Automation fund transfer test",
        "transferDate": "{SystemDate [yyyy-MM-dd]}"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 5. Verify debit transaction recorded on the source account
    Then I send a GET request to "/account/{SourceAccountId}/transactions?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                            | Value            |
      | status                               | SUCCESS          |
      | responsePayload.content[0].txnType   | OUT              |
      | responsePayload.content[0].txnAmount | {TransferAmount} |
      | responsePayload.content[0].txnStatus | SUCCESS          |
    ## 6. Verify credit transaction recorded on the target account
    Then I send a GET request to "/account/{TargetAccountId}/transactions?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute                            | Value   |
      | status                               | SUCCESS |
      | responsePayload.content[0].txnType   | IN      |
      | responsePayload.content[0].txnStatus | SUCCESS |

    Examples:
      | LoginUser             |
      | cashier@nabarun.com   |
      | treasurer@nabarun.com |
