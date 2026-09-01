package com.example.employeemanagement.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeCreateRequest(

        @NotBlank
        String name,

        @NotNull
        LocalDate dateOfBirth,

        @NotNull
        @Positive
        BigDecimal salary,

        String address,

        @NotBlank
        String role,

        @NotNull
        LocalDate joiningDate,

        @Positive
        BigDecimal bonusPercentage,

        @NotNull
        Long departmentId,

        Long managerId

) {
}