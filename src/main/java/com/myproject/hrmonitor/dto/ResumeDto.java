package com.myproject.hrmonitor.dto;

import lombok.Data;

@Data
public class ResumeDto {

    private Long id;
    private String candidateName;
    private String source;
    private String link;
    private Long vacancyId;
}
