package com.example.todo.web;

import com.example.todo.TodoWebProjectApplication;
import com.example.todo.data.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TodoWebProjectApplication.class)
@WebAppConfiguration
public class TodoControllerTestIT {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    TodoRepository todoRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetTodos() throws Exception {
        mockMvc.perform(get("/todo")).andDo(print())
                .andExpect(view().name("/todo/list"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attribute("todos", hasSize(2)));
    }

    @Test
    public void testGetTodo() throws Exception {
        Long id = todoRepository.findAll().get(0).getId();
        mockMvc.perform(get("/todo/"+id)).andDo(print())
                .andExpect(view().name("/todo/todo"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attribute("todo", hasProperty("name", notNullValue())));
    }

    @Test
    public void testGetNewTodo() throws Exception {
        mockMvc.perform(get("/todo/new")).andDo(print())
                .andExpect(view().name("/todo/todo"))
                .andExpect(model().attributeExists("todo"))
                .andExpect(model().attribute("todo", hasProperty("id", nullValue())));
    }

    @Test
    public void testCreateNewTodo() throws Exception {
        mockMvc.perform(post("/todo").param("name", "Short name").param("description", "Short description").param("status", "PAUSE")).andDo(print())
                .andExpect(redirectedUrl("/todo"));

        mockMvc.perform(get("/todo")).andDo(print())
                .andExpect(view().name("/todo/list"))
                .andExpect(model().attributeExists("todos"))
                .andExpect(model().attribute("todos", hasSize(3)));
    }

    @Test
    public void testUpdateTodo() throws Exception {
        Long id = todoRepository.findAll().get(0).getId();

        mockMvc.perform(post("/todo/"+id).param("name", "Short name").param("description", "Short description").param("status", "PAUSE")).andDo(print())
                .andExpect(redirectedUrl("/todo"));

        mockMvc.perform(get("/todo/"+id)).andDo(print())
                .andExpect(view().name("/todo/todo"))
                .andExpect(model().attribute("todo", hasProperty("name", equalToIgnoringCase("Short name"))));
    }

    @Test
    public void testShouldDisplayErrorOrNotFoundTodo() throws Exception {
        mockMvc.perform(get("/todo/505")).andExpect(view().name("error")).andExpect(model().attributeExists("error"));
    }
}