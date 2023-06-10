package com.gni.banking.Cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gni.banking.Enums.AccountType;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.PostAccountDTO;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

public class AccountStepDefinitions extends BaseStepDefinitions{

    @Autowired
    private TestRestTemplate restTemplate;

    private final HttpHeaders httpHeaders = new HttpHeaders();

    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;

    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailable(String endpoint, String method) {
        response = restTemplate
                .exchange("/" + endpoint,
                        HttpMethod.OPTIONS,
                        new HttpEntity<>(null, httpHeaders), // null because OPTIONS does not have a body
                        String.class);
        List<String> options = Arrays.stream(response.getHeaders()
                        .get("Allow")
                        .get(0)// The first element is all allowed methods separated by comma
                        .split(","))
                .toList();
        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }

    @When("^I retrieve all accounts$")
    public void i_retrieve_all_accounts() {

        response = restTemplate.exchange(restTemplate.getRootUri() + "/accounts", HttpMethod.GET, new HttpEntity<>(null, new HttpHeaders()), String.class);
    }

    @Then("I should receive all accounts")
    public void i_should_receive_all_accounts() {

        int actual = JsonPath.read(response.getBody(), "$.size()");
        Assertions.assertEquals(1, actual);
    }

    @When("I create a account with userId {int} and accountType {AccountType}")
    public void iCreateAnAccountWithUserIdAndAccountType(int userId, AccountType accountType) throws JsonProcessingException {
        PostAccountDTO dto = createPostAccountDTO(userId, accountType);
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange("/accounts",
                HttpMethod.POST,
                new HttpEntity<>(
                        mapper.writeValueAsString(dto),
                        httpHeaders
                ), String.class);
    }

    private PostAccountDTO createPostAccountDTO(int userId, AccountType accountType) {
        PostAccountDTO dto = new PostAccountDTO();
        dto.setUserId(userId);
        dto.setType(accountType);
        return dto;
    }

    @Then("The response status is {int}")
    public void theResponseStatusIs(int status) {
        Assertions.assertEquals(status, response.getStatusCode().value());
    }

    @And("The account id is {string}")
    public void theAccountIdIs(String id) throws JsonProcessingException {
        Account account = mapper.readValue(response.getBody(), Account.class);
        Assertions.assertEquals(id, account.getId());
    }
}
