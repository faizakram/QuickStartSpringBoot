package com.app.repository;

import com.app.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // Find department by name
    List<Department> findByName(String name);

    // Find department by description
    List<Department> findByDescription(String description);

    // Find departments by employee name
    List<Department> findByEmployeeDepartments_Employee_Name(String employeeName);
}
