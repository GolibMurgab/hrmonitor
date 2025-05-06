package com.myproject.hrmonitor.dto;

import com.myproject.hrmonitor.entity.Stage;
import lombok.Data;

@Data
public class ResumeFilterDto {
    private Stage stage;
    private Long vacancyId;
    private String sortBy; // "created-asc", "created-desc", "sla-asc", "sla-desc"
}