package com.myproject.hrmonitor.service;

import com.myproject.hrmonitor.dto.ResumeDto;
import com.myproject.hrmonitor.entity.*;
import com.myproject.hrmonitor.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Optional<SLA> slaOpt = slaRepository.findById(Stage.OPEN);
        if(hrOpt.isPresent() && vacancyOpt.isPresent() && slaOpt.isPresent()){
            User hr = hrOpt.get();
            Vacancy vacancy = vacancyOpt.get();
            SLA sla = slaOpt.get();
            Resume resume = new Resume();
            LocalDateTime currentTime = LocalDateTime.now();
            StageHistory stageHistory = new StageHistory();

            stageHistory.setSla(sla);
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
            Optional<SLA> slaOpt = slaRepository.findById(currentStage.getSla().getStage());

            if (slaOpt.isPresent()) {
                SLA sla = slaOpt.get();
                Duration allowed = sla.getDuration();
                LocalDateTime start = currentStage.getStart();
                LocalDateTime deadline = start.plus(allowed);
                resume.setSlaDeadline(deadline);

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

    public List<Resume> getResumesFiltered(String hrUsername, Stage stage, Long vacancyId, String sortBy) {
        Optional<User> hrOpt = userRepository.findByUsername(hrUsername);
        List<Resume> resumes = new ArrayList<>();
        if (hrOpt.isPresent()) {
            User hr = hrOpt.get();
            Sort sort = resolveSort(sortBy);
            resumes = resumeRepository.findAllByHrAndFilters(hr, stage, vacancyId, sort);
            this.setResumeDuration(resumes);
            if (sortBy != null && sortBy.startsWith("sla")) {
                sortResumesBySlaDeadline(resumes, sortBy);
            }
        }
        return resumes;
    }

    private Sort resolveSort(String sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "created");
        }

        String[] parts = sortBy.split("-");
        String field = parts[0];
        Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        if ("created".equals(field)) {
            return Sort.by(direction, "created");
        }

        return Sort.by(Sort.Direction.DESC, "created");
    }

    private void sortResumesBySlaDeadline(List<Resume> resumes, String sortBy) {
        String direction = sortBy.split("-")[1];
        resumes.sort((r1, r2) -> {
            int compare = r1.getSlaDeadline().compareTo(r2.getSlaDeadline());
            return "asc".equals(direction) ? compare : -compare;
        });
    }


    public void changeStage(Long id) {
        Optional<Resume> resumeOpt = resumeRepository.findById(id);
        if(resumeOpt.isPresent()){
            Resume resume = resumeOpt.get();
            StageHistory currentStageHistory = resume.getCurrentStage();
            Stage stage = currentStageHistory.getSla().getStage().next();
            if(stage != null && slaRepository.findById(stage).isPresent()){
                SLA sla = slaRepository.findById(stage).get();
                LocalDateTime currentTime = LocalDateTime.now();
                StageHistory stageHistory = new StageHistory();

                currentStageHistory.setFinish(currentTime);
                stageHistory.setSla(sla);
                stageHistory.setStart(currentTime);
                stageHistory.setResumeId(id);
                stageHistory = stageHistoryRepository.save(stageHistory);
                stageHistoryRepository.save(currentStageHistory);

                resume.setCurrentStage(stageHistory);
                resumeRepository.save(resume);
            }
        }
    }

    public void editResume(Long id, ResumeDto resumeDto) {
        Optional<Resume> resumeOpt = resumeRepository.findById(id);
        Optional<Vacancy> vacancyOpt = vacancyRepository.findById(resumeDto.getVacancyId());

        if(resumeOpt.isPresent() && vacancyOpt.isPresent()){
            Resume resume = resumeOpt.get();
            Vacancy vacancy = vacancyOpt.get();

            resume.setVacancy(vacancy);
            resume.setCandidateName(resumeDto.getCandidateName());
            resume.setSource(resumeDto.getSource());
            resume.setLink(resumeDto.getLink());

            resumeRepository.save(resume);
        }
    }

    @Transactional
    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
        stageHistoryRepository.deleteByResumeId(id);
    }
}
