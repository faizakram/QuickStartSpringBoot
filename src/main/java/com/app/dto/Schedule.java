package com.app.dto;

import com.app.model.ScheduleType;
import lombok.Data;

@Data
public class Schedule {
    private Long id;
    private String name;
    private String parameters;
    private Boolean isActive;
    private Integer day;
    private ScheduleType scheduleType;
}
