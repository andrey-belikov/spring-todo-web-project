package com.example.todo.service;

import com.example.todo.data.model.User;
import com.example.todo.data.repository.UserRepository;
import com.example.todo.service.Exception.UserAlreadyExistException;
import com.example.todo.service.Exception.UserException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserServiceTest {
    UserService cut;
    User  userWithId, userWithoutId;

    private static final String USER_EMAIL= "demo@mail.com";

    @Mock
    UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        cut = new UserService();
        cut.userRepository = userRepository;

        userWithId = new User();
        userWithId.setEmail(USER_EMAIL);
        userWithId.setPassword("123");
        userWithId.setId(1l);

        userWithoutId = new User();
        userWithoutId.setEmail(USER_EMAIL);
        userWithoutId.setPassword("123");
    }

    @Test(expected = UserException.class)
    public void shouldNotProcessUserWithId() throws UserException {
        cut.register(userWithId);
    }

    @Test(expected = UserAlreadyExistException.class)
    public void shouldNotProcessUserWithExistingEmail() throws UserException {
        // create situation: user with email is in db
        when(userRepository.findByEmailIgnoreCase(USER_EMAIL)).thenReturn(Optional.of(userWithoutId));

        // test
        cut.register(userWithoutId);
    }

    @Test
    public void shouldRegisterCorrectUser() throws UserException {
        when(userRepository.findByEmailIgnoreCase(USER_EMAIL)).thenReturn(Optional.<User>empty());

        cut.register(userWithoutId);

        verify(userRepository, times(1)).save(userWithoutId);
    }
}