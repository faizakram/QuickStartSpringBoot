package com.app.service;


import com.app.model.ScheduleTask;
import com.app.repository.ScheduleTaskRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaskSchedulingServiceImpl implements TaskSchedulingService {
    private final RedisLockService redisLockService;
    private final TaskScheduler taskScheduler;
    private final ScheduleTaskRepository scheduleTaskRepository;
    private Map<Long, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    @PostConstruct
    public void initializeScheduledTasks() {
        scheduleTaskRepository.findAllByIsActiveTrue().forEach(this::scheduleTask);
    }


    @Override
    public void scheduleTask(ScheduleTask scheduleTask) {
        String lockKey = "lock:" + scheduleTask.getId();
        Runnable taskWrapper = () -> {
            String lockValue = UUID.randomUUID().toString();
            if (redisLockService.shouldDeferExecution(lockKey)) {
                log.info("Execution deferred due to recent execution.");
                return;
            } else if (!redisLockService.acquireLock(lockKey, lockValue, 30000)) {
                log.info("Task " + scheduleTask.getId() + " is already running. Skipping execution.");
                return;
            }
            try {
                // Task execution logic here
                log.info("Executing Task " + scheduleTask.getId());
                redisLockService.updateLastExecutionTime(lockKey);
            } finally {
                redisLockService.releaseLock(lockKey, lockValue);
            }
        };
        ScheduledFuture<?> future = taskScheduler.schedule(taskWrapper, new CronTrigger(scheduleTask.getCustomScheduleDetails()));
        tasks.put(scheduleTask.getId(), future);
    }

    @Override
    public void cancelScheduledTask(Long taskId) {
        ScheduledFuture<?> future = tasks.get(taskId);
        if (future != null) {
            future.cancel(false);
            tasks.remove(taskId);
        }
    }

    @Override
    public void addOrUpdateTask(ScheduleTask scheduleTask) {
        cancelScheduledTask(scheduleTask.getId()); // Cancel the current task if it's already scheduled
        scheduleTask(scheduleTask); // Reschedule it
    }

}
