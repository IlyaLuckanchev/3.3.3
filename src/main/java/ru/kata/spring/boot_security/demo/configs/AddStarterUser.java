package ru.kata.spring.boot_security.demo.configs;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.Set;

@Component
public class AddStarterUser {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AddStarterUser(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @PostConstruct
    @Transactional
    public void init() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }

        Role userRole = roleRepository.findByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        Optional<User> admin = userRepository.findByEmail("111@mail.ru");
            User admin1 = admin.orElse(new User());
            admin1.setName("admin");
            admin1.setSurName("admin");
            admin1.setEmail("111@mail.ru");
            admin1.setAge(25);
            admin1.setPassword(passwordEncoder.encode("admin"));
            admin1.setRoles(Set.of(adminRole, userRole));
            userRepository.save(admin1);

    }
}
