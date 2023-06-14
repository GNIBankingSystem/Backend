package com.gni.banking.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAccountCurrenyOnTransactionException extends Exception{

        public InvalidAccountCurrenyOnTransactionException(String message){
            super(message);
        }
}
