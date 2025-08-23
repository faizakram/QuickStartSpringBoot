package com.app.service;

import com.app.model.EmployeeDepartment;
import com.app.repository.EmployeeDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeDepartmentService {
    private final EmployeeDepartmentRepository employeeDepartmentRepository;

    // Find active employee-department relations
    public List<EmployeeDepartment> findAllActiveRelations() {
        return employeeDepartmentRepository.findByIsDeletedFalse();
    }

    // Find employees by department name
    public List<EmployeeDepartment> findEmployeesByDepartmentName(String departmentName) {
        return employeeDepartmentRepository.findByDepartment_NameAndIsDeletedFalse(departmentName);
    }

    // Find departments by employee name
    public List<EmployeeDepartment> findDepartmentsByEmployeeName(String employeeName) {
        return employeeDepartmentRepository.findByEmployee_NameAndIsDeletedFalse(employeeName);
    }

}
