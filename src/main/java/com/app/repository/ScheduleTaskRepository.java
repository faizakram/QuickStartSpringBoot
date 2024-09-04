package com.app.repository;

import com.app.model.ScheduleTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleTaskRepository extends JpaRepository<ScheduleTask, Long> {
    List<ScheduleTask> findAllByIsActiveTrue();
}
