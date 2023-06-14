Feature: Transaction CRUD Operations

  Scenario: Get all Transactions
    Given I have a valid token
    And The endpoint "transactions" is available for method "GET"
    When I retrieve all transactions
    Then I should get a list of transactions


  Scenario: Get a Transaction
    Given I have a valid token
    And The endpoint "transactions/:id" is available for method "GET"
    When I retrieve a transaction with id 1
    Then I should get a transaction with id 1

  Scenario: Create a Transaction
    Given I have a valid token
    And The endpoint "transactions" is available for method "POST"
    When I create a new transaction with accountFrom "NL01INHO0000000001" and accountTo "NL01INHO0000000011" and amount 10.0
    Then The response is 200

  Scenario: Update a Transaction
    Given I have a valid token
    And The endpoint "transactions/:id" is available for method "PUT"
    When I update transaction with id 1 with accountFrom "NL01INHO0000000001" and accountTo "NL01INHO0000000011" and amount 20.0 and timestamp "Wed Jun 14 21:15:49 CEST 2023" and performedBy 1
    Then The response is 200


  Scenario: Delete a Transaction
    Given I have a valid token
    And The endpoint "transactions/:id" is available for method "DELETE"
    And I create a new transaction with accountFrom "NL01INHO0000000054" and accountTo "NL01INHO0000000001" and amount 10
    When I delete transaction with id 1
    Then The response is 200