package com.example.employeemanagement.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record DepartmentUpdateRequest(

        @NotBlank(message = "Department name is required")
        @Size(
                max = 100,
                message = "Department name must not exceed 100 characters"
        )
        @Pattern(
                regexp = "^[A-Za-z0-9]+(?: [A-Za-z0-9]+)*$",
                message = "Department name must contain single spaces between words and no leading or trailing spaces"
        )
        String name,

        @Positive(message = "Head employee ID must be positive")
        Long headEmployeeId

) {
}