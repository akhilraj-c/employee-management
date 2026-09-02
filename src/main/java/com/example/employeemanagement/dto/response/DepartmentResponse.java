package com.example.employeemanagement.dto.response;

import java.time.LocalDateTime;

public record DepartmentResponse(
        Long id,
        String name,
        LocalDateTime createdAt,
        Long headEmployeeId,
        String headEmployeeName
) {
}