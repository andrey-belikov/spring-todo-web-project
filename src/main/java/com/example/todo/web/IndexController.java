package com.example.todo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Index Controller.
 */
@Controller
public class IndexController {
	@RequestMapping(value = { "/", "/home", "/welcome" }, method = RequestMethod.GET)
	public String getIndexPage() {

		return "index";
	}

	@RequestMapping(value = { "/about" }, method = RequestMethod.GET)
	public String getAboutPage() {

		return "about";
	}

	@RequestMapping(value = { "/login" }, method = RequestMethod.GET)
	public String getLoginPage() {

		return "login";
	}

}
