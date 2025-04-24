package com.myproject.hrmonitor.dto;

import com.myproject.hrmonitor.entity.Stage;
import lombok.Data;

import java.time.Duration;

@Data
public class SlaDto {
    private Stage stage;
    private int hours;
    private int minutes;

    public Duration getTotalDuration() {
        return Duration.ofHours(hours).plusMinutes(minutes);
    }
}
