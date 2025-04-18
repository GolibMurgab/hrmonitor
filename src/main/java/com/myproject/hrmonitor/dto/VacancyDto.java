package com.myproject.hrmonitor.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class VacancyDto {
    private Long id;
    private String title;
    private String description;
    private String skills;
}
