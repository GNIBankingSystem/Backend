package com.gni.banking.Cucumber.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gni.banking.Configuration.Jwt.JwtKeyProvider;
import com.gni.banking.Configuration.Jwt.JwtTokenFilter;
import com.gni.banking.Configuration.Jwt.JwtTokenProvider;
import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Role;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Model.PostAccountDTO;
import com.gni.banking.Model.PutAccountDTO;
import com.gni.banking.Repository.AccountRepository;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.bs.A;
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
import java.util.Objects;

public class AccountStepDefinitions extends BaseStepDefinitions{

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

    @Given("I have a valid JWT token")
    public void iHaveAValidJWTToken() {
        String jwtToken = jwtTokenProvider.createToken("username", Role.Employee, 2);
        httpHeaders.add("Authorization", "Bearer " + jwtToken);
    }

    @Given("The endpoint for {string} is available for method {string}")
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

    @When("I retrieve all accounts")
    public void i_retrieve_all_accounts() {

        response = restTemplate.exchange(restTemplate.getRootUri() + "/accounts", HttpMethod.GET, new HttpEntity<>(null, httpHeaders), String.class);
    }

    @Then("I should receive all accounts")
    public void i_should_receive_all_accounts() {

        int actual = JsonPath.read(response.getBody(), "$.size()");
        Assertions.assertEquals(5, actual);
    }

    @When("I create an account with userId {int} and accountType {string}")
    public void iCreateAnAccountWithUserIdAndAccountType(int userId, String accountType) throws JsonProcessingException {
        PostAccountDTO dto = createPostAccountDTO(userId, accountType);
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange("/accounts",
                HttpMethod.POST,
                new HttpEntity<>(
                        mapper.writeValueAsString(dto),
                        httpHeaders
                ), String.class);
    }

    private PostAccountDTO createPostAccountDTO(int userId, String accountType) {
        PostAccountDTO dto = new PostAccountDTO();
        dto.setUserId(userId);
        dto.setType(AccountType.valueOf(accountType));
        return dto;
    }

    @Then("The response status is {int}")
    public void theResponseStatusIs(int status) {
        Assertions.assertEquals(status, response.getStatusCode().value());
    }

    @And("The account has an id")
    public void theAccountIdIs() throws JsonProcessingException {
        Account account = mapper.readValue(response.getBody(), Account.class);
        Assertions.assertNotNull(account.getId());
    }

    @And("I create an account with id {string}")
    public void iCreateAnAccountWithId(String id) throws JsonProcessingException {
        Account account = new Account(id, 1, AccountType.Savings, 0.0, Currency.EUR, 100, Status.Open);
        restTemplate.exchange("/accounts/addAccount",
                HttpMethod.POST,
                new HttpEntity<>(
                        account,
                        httpHeaders
                ), String.class);
    }

    @When("I get a account with id {string}")
    public void iGetAAccountWithId(String id) {
        response = restTemplate.exchange("/accounts/" + id,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
    }

    @When("I update the account type of an account with id {string} to {string}")
    public void iUpdateTheAccountTypeOfAnAccountWithIdTo(String id, String accountType) {
        PutAccountDTO dto = new PutAccountDTO();
        dto.setType(AccountType.valueOf(accountType));
        response = restTemplate.exchange("/accounts/" + id,
                HttpMethod.PUT,
                new HttpEntity<>(
                        dto,
                        httpHeaders
                ), String.class);
    }

    @Then("The account type of the account with id {string} is {string}")
    public void theAccountTypeOfTheAccountWithIdIs(String id, String accountType) throws JsonProcessingException {
        response = restTemplate.exchange("/accounts/" + id,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
        Account account = mapper.readValue(response.getBody(), Account.class);
        Assertions.assertEquals(AccountType.valueOf(accountType), account.getType());
    }

    @When("I delete the account with id {string}")
    public void iDeleteTheAccountWithId(String id) {
        restTemplate.exchange("/accounts/" + id,
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                String.class);
    }

    @Then("The account with id {string} is deleted")
    public void theAccountWithIdIsDeleted(String id) throws JsonProcessingException {
        response = restTemplate.exchange("/accounts/" + id,
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class);
        Account account = mapper.readValue(response.getBody(), Account.class);
        Assertions.assertEquals(account.getStatus(), Status.Closed);
    }
}
