package com.myproject.hrmonitor.service;

import com.myproject.hrmonitor.dto.VacancyDto;
import com.myproject.hrmonitor.entity.Role;
import com.myproject.hrmonitor.entity.User;
import com.myproject.hrmonitor.entity.Vacancy;
import com.myproject.hrmonitor.repository.UserRepository;
import com.myproject.hrmonitor.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacancyService {

    @Autowired
    private VacancyRepository vacancyRepository;

    @Autowired
    private UserRepository userRepository;

    public void createVacancy(VacancyDto dto, String teamLeadUsername) {
        Optional<User> userOpt = userRepository.findByUsername(teamLeadUsername);

        if(userOpt.isPresent()){
            Vacancy vacancy = new Vacancy();
            vacancy.setTitle(dto.getTitle());
            vacancy.setDescription(dto.getDescription());
            vacancy.setSkills(dto.getSkills());
            vacancy.setTeamLead(userOpt.get());
            vacancy.setCreated(LocalDateTime.now());

            vacancyRepository.save(vacancy);
        }
    }

    public void update(VacancyDto dto) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(dto.getId());

        if(vacancyOpt.isPresent()){
            Vacancy vacancy = vacancyOpt.get();
            vacancy.setTitle(dto.getTitle());
            vacancy.setDescription(dto.getDescription());
            vacancy.setSkills(dto.getSkills());

            vacancyRepository.save(vacancy);
        }
    }
    public void delete(Long id) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(id);
        if(vacancyOpt.isPresent()){
            vacancyRepository.delete(vacancyOpt.get());
        }
    }

    public List<Vacancy> getAllVacanciesDto(String teamLeadUsername) {
        Optional<User> userOpt = userRepository.findByUsername(teamLeadUsername);
        if(userOpt.isPresent()){
            return vacancyRepository.findAllByTeamLead(userOpt.get());
        }
        return new ArrayList<Vacancy>();
    }

    public List<User> loadAllTeamLeadHr(String teamLeadUsername){
        Optional<User> userOpt = userRepository.findByUsername(teamLeadUsername);
        if(userOpt.isPresent()){
            return userRepository.findAllByTeamLeadAndRole(userOpt.get(), Role.HR);
        }
        return new ArrayList<User>();
    }

    public void addHr(Long vacancyId, Long hrId) {
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(vacancyId);
        Optional<User> hrOpt = userRepository.findById(hrId);
        if(vacancyOpt.isPresent() && hrOpt.isPresent()){
            Vacancy vacancy = vacancyOpt.get();
            vacancy.setHr(hrOpt.get());
            vacancyRepository.save(vacancy);
        }
    }
}