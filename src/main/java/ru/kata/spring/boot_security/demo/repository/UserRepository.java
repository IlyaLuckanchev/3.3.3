package ru.kata.spring.boot_security.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
    public interface UserRepository extends JpaRepository<User, Long> {
        User findByName(String name);

}
