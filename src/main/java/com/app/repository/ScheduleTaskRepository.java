package com.app.repository;

import com.app.model.Schedular;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleTaskRepository extends JpaRepository<Schedular, Long> {

}
