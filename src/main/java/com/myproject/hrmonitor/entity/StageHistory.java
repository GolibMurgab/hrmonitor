package com.myproject.hrmonitor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity(name = "stagehistory")
@Data
public class StageHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Stage stage;

    private Long resumeId;

    @Column(nullable = false)
    private LocalDateTime start;

    private LocalDateTime finish;
}
