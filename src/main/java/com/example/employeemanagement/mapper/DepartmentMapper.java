package com.example.employeemanagement.mapper;

import com.example.employeemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeemanagement.dto.response.DepartmentExpandedResponse;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.entity.Department;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentCreateRequest request) {

        Department department = new Department();

        department.setName(request.name());

        return department;
    }

    public DepartmentResponse toResponse(Department department) {

        return new DepartmentResponse(
                department.getId(),
                department.getName(),
                department.getCreatedAt(),
                department.getHead() != null
                        ? department.getHead().getId()
                        : null,
                department.getHead() != null
                        ? department.getHead().getName()
                        : null
        );
    }

    public DepartmentExpandedResponse toExpandedResponse(
            Department department,
            List<EmployeeResponse> employees
    ) {

        return new DepartmentExpandedResponse(
                department.getId(),
                department.getName(),
                department.getCreatedAt(),
                department.getHead() != null
                        ? department.getHead().getId()
                        : null,
                department.getHead() != null
                        ? department.getHead().getName()
                        : null,
                employees
        );
    }
}