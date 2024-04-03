package com.app.service;


import com.app.dto.ScheduleRequest;
import com.app.dto.TaskRequest;
import com.app.model.Schedular;
import com.app.model.Task;
import com.app.repository.ScheduleTaskRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class TaskSchedulingServiceImpl implements TaskSchedulingService {
    private final RedisLockService redisLockService;
    private final TaskScheduler taskScheduler;
    private final ScheduleTaskRepository scheduleTaskRepository;
    private final RedisMessageQueueService redisMessageQueueService;
    private final ObjectMapper objectMapper;
    private final Map<Long, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();

    @PostConstruct
    public void initializeScheduledTasks() {
        scheduleTaskRepository.findAll().forEach(this::scheduleTasks);
    }


    @Override
    public void scheduleTasks(Schedular scheduleTask) {
        for (Task task : scheduleTask.getTasks()) {
            String lockKey = "lock:" + task.getId();
            Runnable taskWrapper = () -> {
                String lockValue = UUID.randomUUID().toString();
                if (redisLockService.shouldDeferExecution(lockKey)) {
                    log.info("Execution deferred due to recent execution.");
                    return;
                } else if (!redisLockService.acquireLock(lockKey, lockValue, 30000)) {
                    log.info("Task " + task.getId() + " is already running. Skipping execution.");
                    return;
                }
                try {
                    // Task execution logic here
                    log.info("Executing Task " + task.getId());
                    Map<String, String> message = new HashMap<>();
                    message.put("schedule", objectMapper.writeValueAsString(getScheduleRequest(scheduleTask)));
                    redisMessageQueueService.publishToStream("yourStreamKey", message);
                    redisLockService.updateLastExecutionTime(lockKey);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                } finally {
                    redisLockService.releaseLock(lockKey, lockValue);
                }
            };
            ScheduledFuture<?> future = taskScheduler.schedule(taskWrapper, new CronTrigger(task.getCron()));
            tasks.put(scheduleTask.getId(), future);
        }
    }

    private ScheduleRequest getScheduleRequest(Schedular scheduleTask) {
        ScheduleRequest scheduleRequest = new ScheduleRequest();
        scheduleRequest.setId(scheduleTask.getId());
        scheduleRequest.setName(scheduleTask.getName());
        scheduleRequest.setType(scheduleTask.getType());
        scheduleRequest.setTasks(getScheduleRequestTask(scheduleTask.getTasks()));
        return scheduleRequest;
    }

    private List<TaskRequest> getScheduleRequestTask(List<Task> tasks) {
        List<TaskRequest> taskRequests = new ArrayList<>(tasks.size());
        for (Task task : tasks) {
            TaskRequest taskRequest = new TaskRequest();
            taskRequest.setId(task.getId());
            taskRequest.setParameters(task.getParameters());
            taskRequests.add(taskRequest);
        }
        return taskRequests;
    }

    private void cancelScheduledTask(Long taskId) {
        ScheduledFuture<?> future = tasks.get(taskId);
        if (future != null) {
            future.cancel(false);
            tasks.remove(taskId);
        }
    }

    @Override
    public void cancelScheduledTasks(Schedular scheduleTask) {
        for (Task task : scheduleTask.getTasks()) {
            cancelScheduledTask(task.getId());
        }
    }

}
