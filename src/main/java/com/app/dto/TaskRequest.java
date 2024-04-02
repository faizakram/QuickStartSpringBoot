package com.app.dto;

import com.app.model.ScheduleType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TaskRequest {
    private Long id;
    private Integer sequence;
    private Integer day;
    private ScheduleType scheduleType;
    private String parameters;
}
