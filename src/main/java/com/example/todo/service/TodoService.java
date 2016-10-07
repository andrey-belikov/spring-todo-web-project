package com.example.todo.service;

import com.example.todo.data.model.Todo;
import com.example.todo.data.repository.TodoRepository;
import com.example.todo.service.Exception.TodoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

/**
 * Todo's service.
 */
@Service
public class TodoService {
    @Autowired
    TodoRepository todoRepository;

    /**
     * Logged in user.
     */
    @Autowired
    ActiveUser activeUser;

    /**
     * Find all todo's for logged in user.
     * @return todo's list
     */
    public List<Todo> findAllByActiveUser() {
        return todoRepository.findByUser(activeUser.getUser());
    }

    /**
     * Find by Id and logged in user
     * @param id todo's id
     * @return todo if any
     */
    public Todo findByIdAndActiveUser(Long id) {
        Optional<Todo> todoFromDB = todoRepository.findByUserAndId(activeUser.getUser(), id);

        todoFromDB.orElseThrow(() -> new TodoException(String.format("Todo with id=%d can't be found for user %s",
                id, activeUser.getUser().getEmail())));
        if(todoFromDB.isPresent())
            return todoFromDB.get();

        return null;
    }

    /**
     * Update existing todo
     * @param todo (skip user, id fields)
     * @return updated todo
     */
    public Todo update(Todo todo, Long id) {
        Todo fromDB = findByIdAndActiveUser(id);

        ensureTodoNameIsNotEmpty(todo);

        fromDB.setName(todo.getName());
        fromDB.setDescription(todo.getDescription());
        fromDB.setStatus(todo.getStatus());

        return todoRepository.save(fromDB);
    }

    /**
     * Create new Todo for current user
     * @param todo - new todo
     * @return saved todo
     */
    public Todo create(Todo todo) {
        todo.setId(null);
        ensureTodoNameIsNotEmpty(todo);

        activeUser.getUser().addTodo(todo);

        return todoRepository.save(todo);
    }

    private void ensureTodoNameIsNotEmpty(Todo todo) {
        if(isNull(todo.getName()) || todo.getName().trim().isEmpty())
                todo.setName("New Todo");
    }
}
