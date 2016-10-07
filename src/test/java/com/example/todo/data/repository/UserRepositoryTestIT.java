package com.example.todo.data.repository;

import com.example.todo.JpaConfiguration;
import com.example.todo.data.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaConfiguration.class)
public class UserRepositoryTestIT {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TodoRepository todoRepository;

    User testUser;

    private static final String userEmail = "test@email.com";

    @Before
    public void onInit() {
        testUser = new User();
        testUser.setEmail(userEmail);
        testUser.setPassword("passwd");
    }

    @Test
    @Transactional
    public void testShouldFindUserByEmail() {
        userRepository.save(testUser);

        Optional<User> fromDB = userRepository.findByEmailIgnoreCase(userEmail);

        assertEquals(fromDB.isPresent(), true);
        assertEquals(fromDB.get().getPassword(), "passwd");
    }

    @Test
    @Transactional
    public void testShouldSetSinceCorrectly() {
        userRepository.save(testUser);

        Optional<User> fromDB = userRepository.findByEmailIgnoreCase(userEmail);

        assertEquals(fromDB.isPresent(), true);
        assertNotNull(fromDB.get().getSince());
    }

    @Test(expected = TransactionSystemException.class)
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // force flush
    public void testCantSaveUserWithBadEmail() {
        User badUserExample = new User();
        badUserExample.setEmail("badEmail");
        userRepository.save(badUserExample);
    }

    @Test(expected = TransactionSystemException.class)
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // force flush
    public void testCantSaveUserWithEmptyPassword() {
        User badUserExample = new User();
        badUserExample.setEmail("email@mail.com");
        badUserExample.setPassword("");
        userRepository.save(badUserExample);
    }
}