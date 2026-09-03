package com.example.employeemanagement.dto.response;

import java.math.BigDecimal;

public record DepartmentAnalyticsResponse(
        Long departmentId,
        String departmentName,
        Long headCount,
        BigDecimal totalPayrollCost,
        BigDecimal averageSalary,
        String departmentHead
) {
}