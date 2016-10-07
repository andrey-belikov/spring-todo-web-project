package com.example.todo.web;

import com.example.todo.TodoWebProjectApplication;
import com.example.todo.data.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TodoWebProjectApplication.class)
@WebAppConfiguration
public class IndexControllerTestIT {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    UserRepository userRepository;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetWelcomePage() throws Exception {
        mockMvc.perform(get("/welcome")).andDo(print())
                .andExpect(view().name("index"));
    }

    @Test
    public void testGetAboutPage() throws Exception {
        mockMvc.perform(get("/about")).andDo(print())
                .andExpect(view().name("about"));
    }

    @Test
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login")).andDo(print())
                .andExpect(view().name("login"));
    }
}