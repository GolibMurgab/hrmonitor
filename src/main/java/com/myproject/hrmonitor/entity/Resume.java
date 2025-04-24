package com.myproject.hrmonitor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity(name = "resume")
@Data
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String candidateName;
    @Column(nullable = false)
    private String source;
    private String link;
    @Column(nullable = false)
    private LocalDateTime created;

    @OneToOne
    @JoinColumn
    private StageHistory currentStage;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User hr;

    @Transient
    private String slaTimeDisplay;
}
