package ru.kata.spring.boot_security.demo.userDaoImpl;

import ru.kata.spring.boot_security.demo.repository.User;
import java.util.List;

public interface UserDAO {
    List<User> getUser();
    User addUser(User user);
    void deleteUser(Long id);
    void updateUser(String name, String surName, Long id);
    User getUserById(Long id);
}
