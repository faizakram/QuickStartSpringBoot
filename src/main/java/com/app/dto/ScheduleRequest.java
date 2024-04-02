package com.app.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleRequest {
    private Long id;
    private String name;
    private String type;
    private List<TaskRequest> tasks;
}
