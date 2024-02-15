package com.example.blog.service;

import com.example.blog.model.User;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    private final IUserService userService;
    private final HttpSession session;

    public CustomUserDetailsService(IUserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("LoadUserByUsername");
        log.info("Username: {}", username);
        Optional<User> optionalUser = userService.findByUsername(username);
        if(optionalUser.isPresent()){
            log.info("User found");
            User user = optionalUser.get();
            session.setAttribute("idUser", user.getId());
            log.info("idSession: {}", session.getAttribute("idUser"));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getType())
                    .build();
        } else {
            log.error("user not found");
            String errorMessage = "Incorrect user or password";
            session.setAttribute("errorMessage", errorMessage);
            throw new UsernameNotFoundException("User not found");
        }
    }
}
