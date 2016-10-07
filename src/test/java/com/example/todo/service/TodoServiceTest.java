package com.example.todo.service;

import com.example.todo.data.model.Status;
import com.example.todo.data.model.Todo;
import com.example.todo.data.model.User;
import com.example.todo.data.repository.TodoRepository;
import com.example.todo.service.Exception.TodoException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class TodoServiceTest {
    TodoService cut;
    private static final String USER_EMAIL= "demo@mail.com";

    @Mock
    TodoRepository todoRepository;

    @Mock
    ActiveUser activeUser;
    User currentUser;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        cut = new TodoService();
        cut.todoRepository = todoRepository;
        cut.activeUser = activeUser;

        currentUser = new User();
        currentUser.setEmail(USER_EMAIL);
        currentUser.setPassword("123");
        currentUser.setId(1l);
    }

    @Test
    public void findAllByActiveUser() {
        when(activeUser.getUser()).thenReturn(currentUser);
        when(todoRepository.findByUser(currentUser)).thenReturn(new ArrayList<Todo>());

        cut.findAllByActiveUser();

        verify(activeUser, times(1)).getUser();
        verify(todoRepository, times(1)).findByUser(currentUser);
    }

    @Test(expected = TodoException.class)
    public void findByIdAndActiveUserExpectExceptionNotFoundTodoByIDAndUser() throws Exception {
        when(activeUser.getUser()).thenReturn(currentUser);
        when(todoRepository.findByUserAndId(currentUser, 1L)).thenReturn(Optional.empty());

        cut.findByIdAndActiveUser(1l);
    }

    @Test
    public void findByIdAndActiveUser() throws Exception {
        when(activeUser.getUser()).thenReturn(currentUser);
        when(todoRepository.findByUserAndId(currentUser, 1L)).thenReturn(Optional.of(new Todo()));

        cut.findByIdAndActiveUser(1l);

        verify(activeUser, times(1)).getUser();
        verify(todoRepository, times(1)).findByUserAndId(currentUser, 1l);
    }

    @Test
    public void testUpdateTodo() {
        Todo allFilled = new Todo();
        allFilled.setId(2l);
        allFilled.setName("filled");
        allFilled.setDescription("filledDescription");
        allFilled.setStatus(Status.ACTIVE);

        Todo toUpdate = new Todo();
        toUpdate.setName("toUpdate");
        toUpdate.setDescription("toUpdate");
        toUpdate.setStatus(Status.COMPLETE);

        when(activeUser.getUser()).thenReturn(currentUser);
        when(todoRepository.findByUserAndId(currentUser, 2L)).thenReturn(Optional.of(allFilled));
        when(todoRepository.save(allFilled)).thenReturn(allFilled);

        Todo afterUpdate = cut.update(toUpdate, 2l);

        assertEquals(toUpdate.getName(), afterUpdate.getName());
        assertEquals(toUpdate.getDescription(), afterUpdate.getDescription());
        assertEquals(toUpdate.getStatus(), afterUpdate.getStatus());
        assertEquals(afterUpdate.getId(), 2l, 0);

        verify(activeUser, times(1)).getUser();
        verify(todoRepository, times(1)).findByUserAndId(currentUser, 2l);
        verify(todoRepository, times(1)).save(afterUpdate);
    }

    @Test
    public void testCreateTodo() {
        Todo allFilled = new Todo();
        allFilled.setId(2l);
        allFilled.setName("filled");
        allFilled.setDescription("filledDescription");
        allFilled.setStatus(Status.ACTIVE);

        when(activeUser.getUser()).thenReturn(currentUser);
        when(todoRepository.save(allFilled)).thenReturn(allFilled);

        Todo afterCreated = cut.create(allFilled);

        assertEquals(afterCreated.getUser(), currentUser);
        assertNull(afterCreated.getId());

        verify(activeUser, times(1)).getUser();
        verify(todoRepository, times(1)).save(afterCreated);
    }
}