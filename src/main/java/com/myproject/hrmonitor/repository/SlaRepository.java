package com.myproject.hrmonitor.repository;

import com.myproject.hrmonitor.entity.SLA;
import com.myproject.hrmonitor.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlaRepository extends JpaRepository<SLA, Stage> {

}
