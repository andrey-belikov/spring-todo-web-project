package com.example.todo.data.repository;

import com.example.todo.JpaConfiguration;
import com.example.todo.data.model.Todo;
import com.example.todo.data.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JpaConfiguration.class)
public class TodoRepositoryTestIT {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TodoRepository todoRepository;

    User testUser1, testUser2;

    Todo todo1user1, todo2user2, todo3user2;

    private static final String todo1user1Description = "User 1 Description";
    private static final String todo1user1Name = "User 1 Todo 1";
    private static final String user1Email = "new@mail.com";
    private static final String user2Email = "test2@user.com";

    @Before
    public void onInit() {
        initUser1();
        initUser2();
    }

    @Test
    @Transactional
    public void testFindByUser() {
        Optional<User> user1FromDB = userRepository.findByEmailIgnoreCase(user1Email);
        assertTrue(user1FromDB.isPresent());

        List<Todo> todoList = todoRepository.findByUser(user1FromDB.get());

        assertNotNull(todoList);
        assertEquals(todoList.size(), 1);
        assertThat(todoList, hasItem(todo1user1));
    }

    @Test
    @Transactional
    public void testShouldCorrectlyPersistUserWithTodoAndFindByEmail() {
        Optional<User> user2 = userRepository.findByEmailIgnoreCase(user2Email);

        assertTrue(user2.isPresent());
        assertEquals(user2.get().getTodos().size(), 2);
        assertThat(user2.get().getTodos(), hasItems(todo2user2, todo3user2));
    }

    @Test
    @Transactional
    public void testShouldFindTodoByUserAndId() {
        Optional<User> user2 = userRepository.findByEmailIgnoreCase(user2Email);
        assertTrue(user2.isPresent());

        Long todosId = user2.get().getTodos().get(1).getId();
        String todosName = user2.get().getTodos().get(1).getName();

        Optional<Todo> todoFromDB = todoRepository.findByUserAndId(user2.get(), todosId);

        assertTrue(todoFromDB.isPresent());
        assertEquals(todoFromDB.get().getId(), todosId);
        assertEquals(todoFromDB.get().getName(), todosName);
    }

    private void initUser2() {
        User testUser2 = new User();
        testUser2.setEmail(user2Email);
        testUser2.setPassword("123");

        todo2user2 = new Todo();
        todo2user2.setDescription("User 2 Description, Todo 2");
        todo2user2.setName("User 2 Description, Todo 2");

        todo3user2 = new Todo();
        todo3user2.setDescription("User 2 Description, Todo 3");
        todo3user2.setName("User 2 Description, Todo 3");

        testUser2.addTodo(todo2user2);
        testUser2.addTodo(todo3user2);

        userRepository.save(testUser2);
    }

    private void initUser1() {
        User testUser1 = new User();
        testUser1.setEmail(user1Email);
        testUser1.setPassword("123");

        todo1user1 = new Todo();
        todo1user1.setDescription(todo1user1Description);
        todo1user1.setName(todo1user1Name);

        testUser1.addTodo(todo1user1);

        userRepository.save(testUser1);
    }
}