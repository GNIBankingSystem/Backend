package com.gni.banking.Cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gni.banking.Configuration.Jwt.JwtKeyProvider;
import com.gni.banking.Configuration.Jwt.JwtTokenFilter;
import com.gni.banking.Configuration.Jwt.JwtTokenProvider;
import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Role;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.TransactionRequestDTO;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.h2.mvstore.tx.Transaction;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

public class TransactionStepDefinitions extends BaseStepDefinitions {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;
    @Autowired
    private JwtKeyProvider jwtKeyProvider;
    private final HttpHeaders httpHeaders = new HttpHeaders();

    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;

    @When("I retrive all transactions")
    public void iRetriveAllTransactions() {
        response = restTemplate.exchange(
                "/transactions",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
    }

    @Then("I should get a list of transactions")
    public void iShouldGetAListOfTransactions() {
        int actual = JsonPath.read(response.getBody(), "$.size()");
        Assertions.assertEquals(10, actual);
    }


    @When("I create a new transaction with accountFrom {string} and accountTo {string} and amount {double}")
    public void iCreateANewTransactionWithAccountFromAndAccountToAndAmount(String accountFrom, String accountTo, double amount) throws JsonProcessingException {

        response = restTemplate.exchange(
                "/transactions",
                HttpMethod.POST,
                new HttpEntity<>(mapper.writeValueAsString(createTransctiontionRequestDto(accountFrom, accountTo, amount)), httpHeaders),
                String.class);
    }

    private TransactionRequestDTO createTransctiontionRequestDto(String accountFrom, String accountTo, double amount) {
        TransactionRequestDTO transactionRequestDTO = new TransactionRequestDTO();
        transactionRequestDTO.setAccountFrom(accountFrom);
        transactionRequestDTO.setAccountTo(accountTo);
        transactionRequestDTO.setAmount(amount);
        return transactionRequestDTO;
    }

    @And("The transaction has an id")
    public void theTransactionHasId() throws JsonProcessingException {
        Transaction transaction = mapper.readValue(response.getBody(), Transaction.class);
        Assertions.assertNotNull(transaction.getId());
    }

    @When("I retrive a transaction with id {int}")
    public void iRetriveATransactionWithId(int id) {
        response = restTemplate.exchange(
                "/transactions/" + id,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
    }

    @Then("I update transaction with id {int} with accountFrom {string} and accountTo {string} and amount {double}")
    public void iUpdateTransactionWithIdWithAccountFromAndAccountToAndAmount(int id, String accountFrom, String accountTo, double amount) throws JsonProcessingException {
        response = restTemplate.exchange(
                "/transactions/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(mapper.writeValueAsString(createTransctiontionRequestDto(accountFrom, accountTo, amount)), httpHeaders),
                String.class);
    }
    @When("I delete transaction with id {int}")
    public void iDeleteTransactionWithId(int id) {
        response = restTemplate.exchange(
                "/transactions/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                String.class);
    }

    @Then("The transaction with id {int} is archived")
    public void theTransactionWithIdIsArchived(int id) throws JsonProcessingException {
        response = restTemplate.exchange(
                "/transactions/" + id,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
        Transaction transaction = mapper.readValue(response.getBody(), Transaction.class);
        Assertions.assertTrue(JsonPath.read(response.getBody(), "$.isArchived"));
    }

    @When("I retrive all transactions for account {string}")
    public void iRetriveAllTransactionsForAccount(String account) {
        response = restTemplate.exchange(
                "/transactions/account/" + account,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
    }

    @Then("I should get a list of transactions for account {string}")
    public void iShouldGetAListOfTransactionsForAccount(String account) {
        int actual = JsonPath.read(response.getBody(), "$.size()");
        Assertions.assertEquals(10, actual);
    }

    @When("I retrive all transactions for account {string} and type {string}")
    public void iRetriveAllTransactionsForAccountAndType(String account, String type) {
        response = restTemplate.exchange(
                "/transactions/account/" + account + "/type/" + type,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
    }
}
