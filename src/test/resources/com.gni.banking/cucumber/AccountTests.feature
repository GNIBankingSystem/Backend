Feature: Accounts CRUD operations

  Scenario: Get all accounts
    Given I have a valid JWT token
    And The endpoint for "accounts" is available for method "GET"
    When I retrieve all accounts
    Then I should receive all accounts

  Scenario: Create an account
    Given I have a valid JWT token
    And The endpoint for "accounts" is available for method "POST"
    When I create an account with userId 1 and accountType "Savings"
    Then The response status is 201
    And The account has an id

  Scenario: Get an account
    Given I have a valid JWT token
    And The endpoint for "accounts" is available for method "GET"
    And I create an account with id "NL01INHO0000000002"
    When I get a account with id "NL01INHO0000000002"
    Then The response status is 200

  Scenario: Update an account
    Given I have a valid JWT token
    And The endpoint for "accounts/:id" is available for method "PUT"
    And I create an account with id "NL01INHO0000000003"
    When I update the account type of an account with id "NL01INHO0000000003" to "Current"
    Then The response status is 200
    Then The account type of the account with id "NL01INHO0000000003" is "Current"

  Scenario: Delete an account
    Given I have a valid JWT token
    And The endpoint for "accounts/:id" is available for method "DELETE"
    And I create an account with id "NL01INHO0000000004"
    When I delete the account with id "NL01INHO0000000004"
    Then The response status is 200
    Then The account with id "NL01INHO0000000004" is deleted