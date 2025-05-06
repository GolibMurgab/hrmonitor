package com.myproject.hrmonitor.repository;

import com.myproject.hrmonitor.entity.StageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StageHistoryRepository extends JpaRepository<StageHistory, Long> {
    void deleteByResumeId(Long resumeId);
}
