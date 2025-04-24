package com.myproject.hrmonitor.repository;

import com.myproject.hrmonitor.entity.Resume;
import com.myproject.hrmonitor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findAllByHr(User hr);
}
