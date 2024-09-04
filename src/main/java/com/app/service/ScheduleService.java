package com.app.service;

import com.app.dto.Schedule;

public interface ScheduleService {
    Schedule addSchedule(Schedule schedule);

    Schedule deleteSchedule(Long id);

    Schedule updateSchedule(Schedule schedule);
}
