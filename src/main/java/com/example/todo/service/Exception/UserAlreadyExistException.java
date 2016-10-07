package com.example.todo.service.Exception;

/**
 * User with email already exist, exceptions.
 */
public class UserAlreadyExistException extends UserException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
