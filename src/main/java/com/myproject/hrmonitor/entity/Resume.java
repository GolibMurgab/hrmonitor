package com.myproject.hrmonitor.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "resume")
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

    @Enumerated(EnumType.STRING)
    private Stage currentStage;

    @ManyToOne
    @JoinColumn(name = "vacancy_id", nullable = false)
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User hr;

    public Resume() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public User getHr() {
        return hr;
    }

    public void setHr(User hr) {
        this.hr = hr;
    }
}
