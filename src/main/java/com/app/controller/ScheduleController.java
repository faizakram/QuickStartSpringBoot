package com.app.controller;

import com.app.dto.Schedule;
import com.app.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("schedule")
public class ScheduleController {

    private final ScheduleService ScheduleService;

    @PostMapping("add")
    public ResponseEntity<Schedule> addSchedule(@RequestBody Schedule schedule) {
        return new ResponseEntity<>(ScheduleService.addSchedule(schedule), HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity<Schedule> updateSchedule(@RequestBody Schedule schedule) {
        return new ResponseEntity<>(ScheduleService.updateSchedule(schedule), HttpStatus.OK);
    }
    @DeleteMapping("delete")
    public ResponseEntity<Schedule> deleteSchedule(@RequestParam Long id) {
        return new ResponseEntity<>(ScheduleService.deleteSchedule(id), HttpStatus.OK);
    }
}
