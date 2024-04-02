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
//        Runnable taskWrapper = () -> {
//            String lockName = scheduleTask.getName()+" - "+scheduleTask.getId();
//            Instant lockAtMostUntil = Instant.now().plusSeconds(600); // Lock for 10 minutes
//            Instant lockAtLeastUntil = Instant.now().plusSeconds(300);
//            LockConfiguration config = new LockConfiguration(lockName, lockAtMostUntil, lockAtLeastUntil);
//            Optional<SimpleLock> lock = lockProvider.lock(config);
//            try {
//                lock.ifPresentOrElse(simpleLock -> {
//                    try {
//                        LockAssert.assertLocked();
//                        // Execute the actual task logic here
//                        log.info("Executing Task " + scheduleTask.getId());
//                    } catch (Exception e) {
//                        log.error("Error executing task: " + scheduleTask.getId(), e);
//                    }
//                }, () -> {
//                    log.info("Could not acquire lock for task " + scheduleTask.getId());
//                });
//            } finally {
//                lock.ifPresent(SimpleLock::unlock);
//            }
//        };
        ScheduledFuture<?> future = taskScheduler.schedule(() -> this.executeTask(scheduleTask), new CronTrigger(scheduleTask.getCustomScheduleDetails()));
        tasks.put(scheduleTask.getId(), future);
    }

    private void executeTask(ScheduleTask scheduleTask) {
        String lockName = "myDynamicTask";
        Instant lockAtMostUntil = Instant.now().plusSeconds(60); // Lock for 10 minutes
        Instant lockAtLeastUntil = Instant.now().plusSeconds(30); // Minimum lock time

        LockConfiguration config = new LockConfiguration(lockName, lockAtMostUntil, lockAtLeastUntil);
        Optional<SimpleLock> lock = lockProvider.lock(config);
        try {
            lock.ifPresentOrElse(simpleLock -> {
                LockAssert.assertLocked();
                // Task logic here
                log.info("Executing Task " + scheduleTask.getId());
            }, () -> {
                System.out.println("Could not acquire lock for task");
            });
        } finally {
            lock.ifPresent(SimpleLock::unlock);
        }
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
