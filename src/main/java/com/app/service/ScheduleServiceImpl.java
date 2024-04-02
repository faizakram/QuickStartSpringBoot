package com.app.service;

import com.app.dto.Schedule;
import com.app.model.ScheduleTask;
import com.app.repository.ScheduleTaskRepository;
import com.app.utils.CreateCronExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScheduleServiceImpl implements ScheduleService {
    private final CreateCronExpression createCronExpression;
    private final TaskSchedulingService taskSchedulingService;
    private final ScheduleTaskRepository scheduleTaskRepository;

    @Override
    public Schedule addSchedule(Schedule schedule) {
        String cron = switch (schedule.getScheduleType()) {
            case MINUTE -> createCronExpression.generateEveryMinuteCronExpression();
            case HOUR -> createCronExpression.generateHourlyCronExpression();
            case DAILY -> createCronExpression.generateDailyCronExpression();
            case WEEKLY -> createCronExpression.generateWeeklyCronExpression(schedule.getDay());
            case MONTHLY -> createCronExpression.generateMonthlyCronExpression(schedule.getDay());
        };
        ScheduleTask scheduleTask = new ScheduleTask();
        scheduleTask.setCustomScheduleDetails(cron);
        scheduleTask.setName(schedule.getName());
        scheduleTask.setScheduleType(schedule.getScheduleType());
        scheduleTask.setIsActive(schedule.getIsActive());
        scheduleTask = scheduleTaskRepository.save(scheduleTask);
        log.info(cron);
        schedule.setId(scheduleTask.getId());
        taskSchedulingService.scheduleTask(scheduleTask);
        return schedule;
    }

    @Override
    public Schedule deleteSchedule(Long id) {
        Optional<ScheduleTask> scheduleTaskOptional = scheduleTaskRepository.findById(id);
        if(scheduleTaskOptional.isPresent()){
            ScheduleTask scheduleTask = scheduleTaskOptional.get();
            scheduleTask.setIsActive(false);
            scheduleTask = scheduleTaskRepository.save(scheduleTask);
            taskSchedulingService.cancelScheduledTask(scheduleTask.getId());
        }
        return null;
    }

    @Override
    public Schedule updateSchedule(Schedule schedule) {
        return null;
    }
}
