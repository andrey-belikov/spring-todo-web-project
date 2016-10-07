package com.example.todo.service;

import com.example.todo.data.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Current logged in user (added to all controllers).
 */
@Service
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@ControllerAdvice
public class ActiveUser {
	private User user = null;
	
	@ModelAttribute("activeUser")
    public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
}
