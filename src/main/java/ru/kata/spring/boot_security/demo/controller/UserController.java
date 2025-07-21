package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.repository.User;
import ru.kata.spring.boot_security.demo.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
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
    public String addUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/user";
    }

    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("isFormMode", true);
        return "user";
    }

    @PostMapping("/admin/edit/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user) {
        userService.updateUser(user.getName(), user.getSurName(), id);
        return "redirect:/user";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }
}
