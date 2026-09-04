package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeemanagement.dto.request.DepartmentUpdateRequest;
import com.example.employeemanagement.dto.request.PaginationRequest;
import com.example.employeemanagement.dto.response.ApiResponse;
import com.example.employeemanagement.dto.response.DepartmentAnalyticsResponse;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.dto.response.PagedResponse;
import com.example.employeemanagement.exception.BusinessException;
import com.example.employeemanagement.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import org.springframework.validation.annotation.Validated;

@Validated
@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    // Validated and completed
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @Valid @RequestBody DepartmentCreateRequest request
    ) {

        DepartmentResponse response = departmentService.createDepartment(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "Department created successfully",
                        response
                )
        );
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<?> getDepartmentById(

            @PathVariable Long departmentId,

            @RequestParam(
                    value = "expand",
                    required = false
            )
            String expand,
            @Valid
            @ModelAttribute
            PaginationRequest paginationRequest
    ) {

        if (expand == null || expand.isBlank()) {

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Department fetched successfully",
                            departmentService.getDepartmentById(
                                    departmentId
                            )
                    )
            );
        }

        if ("employee".equalsIgnoreCase(expand)) {

            return ResponseEntity.ok(
                    ApiResponse.success(
                            "Department with employees fetched successfully",
                            departmentService.getDepartmentWithEmployees(
                                    departmentId,
                                    paginationRequest
                            )
                    )
            );
        }

        throw new BusinessException(
                "Unsupported expand parameter: " + expand
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<DepartmentResponse>>> getDepartments(
            @Valid PaginationRequest paginationRequest
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Departments retrieved successfully",
                        departmentService.getDepartments(paginationRequest)
                )
        );
    }

    // Validated and completed
    @PutMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable Long departmentId,
            @Valid @RequestBody DepartmentUpdateRequest request
    ) {
        DepartmentResponse response = departmentService.updateDepartment(
                departmentId,
                request);
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Department updated successfully",
                        response
                        )

        );
    }

    @DeleteMapping("/{departmentId}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(
            @PathVariable Long departmentId
    ) {

        departmentService.deleteDepartment(departmentId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Department deleted successfully",
                        null
                )
        );
    }

    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<PagedResponse<DepartmentAnalyticsResponse>>> getDepartmentAnalytics(
            @Valid PaginationRequest paginationRequest
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Analytics fetched successfully",
                        departmentService.getDepartmentAnalytics(paginationRequest)
                )

        );
    }
}