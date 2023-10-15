package com.example.transactionswithspring.exception;

public class DuplicateItemNameException extends RuntimeException {

    public DuplicateItemNameException(String message) {
        super(message);
    }

}
