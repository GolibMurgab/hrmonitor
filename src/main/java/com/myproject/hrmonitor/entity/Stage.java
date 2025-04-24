package com.myproject.hrmonitor.entity;

import jakarta.persistence.Entity;

public enum Stage {
    OPEN,
    REVIEWED,
    INTERVIEW,
    INTERVIEW_PASSED,
    TECHNICAL_INTERVIEW,
    TECHNICAL_INTERVIEW_PASSED,
    OFFER
}