package com.app.controller;

import com.app.dto.ScheduleRequest;
import com.app.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ScheduleRequest> addOrUpdateSchedule(@RequestBody ScheduleRequest schedule) {
        return new ResponseEntity<>(scheduleService.addOrUpdateSchedule(schedule), HttpStatus.OK);
    }
}
