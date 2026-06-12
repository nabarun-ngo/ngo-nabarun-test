Feature: API Tests for Expense Management

  @api @api_regression @api_expense @api_expense01
  Scenario Outline: API_Expense Full Lifecycle — Create, Update, Finalize, Settle
    Given I login with "<LoginUser>" user using API
    ## 1. Get logged-in user ID (used as payerId)
    Then I send a GET request to "/users/profile/me"
    Then The API response status code should be 200
    And I extract data from response using JSON token "responsePayload.id" and store it as "PayerId"
    ## 2. Load expense reference data
    Then I send a GET request to "/expense/static/referenceData"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 3. Create a new expense with line items
    Then I store "{RandomName} Expense" value as "ExpenseName" variable
    Then I store "{SystemDate [yyyy-MM-dd]}" value as "ExpenseDate" variable
    Then I send a POST request to "/expense/create" with payload:
      """
      {
        "name": "{ExpenseName}",
        "expenseRefType": "OPERATIONAL",
        "payerId": "{PayerId}",
        "currency": "INR",
        "expenseDate": "{ExpenseDate}",
        "description": "Automation test expense",
        "expenseItems": [
          {
            "name": "Stationery",
            "quantity": 2,
            "unitPrice": 50,
            "amount": 100
          }
        ]
      }
      """
    Then The API response status code should be 201
    And I extract data from response using JSON token "responsePayload.id" and store it as "ExpenseId"
    And The API response should have the following attributes
      | Attribute                      | Value       |
      | status                         | SUCCESS     |
      | responsePayload.expenseRefType | OPERATIONAL |
    ## 4. Verify expense record by ID
    Then I send a GET request to "/expense/{ExpenseId}"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute          | Value       |
      | responsePayload.id | {ExpenseId} |
    ## 5. Update the expense description
    Then I send a PUT request to "/expense/{ExpenseId}/update" with payload:
      """
      {
        "description": "Updated by automation test"
      }
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 6. Finalize the expense (locks record — no further edits allowed)
    Then I send a POST request to "/expense/{ExpenseId}/finalize" with payload:
      """
      {}
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 7. Settle the expense (marks as paid)
    Then I send a POST request to "/expense/{ExpenseId}/settle" with payload:
      """
      {}
      """
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 8. Verify expense appears in admin list
    Then I send a GET request to "/expense/list?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |
    ## 9. DB — confirm final status persisted in database
    Then The database table "expenses" record with id "{ExpenseId}" should have:
      | Field  | Value   |
      | status | SETTLED |
    ## 10. DB — confirm at least one expense record exists
    Then The database table "expenses" should contain at least 1 records

    Examples:
      | LoginUser                      |
      | cashier@nabarun.com            |
      | assistantcashier@nabarun.com   |

  @api @api_regression @api_expense @api_expense02
  Scenario Outline: API_Self Expense List
    Given I login with "<LoginUser>" user using API
    ## View own expense list (no admin permission required)
    Then I send a GET request to "/expense/list/me?page=0&size=10"
    Then The API response status code should be 200
    And The API response should have the following attributes
      | Attribute | Value   |
      | status    | SUCCESS |

    Examples:
      | LoginUser                      |
      | cashier@nabarun.com            |
      | assistantcashier@nabarun.com   |
      | member@nabarun.com             |
