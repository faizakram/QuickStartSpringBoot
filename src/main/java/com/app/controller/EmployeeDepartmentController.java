package com.app.controller;

import com.app.model.EmployeeDepartment;
import com.app.service.EmployeeDepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee-departments")
@RequiredArgsConstructor
public class EmployeeDepartmentController {

    private final EmployeeDepartmentService employeeDepartmentService;

    @GetMapping("/active")
    public List<EmployeeDepartment> getAllActiveRelations() {
        return employeeDepartmentService.findAllActiveRelations();
    }

    @GetMapping("/by-department/{departmentName}")
    public List<EmployeeDepartment> getEmployeesByDepartment(@PathVariable String departmentName) {
        return employeeDepartmentService.findEmployeesByDepartmentName(departmentName);
    }

    @GetMapping("/by-employee/{employeeName}")
    public List<EmployeeDepartment> getDepartmentsByEmployee(@PathVariable String employeeName) {
        return employeeDepartmentService.findDepartmentsByEmployeeName(employeeName);
    }
}
