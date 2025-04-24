package com.myproject.hrmonitor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Duration;

@Entity(name = "slr")
@Data
public class SLA {
    @Id
    @Enumerated(EnumType.STRING)
    private Stage stage;

    @Column(nullable = false)
    private Duration duration;

}
