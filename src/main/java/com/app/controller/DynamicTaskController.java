package com.app.controller;

import com.app.dto.ScheduledTask;
import com.app.service.DynamicTaskSchedulerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/task")
public class DynamicTaskController {

    private DynamicTaskSchedulerService dynamicTaskSchedulerService;

    public DynamicTaskController(DynamicTaskSchedulerService dynamicTaskSchedulerService) {
        this.dynamicTaskSchedulerService = dynamicTaskSchedulerService;
    }

    @PostMapping("schedule")
    public ResponseEntity<ScheduledTask> scheduleTask(@RequestBody ScheduledTask scheduledTask) {
        scheduledTask.setTaskId(UUID.randomUUID().toString());
        return new ResponseEntity<>(dynamicTaskSchedulerService.scheduleTask(scheduledTask), HttpStatus.OK);
    }

    @DeleteMapping("cancel")
    public ResponseEntity<Boolean> scheduleTask(@RequestParam String taskId) {
        return new ResponseEntity<>(dynamicTaskSchedulerService.cancelTask(taskId), HttpStatus.OK);
    }
}
