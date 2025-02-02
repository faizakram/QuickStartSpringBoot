package com.app.repository;

import com.app.model.EmployeeDepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeDepartmentRepository extends JpaRepository<EmployeeDepartment, Long> {

    // Find active employee-department relations
    List<EmployeeDepartment> findByIsDeletedFalse();

    // Find employees by department name (ignoring deleted)
    List<EmployeeDepartment> findByDepartment_NameAndIsDeletedFalse(String departmentName);

    // Find departments by employee name (ignoring deleted)
    List<EmployeeDepartment> findByEmployee_NameAndIsDeletedFalse(String employeeName);
}
