package com.example.todo.web;

import com.example.todo.data.model.User;
import com.example.todo.service.Exception.UserAlreadyExistException;
import com.example.todo.service.Exception.UserException;
import com.example.todo.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * User controller (just registration for now).
 */
@Controller
@Log
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping(value="/register", method= RequestMethod.GET)
    public String createNewUser(Model model) {
        model.addAttribute("user", new User());

        return "user/register";
    }

    @RequestMapping(value="/register", method= RequestMethod.POST)
    public String saveNewUser(Model model, @Valid User newUser, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                log.severe("Registration error. " + error);
            }
            return "/user/register";
        }

        try {
            userService.register(newUser);
        } catch (UserAlreadyExistException e) {
            String rejectMessage = String.format("User with email %s is already registered", newUser.getEmail());

            result.rejectValue("email","", rejectMessage);

            log.severe(rejectMessage);

            return "/user/register";
        } catch (UserException e) {
            log.severe(e.getMessage());
        }

        return "redirect:/welcome";
    }
}
