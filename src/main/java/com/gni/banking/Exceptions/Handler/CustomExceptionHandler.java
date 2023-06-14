package com.gni.banking.Exceptions.Handler;

import com.gni.banking.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
public class CustomExceptionHandler {

    @ExceptionHandler({InvalidAccountTypeOnTransactionException.class,
            NotEnoughBalanceException.class,
            LimitOnTransactionExceededException.class,
            InvalidAccountCurrenyOnTransactionException.class})
    public ResponseEntity<Map<String,String>> handleBadRequestExceptions(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccountStatusException.class)
    public ResponseEntity<Map<String,String>> handleForbiddenException(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String,String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", "Please enter right format of input");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> handleIllegalArgumentException(IllegalArgumentException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String,String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", "Input not available or not in the right format");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    // handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,String>> handleUnknownException(Exception e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("errorMessage", e.getClass().getSimpleName());
        errorResponse.put("message", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
}
