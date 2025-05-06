package com.myproject.hrmonitor.controller;

import com.myproject.hrmonitor.dto.ResumeDto;
import com.myproject.hrmonitor.dto.VacancyDto;
import com.myproject.hrmonitor.entity.Stage;
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
                            @RequestParam(required = false) Stage stage,
                            @RequestParam(required = false) Long vacancyId,
                            @RequestParam(defaultValue = "created-desc") String sortBy,
                            Authentication authentication) {
        model.addAttribute("vacancies", resumeService.getAllVacancies(authentication.getName()));
        model.addAttribute("resumeDto", new ResumeDto());
        model.addAttribute("resumes",
                resumeService.getResumesFiltered(authentication.getName(), stage, vacancyId, sortBy));
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

    @PutMapping("/{id}")
    public String editResume(@PathVariable Long id,
                      @ModelAttribute ResumeDto resumeDto){
        resumeService.editResume(id, resumeDto);
        return "redirect:/hr/resume";
    }

    @DeleteMapping("/{id}")
    public String deleteVacancy(@PathVariable Long id){
        resumeService.deleteResume(id);
        return "redirect:/hr/resume";
    }
}
