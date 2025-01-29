package com.fawez.ebankingbackend.exception;

public class BalanceNotSufficientException extends Throwable {
    public BalanceNotSufficientException(String msg) {
        super(msg);
    }
}
