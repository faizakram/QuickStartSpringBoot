package com.app.service;

import com.app.dto.ScheduledTask;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DynamicTaskSchedulerServiceImpl implements DynamicTaskSchedulerService {
    private final TaskScheduler taskScheduler;
    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public DynamicTaskSchedulerServiceImpl(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }
    @Override
    public ScheduledTask scheduleTask(ScheduledTask task) {
        Runnable runnableTask = () -> {
            // Implement the logic of the task here
            System.out.println("Executing task: " + task.getTaskName());
            // For example, you can execute a method on a service or perform any desired operation
        };

        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(runnableTask, new CronTrigger(task.getTaskCronExpression()));
        scheduledTasks.put(task.getTaskId(), scheduledFuture);
        task.setScheduled(true);
        return task;
    }

    @Override
    public boolean cancelTask(String taskId) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskId);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledTasks.remove(taskId);
            System.out.println("Task with ID " + taskId + " has been cancelled.");
        } else {
            System.out.println("Task with ID " + taskId + " not found.");
        }
        return true;
    }
}
