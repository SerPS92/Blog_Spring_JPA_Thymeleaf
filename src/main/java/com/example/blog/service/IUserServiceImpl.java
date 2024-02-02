package com.example.blog.service;

import com.example.blog.model.User;
import com.example.blog.repository.IUserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IUserServiceImpl implements IUserService {

    private final IUserRepo userRepo;

    public IUserServiceImpl(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public Optional<User> findById(Integer id) {
         return userRepo.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public void deleteById(Integer id) {
        userRepo.deleteById(id);
    }

    @Override
    public void update(User user) {
        userRepo.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepo.findAll(pageable);
    }
}
