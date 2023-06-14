package com.gni.banking.Exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LimitOnTransactionExceededException extends Exception{

    public LimitOnTransactionExceededException(String message){
        super(message);
    }
}
