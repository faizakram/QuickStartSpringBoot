package com.app.service;

import com.app.model.ScheduleTask;

public interface TaskSchedulingService {

    void scheduleTask(ScheduleTask scheduleTask);

    void cancelScheduledTask(Long taskId);

    void addOrUpdateTask(ScheduleTask scheduleTask);
}
