package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.repository.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getUser();
    void addUser(User user);
    void deleteUser(Long id);
    void updateUser(String name, String surName, Long id);
    User getUserById(Long id);
}
