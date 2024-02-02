package com.example.blog.service;

import com.example.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUserService {

    Optional<User> findById(Integer id);
    Optional<User> findByUsername(String username);
    User save(User user);
    void deleteById(Integer id);
    void update(User user);
    Page<User> findAll(Pageable pageable);
}
