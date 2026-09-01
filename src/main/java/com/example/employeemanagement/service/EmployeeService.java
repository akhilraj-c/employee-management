package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request
    );

    EmployeeResponse getEmployeeById(Long employeeId);

    void deleteEmployee(Long employeeId);

    EmployeeResponse updateEmployeeDepartment(
            Long employeeId,
            Long departmentId
    );

    Page<EmployeeResponse> getEmployees(Pageable pageable);

}