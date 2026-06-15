Feature: API Tests for Self Account and Transaction Management

  @api @api_regression @api_account @api_account01
  Scenario Outline: API_Account Self 
    Given I login with "<LoginUser>" user using API
    Examples:
      | LoginUser |