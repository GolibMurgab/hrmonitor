package com.myproject.hrmonitor.dto;

import com.myproject.hrmonitor.entity.Stage;
import lombok.Data;

@Data
public class StageStatisticsDto {
    private Stage stage;
    private double averageHours;
}