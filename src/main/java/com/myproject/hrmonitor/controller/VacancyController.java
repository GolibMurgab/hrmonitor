package com.myproject.hrmonitor.controller;

import com.myproject.hrmonitor.dto.VacancyDto;
import com.myproject.hrmonitor.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/team-lead-hr/vacancies")
public class VacancyController {
    @Autowired
    private VacancyService vacancyService;


    @GetMapping()
    public String showVacancies(@RequestParam(value = "error", required = false) boolean error,
                                Authentication authentication,
            Model model) {
        model.addAttribute("vacancyDto", new VacancyDto());
        model.addAttribute("vacancies",
                (vacancyService.getAllVacanciesDto(authentication.getName())));
        return "vacancies";
    }

    @PostMapping("/add")
    public String createVacancy(@ModelAttribute VacancyDto vacancy,
                                Authentication authentication) {
        vacancyService.createVacancy(vacancy, authentication.getName());

        return "redirect:/team-lead-hr/vacancies";
    }

    @PutMapping("/{id}")
    public String updateVacancy(@PathVariable Long id,
                                @ModelAttribute VacancyDto vacancyDto) {

        vacancyService.update(vacancyDto);
        return "redirect:/team-lead-hr/vacancies";
    }

    @DeleteMapping("/{id}")
    public String deleteVacancy(@PathVariable Long id){
        vacancyService.delete(id);
        return "redirect:/team-lead-hr/vacancies";
    }

}
