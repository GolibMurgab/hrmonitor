package com.myproject.hrmonitor.controller;

import com.myproject.hrmonitor.dto.HrDto;
import com.myproject.hrmonitor.dto.SlaDto;
import com.myproject.hrmonitor.dto.StatisticsDto;
import com.myproject.hrmonitor.entity.Stage;
import com.myproject.hrmonitor.service.HrService;
import com.myproject.hrmonitor.service.SlaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/team-lead-hr")
public class TeamLeadHrDashboardController {
    @Autowired
    private HrService hrService;

    @Autowired
    private SlaService slaService;

    @GetMapping("/dashboard")
    public String showMyHr(Model model, Authentication authentication){
        model.addAttribute("hrDto", new HrDto());
        model.addAttribute("freeHrs", hrService.getAwailableHr());
        model.addAttribute("hrs", hrService.getMyHr(authentication.getName()));
        return "myhr";
    }

    @PostMapping("add-hr")
    public String addNewHr(@ModelAttribute HrDto hrDto,
                           Authentication authentication){
        hrService.addHr(hrDto, authentication.getName());
        return "redirect:/team-lead-hr/dashboard";
    }

    @GetMapping("/sla")
    public String slaSettings(Model model) {
        model.addAttribute("slaDto", new SlaDto());
        model.addAttribute("slas", slaService.getAllSla());
        return "sla";
    }

    @PostMapping("/sla")
    public String saveSla(@ModelAttribute SlaDto slaDto) {
        slaService.save(slaDto);
        return "redirect:/team-lead-hr/sla";
    }

    @GetMapping("/statistics")
    public String showStatistics(@RequestParam(required = false) String username,
                                 Model model, Authentication authentication){

        if (username != null && !username.isEmpty()) {
            model.addAttribute("stats", hrService.getStatistics(username));
        } else {
            model.addAttribute("stats", new StatisticsDto());
        }
        model.addAttribute("hrDto", new HrDto());
        model.addAttribute("hrs", hrService.getMyHr(authentication.getName()));
        return "statisticsT";
    }

    @PostMapping("/showStat")
    public String getStatistics(@ModelAttribute HrDto hrDto,
                                RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("username", hrDto.getUsername());
        return "redirect:/team-lead-hr/statistics";
    }
}
