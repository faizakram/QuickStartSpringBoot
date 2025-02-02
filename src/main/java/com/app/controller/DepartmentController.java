package com.app.controller;

import com.app.model.Department;
import com.app.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;


    @PostMapping
    public Department createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Optional<Department> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department departmentDetails) {
        return departmentService.updateDepartment(id, departmentDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

    // FindBy Operations

    @GetMapping("/by-name/{name}")
    public List<Department> getDepartmentsByName(@PathVariable String name) {
        return departmentService.findByName(name);
    }

    @GetMapping("/by-employee-name/{employeeName}")
    public List<Department> getDepartmentsByEmployeeName(@PathVariable String employeeName) {
        return departmentService.findByEmployeeName(employeeName);
    }
}
