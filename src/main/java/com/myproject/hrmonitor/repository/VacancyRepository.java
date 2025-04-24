package com.myproject.hrmonitor.repository;

import com.myproject.hrmonitor.entity.User;
import com.myproject.hrmonitor.entity.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    List<Vacancy> findAllByTeamLead(User teamLead);
    List<Vacancy> findAllByHr(User hr);

}
