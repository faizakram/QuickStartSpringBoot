package com.app.service;

import com.app.model.Department;
import com.app.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    // CRUD Operations
    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.setName(departmentDetails.getName());
        department.setDescription(departmentDetails.getDescription());

        return departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }

    // FindBy Methods
    public List<Department> findByName(String name) {
        return departmentRepository.findByName(name);
    }

    public List<Department> findByEmployeeName(String employeeName) {
        return departmentRepository.findByEmployeeDepartments_Employee_Name(employeeName);
    }
}