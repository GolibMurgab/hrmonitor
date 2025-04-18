package com.myproject.hrmonitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/team-lead-hr/dashboard")
public class TeamleadHrDashboardController {
    @GetMapping
    public String showMainMenu(){
        return "index";
    }
}
