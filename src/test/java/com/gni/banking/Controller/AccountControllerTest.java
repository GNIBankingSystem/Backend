package com.gni.banking.Controller;
import com.gni.banking.Configuration.Jwt.JwtTokenProvider;
import com.gni.banking.Enums.AccountType;
import com.gni.banking.Enums.Currency;
import com.gni.banking.Enums.Role;
import com.gni.banking.Enums.Status;
import com.gni.banking.Model.Account;
import com.gni.banking.Service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService service;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {


    }

    @Test
    void getAll() throws Exception {
        // We 'Arrange', 'Act' and 'Assert' if the results match what we expect

        // First, we 'Arrange' everything for our test
        jwtTokenProvider = new JwtTokenProvider();
        String token = jwtTokenProvider.createToken("username", Role.Employee, 1);

        // Mockito allows us to 'inject' return values for methods we call
        // This way, we don't actually test the service, just the controller
        when(service
                .getAll(0, 10, null, null, null))
                .thenReturn(List.of(
                        new Account("Nl01INHO000000032133", 1, AccountType.Current, 1000.0, Currency.EUR, 200.0, Status.Open)));

        // Check if we get a 200 OK
        // And if the JSON content matches our expectations
        this.mockMvc.perform(get("/account")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
                //.andExpect(jsonPath("$[0].brand").value("Honda"));
    }

}
