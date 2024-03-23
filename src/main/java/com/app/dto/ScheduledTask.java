package com.app.dto;

public class ScheduledTask {
    private String taskId;
    private String taskName;
    private String taskCronExpression;
    private boolean isScheduled;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskCronExpression() {
        return taskCronExpression;
    }

    public void setTaskCronExpression(String taskCronExpression) {
        this.taskCronExpression = taskCronExpression;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean scheduled) {
        isScheduled = scheduled;
    }
}
