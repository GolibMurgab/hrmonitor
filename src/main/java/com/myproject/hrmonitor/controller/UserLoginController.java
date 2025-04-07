package com.myproject.hrmonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/login")
public class UserLoginController {
    @GetMapping
    public String showLoginForm(
            @RequestParam(value = "error", required = false) boolean error,
            Model model
    ) {
        if (error) {
            model.addAttribute("errorMessage", "Неверный логин или пароль");
        }
        return "login";
    }
}
