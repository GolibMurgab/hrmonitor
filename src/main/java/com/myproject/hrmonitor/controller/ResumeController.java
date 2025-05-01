package com.myproject.hrmonitor.controller;

import com.myproject.hrmonitor.dto.ResumeDto;
import com.myproject.hrmonitor.service.HrService;
import com.myproject.hrmonitor.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/hr/resume")
public class ResumeController {

    @Autowired
    private ResumeService resumeService;

    @Autowired
    private HrService hrService;

    @GetMapping
    public String getResume(Model model,
                            Authentication authentication){
        model.addAttribute("vacancies",
                (resumeService.getAllVacancies(authentication.getName())));
        model.addAttribute("resumeDto", new ResumeDto());
        model.addAttribute("resumes", resumeService.getResumes(authentication.getName()));
        return "myresume";
    }

    @PostMapping("/add")
    public String addResume(@ModelAttribute ResumeDto resumeDto,
                            Authentication authentication){
        resumeService.save(resumeDto, authentication.getName());
        return "redirect:/hr/resume";
    }

    @PostMapping("/{id}/next-stage")
    public String changeStage(@PathVariable Long id){
        resumeService.changeStage(id);
        return "redirect:/hr/resume";
    }
}
