package com.example.employeemanagement.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
public record EmployeeUpdateRequest(

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Date of birth is required")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotNull(message = "Salary is required")
        @Positive(message = "Salary must be greater than zero")
        BigDecimal salary,

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "Role is required")
        String role,

        @NotNull(message = "Joining date is required")
        LocalDate joiningDate,

        @NotNull(message = "Bonus percentage is required")
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "100.0")
        BigDecimal bonusPercentage,

        Long managerId

) {
}