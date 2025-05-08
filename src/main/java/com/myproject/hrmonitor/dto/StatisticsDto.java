package com.myproject.hrmonitor.dto;

import com.myproject.hrmonitor.entity.Stage;
import lombok.Data;

import java.util.Map;

@Data
public class StatisticsDto {
    private Map<Stage, Double> avgTimePerStage; // Среднее время по стадиям
    private Map<Stage, Long> stageDistribution; // Распределение по стадиям
    private Map<String, Long> sourceDistribution; // Распределение по источникам
    private Double avgCandidatesPerVacancy; // Среднее кандидатов на вакансию
    private Long slaViolationsCount; // Количество нарушений SLA
}