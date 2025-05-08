package com.myproject.hrmonitor.service;


import com.myproject.hrmonitor.dto.HrDto;
import com.myproject.hrmonitor.dto.StatisticsDto;
import com.myproject.hrmonitor.entity.*;
import com.myproject.hrmonitor.repository.ResumeRepository;
import com.myproject.hrmonitor.repository.StageHistoryRepository;
import com.myproject.hrmonitor.repository.UserRepository;
import com.myproject.hrmonitor.repository.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HrService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private StageHistoryRepository stageHistoryRepository;

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

    public StatisticsDto getStatistics(String hrUsername) {
        Optional<User> hrOpt = userRepository.findByUsername(hrUsername);
        if (hrOpt.isEmpty()) return new StatisticsDto();

        User hr = hrOpt.get();
        StatisticsDto stats = new StatisticsDto();

        stats.setAvgTimePerStage(calculateAvgStageTimes(hr));

        List<Object[]> stageCounts = resumeRepository.countByStagePerHr(hr);
        Map<Stage, Long> stageDistribution = stageCounts.stream()
                .collect(Collectors.toMap(
                        arr -> (Stage) arr[0],
                        arr -> (Long) arr[1]
                ));
        stats.setStageDistribution(stageDistribution);

        List<Object[]> sourceCounts = resumeRepository.countBySourcePerHr(hr);
        Map<String, Long> sourceDistribution = sourceCounts.stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> (Long) arr[1]
                ));
        stats.setSourceDistribution(sourceDistribution);

        List<Vacancy> vacancies = vacancyRepository.findAllByHr(hr);
        long totalResumes = resumeRepository.countByHr(hr);
        stats.setAvgCandidatesPerVacancy(
                vacancies.isEmpty() ? 0 : (double) totalResumes / vacancies.size()
        );

        stats.setSlaViolationsCount(
                stageHistoryRepository.countSlaViolationsByHr(hr)
        );

        return stats;
    }

    private Map<Stage, Double> calculateAvgStageTimes(User hr) {
        List<StageHistory> histories = stageHistoryRepository.findAllByHr(hr);

        return histories.stream()
                .filter(h -> h.getFinish() != null)
                .collect(Collectors.groupingBy(
                        h -> h.getSla().getStage(),
                        Collectors.averagingDouble(h ->
                                Duration.between(h.getStart(), h.getFinish()).toHours()
                        )
                ));
    }
}
