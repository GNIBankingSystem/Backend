package com.gni.banking.Exceptions.Handler;

import com.gni.banking.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({InvalidAccountTypeOnTransactionException.class,
            NotEnoughBalanceException.class,
            LimitOnTransactionExceededException.class,
            InvalidAccountCurrenyOnTransactionException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidAccountStatusException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.FORBIDDEN, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    // handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknownException(Exception e) {
        ErrorResponse errorResponse = ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
