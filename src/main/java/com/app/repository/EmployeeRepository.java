package com.app.repository;

import com.app.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Find by name
    List<Employee> findByName(String name);

    // Find by role
    List<Employee> findByRole(String role);

    // Find employees by address city
    List<Employee> findByAddresses_City(String city);

    // Find employees by department name (through EmployeeDepartment)
    List<Employee> findByEmployeeDepartments_Department_Name(String departmentName);
}
