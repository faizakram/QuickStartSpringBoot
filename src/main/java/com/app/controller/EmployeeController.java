package com.app.controller;

import com.app.model.Employee;
import com.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // CRUD Operations
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Optional<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        return employeeService.updateEmployee(id, employeeDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    // FindBy Operations
    @GetMapping("/by-city/{city}")
    public List<Employee> getEmployeesByCity(@PathVariable String city) {
        return employeeService.findByCity(city);
    }

    @GetMapping("/by-department/{departmentName}")
    public List<Employee> getEmployeesByDepartmentName(@PathVariable String departmentName) {
        return employeeService.findByDepartmentName(departmentName);
    }
}
