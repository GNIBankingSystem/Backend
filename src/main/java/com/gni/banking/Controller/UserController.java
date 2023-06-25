package com.gni.banking.Controller;


import com.gni.banking.Model.Transaction;
import com.gni.banking.Model.TransactionRequestDTO;
import com.gni.banking.Model.UserResponse;
import com.gni.banking.Service.TransactionService;
import com.gni.banking.Service.UserService;
import com.gni.banking.Model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    ModelMapper modelMapper;

    public UserController() {
        modelMapper = new ModelMapper();
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam(defaultValue = "0") int offset,
                                  @RequestParam(defaultValue = "10") int limit,
                                  @RequestParam(required = false) String userId,
                                  @RequestParam(required = false) String username,
                                  @RequestParam(required = false) String status) throws Exception {
        return userService.getAll(offset, limit, userId, username, status);
    }


    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        return userService.getById(userId);
    }

    /*@PostMapping
    public ResponseEntity<UserResponse> add(@RequestBody User a) {
        User savedUser = a;
        userService.add(savedUser);



        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setUsername(savedUser.getUsername());
        userResponse.setToken("Successfully registered");
        userResponse.setRoles(savedUser.getRoles());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }*/




    @PutMapping("/{userid}")
    public User update(@RequestBody User user, @PathVariable long userid) throws Exception {
        return userService.update(user, userid);
    }

    @DeleteMapping("/{userid}")
    public User changeStatus(@PathVariable long userid) throws Exception {

        return userService.changeStatus(userid);
    }


    @GetMapping("/{id}/transactions")
    public ResponseEntity getAll(
            @PathVariable long id,
            @RequestParam(required = false) String ibanTo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate,
            @RequestParam(required = false) String comparisonOperator,
            @RequestParam(required = false) Double balance
    ) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByUserId(id, startDate, endDate, ibanTo, comparisonOperator, balance);
            List<TransactionRequestDTO> transactionDtos = transactions.stream()
                    .map(transaction -> modelMapper.map(transaction, TransactionRequestDTO.class))
                    .collect(Collectors.toList());
            return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), null, 401);
        }
    }


}
