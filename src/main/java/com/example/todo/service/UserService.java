package com.example.todo.service;

import com.example.todo.data.model.User;
import com.example.todo.data.repository.UserRepository;
import com.example.todo.service.Exception.UserAlreadyExistException;
import com.example.todo.service.Exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.nonNull;

/**
 * Users service.
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    /**
     * Register new user.
     * @param newUser details.
     * @return user with id.
     */
    public User register(User newUser) throws UserException {
        if(nonNull(newUser.getId()))
            throw new UserException("User Id should be empty");

        // check user existing email
        checkExistingUserEmail(newUser);

        userRepository.save(newUser);

        // call notification service for greeting email

        return newUser;
    }

    /**
     * Change user password
     * @param user for password modification
     * @param newPassword new password
     * @return user with new password
     */
    public User changePassword(User user, String newPassword) {
        // check password
        // generated protected hash

        user.setPassword(newPassword);
        userRepository.save(user);

        // call notification service for password change email

        return user;
    }

    private void checkExistingUserEmail(User user) throws UserAlreadyExistException {
        Optional<User> userFromDB = userRepository.findByEmailIgnoreCase(user.getEmail());
        if(userFromDB.isPresent())
            throw new UserAlreadyExistException(String.format("User with email %s already registered", user.getEmail()));
    }
}
