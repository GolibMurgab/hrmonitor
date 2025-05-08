package com.myproject.hrmonitor.repository;

import com.myproject.hrmonitor.entity.Resume;
import com.myproject.hrmonitor.entity.Stage;
import com.myproject.hrmonitor.entity.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ResumeRepository extends JpaRepository<Resume, Long>, JpaSpecificationExecutor<Resume> {
    List<Resume> findAllByHr(User hr);

    @Query("SELECT r FROM resume r WHERE r.hr = :hr " +
            "AND (:stage IS NULL OR r.currentStage.sla.stage = :stage) " +
            "AND (:vacancyId IS NULL OR r.vacancy.id = :vacancyId)")
    List<Resume> findAllByHrAndFilters(@Param("hr") User hr,
                                       @Param("stage") Stage stage,
                                       @Param("vacancyId") Long vacancyId,
                                       Sort sort);

    @Query("SELECT r.currentStage.sla.stage, COUNT(r) FROM resume r "
            + "WHERE r.hr = :hr GROUP BY r.currentStage.sla.stage")
    List<Object[]> countByStagePerHr(@Param("hr") User hr);

    @Query("SELECT r.source, COUNT(r) FROM resume r "
            + "WHERE r.hr = :hr GROUP BY r.source")
    List<Object[]> countBySourcePerHr(@Param("hr") User hr);

    Long countByHr(User hr);
}
