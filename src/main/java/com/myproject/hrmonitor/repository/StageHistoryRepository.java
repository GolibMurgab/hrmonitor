package com.myproject.hrmonitor.repository;

import com.myproject.hrmonitor.entity.StageHistory;
import com.myproject.hrmonitor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StageHistoryRepository extends JpaRepository<StageHistory, Long> {
    void deleteByResumeId(Long resumeId);

    @Query("SELECT h FROM stagehistory h WHERE h.resumeId IN "
            + "(SELECT r.id FROM resume r WHERE r.hr = :hr)")
    List<StageHistory> findAllByHr(@Param("hr") User hr);

    @Query("SELECT COUNT(sh) " +
            "FROM stagehistory sh " +
            "JOIN slr s ON sh.sla.stage = s.stage " +
            "JOIN resume r ON sh.resumeId = r.id " +
            "WHERE r.hr = :hr " +
            "AND (" +
            "   (sh.finish IS NOT NULL AND sh.finish > FUNCTION('TIMESTAMPADD', SECOND, s.duration, sh.start)) " +
            "   OR " +
            "   (sh.finish IS NULL AND CURRENT_TIMESTAMP > FUNCTION('TIMESTAMPADD', SECOND, s.duration, sh.start))" +
            ")")
    Long countSlaViolationsByHr(@Param("hr") User hr);
}
