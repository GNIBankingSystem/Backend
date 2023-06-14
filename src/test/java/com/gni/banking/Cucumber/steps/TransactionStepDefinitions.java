package com.gni.banking.Cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gni.banking.Configuration.Jwt.JwtKeyProvider;
import com.gni.banking.Configuration.Jwt.JwtTokenFilter;
import com.gni.banking.Configuration.Jwt.JwtTokenProvider;
import com.gni.banking.Enums.Role;
import com.gni.banking.Model.TransactionPutDto;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

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

    @Given("I have a valid token")
    public void iHaveAValidJWTToken() {
        String jwtToken = jwtTokenProvider.createToken("admin", Role.ROLE_EMPLOYEE, 2);
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
    }

    @Given("The endpoint {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {
        response = restTemplate.exchange(
                "/" + endpoint,
                HttpMethod.OPTIONS,
                new HttpEntity<>(null, httpHeaders),
                String.class
        );

        List<String> options = Arrays.asList(Objects.requireNonNull(response.getHeaders().get("Allow")).get(0).split(","));
        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }

    @Then("The response is {int}")
    public void theResponseStatusIs(int status) {

        System.out.println(response);
        Assertions.assertEquals(status, response.getStatusCode().value());
    }

    @When("I retrieve all transactions")
    public void iRetrieveAllTransactions() {
        response = restTemplate.exchange(
                "/transactions",
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
    }

    @Then("I should get a list of transactions")
    public void iShouldGetAListOfTransactions() {
        int actual = JsonPath.read(response.getBody(), "$.size()");
        Assertions.assertEquals(1, actual);
    }



    @Then("I update transaction with id {int} with accountFrom {string} and accountTo {string} and amount {double} and date {string} ")
    public void iUpdateTransactionWithIdWithAccountFromAndAccountToAndAmount(int id, String accountFrom, String accountTo, double amount) throws JsonProcessingException {
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange(
                "/transactions/" + id,
                HttpMethod.PUT,
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
        System.out.print(response);
        Transaction transaction = mapper.readValue(response.getBody(), Transaction.class);
        Assertions.assertEquals(anyInt(), transaction.getId());
    }

    @When("I retrieve a transaction with id {int}")
    public void iRetrieveATransactionWithId(int id) {
        response = restTemplate.exchange(
                "/transactions/" + id,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
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


    @Then("transaction has the new amount {double}")
    public void thenAccountHasTheNewAmount(double amount) {
        double actual = JsonPath.read(response.getBody(), "$.amount");
        Assertions.assertEquals(amount, actual);
    }

    @When("I update transaction with id {int} with accountFrom {string} and accountTo {string} and amount {double} and timestamp {string} and performedBy {int}")
    public void iUpdateTransactionWithIdWithAccountFromAndAccountToAndAmountAndTimestampAndPerfomedBy(int id, String accountFrom, String accountTo, double amount, String timeStamp, int performedBy) throws JsonProcessingException, ParseException {
        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH).parse(timeStamp);

        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange(
                "/transactions/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(mapper.writeValueAsString(createTransactionPutDto(accountFrom,accountTo,amount,date,performedBy)), httpHeaders),
                String.class);

    }

    TransactionPutDto createTransactionPutDto(String accountFrom, String accountTo, double amount, Date timeStamp, int performedBy){
        TransactionPutDto transactionPutDto = new TransactionPutDto();
        transactionPutDto.setAccountFrom(accountFrom);
        transactionPutDto.setAccountTo(accountTo);
        transactionPutDto.setAmount(amount);
        transactionPutDto.setTimestamp(timeStamp);
        transactionPutDto.setPerformedBy(performedBy);
        return transactionPutDto;

    }

    @Then("I should get a transaction with id {int}")
    public void iShouldGetATransactionWithId(int id) {
        int actual = JsonPath.read(response.getBody(), "$.id");
        Assertions.assertEquals(id, actual);
    }

    @When("I create a new transaction with accountFrom {string} and accountTo {string} and amount {double}")
    public void iCreateANewTransactionWithAccountFromAndAccountToAndAmount(String accountFrom, String accountTo, double amount) {
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange(
                "/transactions",
                HttpMethod.POST,
                new HttpEntity<>(createTransctiontionRequestDto(accountFrom, accountTo, amount), httpHeaders),
                String.class);
    }
}
