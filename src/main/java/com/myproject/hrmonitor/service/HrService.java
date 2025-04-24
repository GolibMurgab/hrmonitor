package com.myproject.hrmonitor.service;


import com.myproject.hrmonitor.dto.HrDto;
import com.myproject.hrmonitor.entity.Role;
import com.myproject.hrmonitor.entity.User;
import com.myproject.hrmonitor.entity.Vacancy;
import com.myproject.hrmonitor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HrService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAwailableHr(){
        return userRepository.findAllByTeamLeadIsNullAndRoleNot(Role.TEAM_LEAD_HR);
    }

    public void addHr(HrDto hrDto, String teamLeadUsername) {
        Optional<User> userOpt = userRepository.findByUsername(teamLeadUsername);
        Optional<User> hrOpt = userRepository.findById(hrDto.getId());

        if(userOpt.isPresent() && hrOpt.isPresent()){
            User hr = hrOpt.get();
            hr.setTeamLead(userOpt.get());

            userRepository.save(hr);
        }

    }

    public List<User> getMyHr(String teamLeadUsername) {
        Optional<User> userOpt = userRepository.findByUsername(teamLeadUsername);
        if(userOpt.isPresent()){
            return userRepository.findAllByTeamLeadAndRole(userOpt.get(), Role.HR);
        }
        return new ArrayList<>();
    }
}
