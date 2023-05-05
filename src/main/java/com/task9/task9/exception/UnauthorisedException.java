package com.task10.task10.exception;

import java.time.LocalDate;

public class UnauthorisedException extends RuntimeException{
    public UnauthorisedException(String message) {
        super(message);
    }
}
