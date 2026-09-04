package com.example.employeemanagement.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record DepartmentExpandedResponse(
        Long id,
        String name,
        LocalDateTime createdAt,
        Long headEmployeeId,
        String headEmployeeName,
        PagedResponse<EmployeeResponse> employees
) {
}