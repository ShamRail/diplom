package ru.ugasu.app.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${ADMIN_LOGIN}")
    private String adminLogin;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        if (!adminLogin.equals(login)) {
            throw new UsernameNotFoundException(login);
        }
        return new User(login, adminPassword, Collections.emptyList());
    }

}
