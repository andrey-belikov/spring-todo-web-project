package com.example.todo.web;

import com.example.todo.data.model.Todo;
import com.example.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Todo Controller.
 */
@Controller
public class TodoController {
	@Autowired
    TodoService todoService;

	@RequestMapping(value="/todo", method=RequestMethod.GET)
	public String getTodos(Model model) {
        model.addAttribute("todos", todoService.findAllByActiveUser());

		return "/todo/list";
	}

	@RequestMapping(value="/todo/{id}", method=RequestMethod.GET)
	public String editTodo(Model model, @PathVariable("id") Long id) {
		model.addAttribute("todo", todoService.findByIdAndActiveUser(id));

        return "/todo/todo";
	}

	@RequestMapping(value="/todo/new", method=RequestMethod.GET)
	public String getNewTodo(Model model) {
		model.addAttribute("todo", new Todo());

        return "/todo/todo";
	}

	@RequestMapping(value="/todo", method=RequestMethod.POST)
	public String createNewTodo(Todo todo) {
		todoService.create(todo);

        return "redirect:/todo";
	}

	@RequestMapping(value="/todo/{id}", method=RequestMethod.POST)
	public String updateTodo(Todo todo, @PathVariable("id") Long id) {
		todoService.update(todo, id);

        return "redirect:/todo";
	}

}
