package com.example.employeemanagement.dto.request;


import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeCreateRequest(

        @NotBlank(message = "Name is required")
        @Size(
                max = 150,
                message = "Name must not exceed 150 characters"
        )
        String name,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be greater than zero")
        BigDecimal salary,

        @NotBlank(message = "Address is required")
        @Size(
                max = 255,
                message = "Address must not exceed 255 characters"
        )
        String address,

        @NotBlank(message = "Role is required")
        @Size(
                max = 100,
                message = "Role must not exceed 100 characters"
        )
        String role,

        @NotNull(message = "Joining date is required")
        LocalDate joiningDate,

        @NotNull(message = "Yearly bonus percentage is required")
        @DecimalMin(
                value = "0.0",
                message = "Yearly bonus percentage cannot be negative"
        )
        @DecimalMax(
                value = "100.0",
                message = "Yearly bonus percentage cannot exceed 100"
        )
        BigDecimal bonusPercentage,

        @NotNull(message = "Department ID is required")
        @Positive(message = "Department ID not valid")
        Long departmentId,

        Long managerId

) {
}