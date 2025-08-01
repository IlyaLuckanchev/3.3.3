package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.RoleRepository;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/user")
    public String getAllUsers(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByName(currentUsername).orElse(null);

        if (currentUser == null) {
            return "redirect:/logout";
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.getUser());
        model.addAttribute("isFormMode", false);
        return "user";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isFormMode", true);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("user") User user) {
        List<String> roleNames = user.getRoleNames();
        if (roleNames == null || roleNames.isEmpty()) {
            roleNames = List.of("ROLE_USER");
        }

        Set<Role> roles = roleNames.stream()
                .map(roleRepository::findByName)
                .collect(Collectors.toSet());

        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.addUser(user);

        return "redirect:/user";
    }

    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/user?error=user_not_found";
        }
        model.addAttribute("user", user);
        model.addAttribute("isFormMode", true);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "user";
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(
            @PathVariable Long id,
            @ModelAttribute("user") User user) {

        User existingUser = userService.getUserById(id);

        List<String> roleNames = user.getRoleNames();
        if (roleNames == null || roleNames.isEmpty()) {
            roleNames = List.of("ROLE_USER");
        }

        Set<Role> roles = roleNames.stream()
                .map(roleRepository::findByName)
                .collect(Collectors.toSet());

        existingUser.setRoles(roles);
        existingUser.setName(user.getName());
        existingUser.setSurName(user.getSurName());
        existingUser.setAge(user.getAge());
        existingUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userService.updateUser(existingUser);
        return "redirect:/user";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }
}
