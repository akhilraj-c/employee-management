package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {

        Department department = departmentRepository
                .findById(request.departmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found: " + request.departmentId()
                        )
                );

        Employee manager = null;

        if (request.managerId() != null) {
            manager = employeeRepository
                    .findById(request.managerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Manager not found: " + request.managerId()
                            )
                    );
        }

        Employee employee = employeeMapper.toEntity(request);

        employee.setDepartment(department);
        employee.setReportingManager(manager);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request
    ) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found: " + employeeId
                        )
                );

        Department department = departmentRepository
                .findById(request.departmentId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found: " + request.departmentId()
                        )
                );

        Employee manager = null;

        if (request.managerId() != null) {

            if (request.managerId().equals(employeeId)) {
                throw new IllegalArgumentException(
                        "Employee cannot report to themselves"
                );
            }

            manager = employeeRepository
                    .findById(request.managerId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Manager not found: " + request.managerId()
                            )
                    );
        }

        employeeMapper.updateEntity(employee, request);

        employee.setDepartment(department);
        employee.setReportingManager(manager);
        employee.setUpdatedAt(java.time.LocalDateTime.now());

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(updatedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found: " + employeeId
                        )
                );

        return employeeMapper.toResponse(employee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public EmployeeResponse updateEmployeeDepartment(
            Long employeeId,
            Long departmentId
    ) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}