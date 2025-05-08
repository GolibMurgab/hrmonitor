package com.myproject.hrmonitor.controller;

import com.myproject.hrmonitor.dto.StatisticsDto;
import com.myproject.hrmonitor.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hr/statistics")
public class HrDashboardController {
    @Autowired
    private HrService hrService;

    @GetMapping
    public String getStatisticsPage(Model model, Authentication authentication) {
        StatisticsDto stats = hrService.getStatistics(authentication.getName());
        model.addAttribute("stats", stats);
        return "statistics";
    }
}
