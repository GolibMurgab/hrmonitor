package com.myproject.hrmonitor.service;

import com.myproject.hrmonitor.dto.ResumeDto;
import com.myproject.hrmonitor.entity.*;
import com.myproject.hrmonitor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private ResumeRepository resumeRepository;
    @Autowired
    private StageHistoryRepository stageHistoryRepository;
    @Autowired
    private SlaRepository slaRepository;

    public List<Vacancy> getAllVacancies(String hrUsername){
        Optional<User> hrOpt = userRepository.findByUsername(hrUsername);
        if(hrOpt.isPresent()){
            return vacancyRepository.findAllByHr(hrOpt.get());
        }
        return new ArrayList<>();
    }

    public void save(ResumeDto resumeDto, String hrUsername) {
        Optional<User> hrOpt = userRepository.findByUsername(hrUsername);
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(resumeDto.getVacancyId());
        if(hrOpt.isPresent() && vacancyOpt.isPresent()){
            User hr = hrOpt.get();
            Vacancy vacancy = vacancyOpt.get();
            Resume resume = new Resume();
            LocalDateTime currentTime = LocalDateTime.now();
            StageHistory stageHistory = new StageHistory();

            stageHistory.setStage(Stage.OPEN);
            stageHistory.setStart(currentTime);
            stageHistory = stageHistoryRepository.save(stageHistory);

            resume.setCandidateName(resumeDto.getCandidateName());
            resume.setSource(resumeDto.getSource());
            resume.setLink(resumeDto.getLink());
            resume.setCreated(currentTime);
            resume.setHr(hr);
            resume.setVacancy(vacancy);
            resume.setCurrentStage(stageHistory);
            resume = resumeRepository.save(resume);

            stageHistory.setResumeId(resume.getId());
            stageHistoryRepository.save(stageHistory);
        }
    }

    public void setResumeDuration(List<Resume> resumes){
        resumes.forEach(resume -> {
            StageHistory currentStage = resume.getCurrentStage();
            Optional<SLA> sla = slaRepository.findById(currentStage.getStage());

            if (sla.isPresent()) {
                Duration allowed = sla.get().getDuration();
                Duration actual = Duration.between(
                        currentStage.getStart(),
                        LocalDateTime.now()
                );

                if (actual.compareTo(allowed) <= 0) {
                    Duration remaining = allowed.minus(actual);
                    String text = String.format("Осталось %d ч %d мин",
                            remaining.toHours(),
                            remaining.toMinutes() % 60);
                    resume.setSlaTimeDisplay(text);
                } else {
                    Duration overdue = actual.minus(allowed);
                    String text = String.format("Просрочено на %d д %d ч",
                            overdue.toDays(),
                            overdue.toHours() % 24);
                    resume.setSlaTimeDisplay(text);
                }
            } else {
                resume.setSlaTimeDisplay("SLA не настроено");
            }
        });
    }

    public List<Resume> getResumes(String hrUsername){
        Optional<User> hrOpt = userRepository.findByUsername(hrUsername);
        List<Resume> resumes = new ArrayList<>();
        if(hrOpt.isPresent()){
            resumes = resumeRepository.findAllByHr(hrOpt.get());
            this.setResumeDuration(resumes);
        }
        return resumes;
    }

    public void changeStage(Long id) {
        Optional<Resume> resumeOpt = resumeRepository.findById(id);
        if(resumeOpt.isPresent()){
            Resume resume = resumeOpt.get();
            Stage stage = resume.getCurrentStage().getStage().next();
            if(stage != null){
                LocalDateTime currentTime = LocalDateTime.now();
                StageHistory stageHistory = new StageHistory();
                stageHistory.setStage(stage);
                stageHistory.setStart(currentTime);
                stageHistory = stageHistoryRepository.save(stageHistory);

                resume.setCurrentStage(stageHistory);
                resumeRepository.save(resume);
            }
        }
    }
}
