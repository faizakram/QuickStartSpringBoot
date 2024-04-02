package com.app.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Table(name = "schedular")
@Data
@DynamicInsert
@DynamicUpdate
public class Schedular {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 255)
    private String name;
    @Column(nullable = false, length = 50)
    private String type;
    @OneToMany(mappedBy = "schedular", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Task> tasks;
}
