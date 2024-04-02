package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "schedular_task")
@Data
@DynamicInsert
@DynamicUpdate
public class Task {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String cron;
    @Column(columnDefinition = "TEXT")
    private String parameters;
    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedular schedular;
}
