package com.example.todo.web;

import com.example.todo.service.ActiveUser;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * Application error controller
 */
@ControllerAdvice
@Controller
@Log
public class ApplicationErrorController implements ErrorController {
    public static final String DEFAULT_ERROR_VIEW = "error";

    @Autowired
    ActiveUser activeUser;

    /**
     * Handler for all application exception
     * @param req Request
     * @param e Exception
     * @return model with error attribute
     * @throws Exception
     */
    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.severe(activeUser.getUser().getEmail() + " " + e.getMessage());

        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);

        if(Objects.nonNull(activeUser.getUser()))
            mav.addObject("activeUser", activeUser.getUser());

        mav.addObject("error", e.getMessage());

        return mav;
    }

    @Override
    public String getErrorPath() {
        return DEFAULT_ERROR_VIEW;
    }

    /**
     * Return error page of exception
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/error")
    public String handle(HttpServletRequest request, Model model) {
        String statusCode = Objects.nonNull(request.getAttribute("javax.servlet.error.status_code")) ? request.getAttribute("javax.servlet.error.status_code").toString() : "";
        String exception = Objects.nonNull(request.getAttribute("javax.servlet.error.exception")) ? request.getAttribute("javax.servlet.error.exception").toString() : "";
        String errorMessage = Objects.nonNull(request.getAttribute("javax.servlet.error.message")) ? request.getAttribute("javax.servlet.error.message").toString() : "";

        if(Objects.nonNull(activeUser.getUser()))
            model.addAttribute("activeUser", activeUser.getUser());

        model.addAttribute("error", statusCode + " " + exception + " " + errorMessage);

        return "error";
    }
}
