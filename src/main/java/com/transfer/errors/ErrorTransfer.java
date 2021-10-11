package com.transfer.errors;

public class ErrorTransfer extends RuntimeException {
    public ErrorTransfer(String message) {
        super(message);
    }
}
