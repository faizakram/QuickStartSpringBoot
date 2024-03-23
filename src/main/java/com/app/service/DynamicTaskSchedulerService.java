package com.app.service;

import com.app.dto.ScheduledTask;

public interface DynamicTaskSchedulerService {
    ScheduledTask scheduleTask(ScheduledTask task);

    boolean cancelTask(String taskId);
}
