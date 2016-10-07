package com.example.todo.security;

import com.example.todo.data.model.User;
import com.example.todo.data.repository.UserRepository;
import com.example.todo.service.ActiveUser;
import com.example.todo.service.Exception.UserAuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User authentication.
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    ActiveUser activeUser;
    @Autowired
    UserRepository userRepository;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (Objects.isNull(authentication.getName()) || Objects.isNull(authentication.getCredentials()))
            throw new UserAuthenticationException(String.format("null name=%s or password=%s",
            		authentication.getName(), authentication.getCredentials()));

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> user = userRepository.findByEmailIgnoreCase(name);

        if (!user.isPresent())
            throw new UserAuthenticationException(String.format("User with email-%s can't be found", name));

        if (!user.get().getPassword().equalsIgnoreCase(password))
            throw new UserAuthenticationException(String.format("User password with email=%s is not same", name));

        activeUser.setUser(user.get());

        List<GrantedAuthority> grantedAuths = new ArrayList<>();
        grantedAuths.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ALL";
            }
        });

        return new UsernamePasswordAuthenticationToken("user", "password", grantedAuths);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
