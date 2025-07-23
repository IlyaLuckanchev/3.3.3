package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.repository.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.User;
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
        model.addAttribute("users", userService.getUser());
        model.addAttribute("isFormMode", false);
        return "user";
    }

    @GetMapping("/admin/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("isFormMode", true);
        return "user";
    }

    @PostMapping("/admin/add")
    public String addUser(@ModelAttribute("user") User user, List<String> roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = roleName.stream()
                .map(name -> roleRepository.findByName(name))
                .collect(Collectors.toSet());
        user.setRoles(roles);
        userService.addUser(user);
        return "redirect:/user";
    }

    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/user";
        }
        model.addAttribute("user", user);
        model.addAttribute("isFormMode", true);
        return "user";
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, List<String> roleName) {
        User findUser = userService.getUserById(id);
        Set<Role> roles = roleName.stream()
                .map(name -> roleRepository.findByName(name))
                .collect(Collectors.toSet());
        findUser.setRoles(roles);
        findUser.setName(user.getName());
        findUser.setSurName(user.getSurName());
        userService.updateUser(findUser);
        return "redirect:/user";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }
}
