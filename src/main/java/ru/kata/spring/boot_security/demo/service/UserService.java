package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.repository.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getUser();
    User addUser(User user);
    void deleteUser(Long id);
    void updateUser(User user);
    User getUserById(Long id);
    User findByName(String name);
}
