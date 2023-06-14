Feature: Transaction CRUD Operations

  Scenario: Get all Transactions
    Given I have a valid JWT token
    And The endpoint for "transactions" is available for method "GET"
    Then I should receive all transactions
    Then I should receive all accounts


  Scenario: Get a Transaction
    Given I have a valid JWT token
    And The endpoint for "transactions" is available for method "GET"
    Then I should receive a transaction


  Scenario: Create a Transaction
    Given I have a valid JWT token


  Scenario: Update a Transaction
    Given I have a valid JWT token



  Scenario: Delete a Transaction
    Given I have a valid JWT token
    And The endpoint for "transactions/:id" is available for method "DELETE"
    And I create a new transaction with accountFrom "NL01INHO0000000054" and accountTo "NL01INHO0000000001" and amount 10
    When I delete transaction with id 1
    Then The response status is 200
    Then The transaction with id 1 is archived