package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeDepartmentUpdateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.request.PaginationRequest;
import com.example.employeemanagement.dto.response.ApiResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.validation.annotation.Validated;

@Validated
@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Validated and completed
    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeResponse>> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request
    ) {
        EmployeeResponse response =
                employeeService.createEmployee(request);


        return ResponseEntity.ok(
                ApiResponse.success(
                        "Employee created successfully",
                        response
                )
        );

    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> getEmployeeById(
            @PathVariable Long employeeId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Employee fetched successfully",
                        employeeService.getEmployeeById(employeeId)
                )

        );
    }

    // Validated and completed
    @PutMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeResponse>> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeUpdateRequest request
    ) {

        EmployeeResponse response = employeeService.updateEmployee(
                employeeId,
                request
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Employee updated successfully",
                        response
                )
        );
    }


    //6 Validated and completed
    @PatchMapping("/{employeeId}/department")
    public ResponseEntity<ApiResponse<EmployeeResponse>> moveEmployeeDepartment(
            @PathVariable
            @Positive(message = "Employee ID must be positive")
            Long employeeId,

            @Valid
            @RequestBody
            EmployeeDepartmentUpdateRequest request
    ) {

        EmployeeResponse response =
                employeeService.moveEmployeeDepartment(
                        employeeId,
                        request.departmentId()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Employee moved to another department succesfully",
                        response
                )
        );
    }

    // Validated and completed
    @GetMapping
    public ResponseEntity<?> getEmployees(

            @RequestParam(
                    value = "lookup",
                    required = false,
                    defaultValue = "false"
            )
            boolean lookup,

            @Valid PaginationRequest paginationRequest
    ) {

        if (lookup) {

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Employee lookup data fetched successfully",
                            employeeService.getEmployeeLookup(paginationRequest)
                    )
            );
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Employees fetched successfully",
                        employeeService.getEmployees(paginationRequest)
                )
        );
    }
}