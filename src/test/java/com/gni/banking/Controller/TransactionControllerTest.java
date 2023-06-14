package com.gni.banking.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.gni.banking.Configuration.Jwt.JwtTokenDecoder;
import com.gni.banking.Configuration.Jwt.JwtTokenProvider;
import com.gni.banking.Enums.TransactionType;
import com.gni.banking.Model.Transaction;
import com.gni.banking.Service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private JwtTokenDecoder jwtTokenDecoder;

    private final String token = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlbXBsb3llZSIsImF1dGgiOiJST0xFX0VNUExPWUVFIiwiaWQiOjMsImlhdCI6MTY4NjczODYyMSwiZXhwIjoxNzE4MzYxMDIxfQ.OB2kZct-BgORjdSHerarq8WgBiriGlvz5kX8sM3aAfDsyfINFLx_ktVy7ewEZ74iuxig-xNHdtA5-1LQAUyK9-DEawlW94Qz8V4LKdSTplVDznoi92M0U2KiT0k7HkZCmIGGiSIapuLsdT0v3CpfyUlxsuivtmyutDr1XplERKbdjkQAuHmzbwcN4MhpLWJcxLlO4l14by_q-Hl9WINyBce0b70zKTp3EAX7Ol9cp03Ufj3VkINVaCS_vgEaEDrNd7uJM92ar4hzZ442vQ9CHp7yxH4NNozafA0ZnPYPLClAPF9lsbHAuh79XbReHW91fMtyEHLI2zLbSmMS2gtd6Q";   @Mock
    private ObjectMapper mapper;

    //happy flow
    @Test
    void getAllTransactions() throws Exception {

        // mock the call to transactionService.getAll()
        when(transactionService.getAll(anyInt(), anyInt(), any())).thenReturn(createPage());

        // now perform the test
        this.mockMvc.perform(get("/transactions").header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk());
    }
    //happy flow
    @Test
    void getTransactionById() throws Exception {
        when(transactionService.getById(1)).thenReturn(createTransaction(1,new Date(),"NL01INHO0000000001","NL01INHO0000000002",100,1, TransactionType.TRANSFER));
        this.mockMvc.perform(get("/transactions/1").header("Authorization", "Bearer " +token))
                .andDo(print())
                .andExpect(status().isOk());
    }
    //id is null
    @Test
    void getTransactionByIdWhereIdIsNull() throws Exception {
        this.mockMvc.perform(get("/transactions/0").header("Authorization", "Bearer " +token))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Illegal id ,please enter a valid id "));
    }
    //id is string
    @Test
    void getTransactionByIdWhereIdIsAnyString() throws Exception {
        this.mockMvc.perform(get("/transactions/dkjasbdas").header("Authorization", "Bearer " +token))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Please enter right format of input"));

    }
    //happy flow
    @Test
    void createTransaction() throws Exception {
        when(transactionService.add(any())).thenReturn(createTransaction(1,new Date(),"NL01INHO0000000001","NL01INHO0000000002",100,1, TransactionType.TRANSFER));

        this.mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content("""
                                {
                                "accountFrom": "NL01INHO0000000001",
                                "accountTo": "NL01INHO0000000002",
                                "amount": 100.00
                                }
                                """)
                        .header("Authorization", "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("TRANSFER"));
    }

    @Test
    void deleteTransaction() throws Exception {
        long id = 1L;

        // Perform the DELETE request
        mockMvc.perform(delete("/transactions/" + id))
                .andExpect(status().isOk());

        // Verify that the service's delete method was called once with the correct argument
        verify(transactionService, times(1)).delete(id);
    }


    private Transaction createTransaction(int id , Date timeStamp , String accountFrom, String accountTo, double amount, int performedBy,TransactionType transactionType){
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setTimestamp(timeStamp);
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setAmount(amount);
        transaction.setPerformedBy(performedBy);
        transaction.setType(transactionType);
        return transaction;
    }

    private Page<Transaction> createPage(){
        List<Transaction> list = new ArrayList<>();
        list.add(createTransaction(1,new Date(),"NL01INHO0000000001","NL01INHO0000000002",100,1, TransactionType.TRANSFER));
        Pageable pageable = PageRequest.of(0,10);
        return new PageImpl<>(list,pageable,1);
    }

}
