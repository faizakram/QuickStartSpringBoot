package com.app.service;


import com.app.model.Schedular;

public interface TaskSchedulingService {

    void scheduleTasks(Schedular scheduleTask);


    void cancelScheduledTasks(Schedular scheduleTask);

}
