package com.app.service;


import com.app.model.ScheduleTask;
import com.app.repository.ScheduleTaskRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.core.LockConfiguration;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.core.SimpleLock;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaskSchedulingServiceImpl implements TaskSchedulingService {
    private final TaskScheduler taskScheduler;
    private final LockProvider lockProvider;
    private final ScheduleTaskRepository scheduleTaskRepository;
    private Map<Long, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    @PostConstruct
    public void initializeScheduledTasks() {
        scheduleTaskRepository.findAllByIsActiveTrue().forEach(this::scheduleTask);
    }


    @Override
    public void scheduleTask(ScheduleTask scheduleTask) {
        Runnable taskWrapper = () -> {
            String lockName = scheduleTask.getName()+" - "+scheduleTask.getId();
            Instant createdAt = Instant.now();
            LockConfiguration config = new LockConfiguration(createdAt, lockName, Duration.ofMinutes(1), Duration.ofSeconds(10));
            Optional<SimpleLock> lock = lockProvider.lock(config);
            try {
                lock.ifPresent(simpleLock -> {
                    try {
                        LockAssert.assertLocked();
                        // Execute the actual task logic here
                        log.info("Executing Task " + scheduleTask.getId());
                    } catch (Exception e) {
                        log.error("Error executing task: " + scheduleTask.getId(), e);
                    }
                });
            } finally {
                lock.ifPresent(SimpleLock::unlock);
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
