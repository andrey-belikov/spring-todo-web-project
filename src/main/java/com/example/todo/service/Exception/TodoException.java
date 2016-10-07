package com.example.todo.service.Exception;

/**
 * Todo specific exceptions.
 */
public class TodoException extends RuntimeException {
    public TodoException(String message) {
        super(message);
    }
}
