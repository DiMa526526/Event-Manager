package ru.Diana.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.Diana.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public SimpleExceptionResponse exception(ResourceNotFoundException e) {
        return new SimpleExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public SimpleExceptionResponse exception(Exception e) {
        return new SimpleExceptionResponse(e.getMessage());
    }

    public static class SimpleExceptionResponse {
        private final String message;

        public SimpleExceptionResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}