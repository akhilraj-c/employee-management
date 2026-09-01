package com.example.employeemanagement.mapper;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeCreateRequest request) {

        Employee employee = new Employee();

        employee.setName(request.name());
        employee.setDateOfBirth(request.dateOfBirth());
        employee.setSalary(request.salary());
        employee.setAddress(request.address());
        employee.setRole(request.role());
        employee.setJoiningDate(request.joiningDate());
        employee.setYearlyBonusPercentage(request.bonusPercentage());

        return employee;
    }

    public EmployeeResponse toResponse(Employee employee) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getDateOfBirth(),
                employee.getSalary(),
                employee.getAddress(),
                employee.getRole(),
                employee.getJoiningDate(),
                employee.getYearlyBonusPercentage(),

                employee.getDepartment().getId(),
                employee.getDepartment().getName(),

                employee.getReportingManager() != null
                        ? employee.getReportingManager().getId()
                        : null,

                employee.getReportingManager() != null
                        ? employee.getReportingManager().getName()
                        : null
        );
    }

    public void updateEntity(
            Employee employee,
            EmployeeUpdateRequest request
    ) {
        employee.setName(request.name());
        employee.setDateOfBirth(request.dateOfBirth());
        employee.setSalary(request.salary());
        employee.setAddress(request.address());
        employee.setRole(request.role());
        employee.setJoiningDate(request.joiningDate());
        employee.setYearlyBonusPercentage(request.bonusPercentage());
    }
}