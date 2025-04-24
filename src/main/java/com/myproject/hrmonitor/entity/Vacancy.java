package com.myproject.hrmonitor.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "vacancy")
@Data
public class Vacancy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime created;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private String skills;

    @ManyToOne
    @JoinColumn(name = "hr_id")
    private User hr;

    @ManyToOne
    @JoinColumn(name = "teamlead_id")
    private User teamLead;

}
