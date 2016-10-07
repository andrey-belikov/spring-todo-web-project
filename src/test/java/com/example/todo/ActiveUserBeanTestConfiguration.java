package com.example.todo;

import com.example.todo.data.model.User;
import com.example.todo.service.ActiveUser;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

/**
 * Custom test configuration for ActiveUser bean (Singleton)
 */
@Configuration
public class ActiveUserBeanTestConfiguration {
    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ActiveUser getActiveUserForTest() {
        User demoUser = new User();
        demoUser.setId(1l);
        demoUser.setEmail("demo@user.com");
        demoUser.setPassword("123");

        ActiveUser activeUser = new ActiveUser();
        activeUser.setUser(demoUser);

        return activeUser;
    }
}
