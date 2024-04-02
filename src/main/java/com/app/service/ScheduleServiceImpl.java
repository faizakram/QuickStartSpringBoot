package com.app.service;

import com.app.dto.ScheduleRequest;
import com.app.dto.TaskRequest;
import com.app.model.Schedular;
import com.app.model.Task;
import com.app.repository.ScheduleTaskRepository;
import com.app.utils.CreateCronExpression;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScheduleServiceImpl implements ScheduleService {
    private final CreateCronExpression createCronExpression;
    private final TaskSchedulingService taskSchedulingService;
    private final ScheduleTaskRepository scheduleTaskRepository;

    @Override
    public ScheduleRequest addOrUpdateSchedule(ScheduleRequest scheduleRequest) {
        Schedular schedule = scheduleTaskCreatedOrNot(scheduleRequest);
        schedule.setName(scheduleRequest.getName());
        schedule.setType(scheduleRequest.getType());
        schedule.getTasks().clear();
        for (TaskRequest taskRequest : scheduleRequest.getTasks()) {
            String cron = switch (taskRequest.getScheduleType()) {
                case MINUTE -> createCronExpression.generateEveryMinuteCronExpression();
                case HOUR -> createCronExpression.generateHourlyCronExpression();
                case DAILY -> createCronExpression.generateDailyCronExpression();
                case WEEKLY -> createCronExpression.generateWeeklyCronExpression(taskRequest.getDay());
                case MONTHLY -> createCronExpression.generateMonthlyCronExpression(taskRequest.getDay());
            };
            Task task = new Task();
            task.setCron(cron);
            task.setSchedular(schedule);
            task.setParameters(taskRequest.getParameters());
            schedule.getTasks().add(task);
        }
        return setScheduleRequest(scheduleTaskRepository.save(schedule), scheduleRequest);
    }

    private ScheduleRequest setScheduleRequest(Schedular schedule, ScheduleRequest scheduleRequest) {
        taskSchedulingService.scheduleTasks(schedule);
        scheduleRequest.setId(schedule.getId());
        return scheduleRequest;
    }


    /**
     * schedule Task Created Or Not
     *
     * @param schedule
     * @return
     */
    private Schedular scheduleTaskCreatedOrNot(ScheduleRequest schedule) {
        if (schedule.getId() != null) {
            Optional<Schedular> scheduleTaskOptional = scheduleTaskRepository.findById(schedule.getId());
            if (scheduleTaskOptional.isPresent()) {
                Schedular schedular = scheduleTaskOptional.get();
                taskSchedulingService.cancelScheduledTasks(schedular);
                return schedular;
            }
        }
        Schedular scheduleTask = new Schedular();
        scheduleTask.setTasks(new ArrayList<>(schedule.getTasks().size()));
        return scheduleTask;
    }
}
