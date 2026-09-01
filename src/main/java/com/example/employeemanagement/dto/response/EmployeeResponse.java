package com.example.employeemanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String name,
        LocalDate dateOfBirth,
        BigDecimal salary,
        String address,
        String role,
        LocalDate joiningDate,
        BigDecimal bonusPercentage,
        Long departmentId,
        String departmentName,
        Long managerId,
        String managerName
) {
}