package com.example.todo.web;

import com.example.todo.TodoWebProjectApplication;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TodoWebProjectApplication.class)
@WebAppConfiguration
public class UserControllerTestIT {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testGetRegistration() throws Exception {
        mockMvc.perform(get("/register")).andDo(print())
                .andExpect(view().name("user/register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testPostRegisterWithInvalidUserEmail() throws Exception {
        mockMvc.perform(post("/register").param("email", "badEmail").param("password", "notEmpty")).andDo(print())
                .andExpect(view().name("/user/register"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    public void testPostRegisterWithEmptyPassword() throws Exception {
        mockMvc.perform(post("/register").param("email", "badEmail@com.ru").param("password", "")).andDo(print())
                .andExpect(view().name("/user/register"))
                .andExpect(model().attributeHasFieldErrors("user", "password"));
    }

    @Test
    public void testPostRegisterWithSameEmail() throws Exception {
        // user with email demo@user.com is provided by ApplicationInitializer class
        mockMvc.perform(post("/register").param("email", "demo@user.com").param("password", "notEmpty")).andDo(print())
                .andExpect(view().name("/user/register"))
                .andExpect(model().attributeHasFieldErrors("user", "email"));
    }

    @Test
    public void testPostRegister() throws Exception {
        mockMvc.perform(post("/register").param("email", "demo1@user.com").param("password", "notEmpty")).andDo(print())
                .andExpect(redirectedUrl("/welcome"));
    }
}