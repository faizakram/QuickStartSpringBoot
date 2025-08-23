package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
@Data
@DynamicInsert
@DynamicUpdate
public class ScheduleTask {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScheduleType scheduleType;

    @Column
    private LocalDateTime scheduleTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String customScheduleDetails;

    private LocalDateTime lastRun;

    @Column
    private LocalDateTime nextRun;

    @Column
    private Boolean isActive;

    @Column(columnDefinition = "TEXT")
    private String parameters;

}
