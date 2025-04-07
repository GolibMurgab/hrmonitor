package com.myproject.hrmonitor.controller;

import com.myproject.hrmonitor.dto.UserRegisterDto;
import com.myproject.hrmonitor.repository.UserRepository;
import com.myproject.hrmonitor.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {
    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegisterDto userRegistrationDto(){
        return new UserRegisterDto();
    }

    @GetMapping
    public String showRegistrationForm(){
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") UserRegisterDto uRegDto,
                                      Model model
    ){
        if(!userService.save(uRegDto)){
            model.addAttribute("errorMessage", "Имя пользователя занято");
            return "registration";
        }
        return "redirect:/login";
    }
}
