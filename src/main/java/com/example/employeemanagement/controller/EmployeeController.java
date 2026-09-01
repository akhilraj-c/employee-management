package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request
    ) {
        EmployeeResponse response =
                employeeService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long employeeId
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployeeById(employeeId)
        );
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeUpdateRequest request
    ) {
        return ResponseEntity.ok(
                employeeService.updateEmployee(
                        employeeId,
                        request
                )
        );
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long employeeId
    ) {
        employeeService.deleteEmployee(employeeId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{employeeId}/department/{departmentId}")
    public ResponseEntity<EmployeeResponse> updateEmployeeDepartment(
            @PathVariable Long employeeId,
            @PathVariable Long departmentId
    ) {
        return ResponseEntity.ok(
                employeeService.updateEmployeeDepartment(
                        employeeId,
                        departmentId
                )
        );
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getEmployees(
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployees(pageable)
        );
    }
}