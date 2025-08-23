package com.app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    private Set<EmployeeDepartment> employeeDepartments;

    // Constructors, Getters, and Setters
}
