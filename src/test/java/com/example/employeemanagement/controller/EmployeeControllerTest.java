package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeDepartmentUpdateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.EmployeeLookupResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.dto.response.PagedResponse;
import com.example.employeemanagement.dto.response.PaginationResponse;
import com.example.employeemanagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private EmployeeResponse employeeResponse;

    @BeforeEach
    void setUp() {
        employeeResponse = new EmployeeResponse(
                1L,
                "Sarah Connor",
                LocalDate.of(1990, 1, 1),
                new BigDecimal("100000.00"),
                "123 Tech Lane",
                "Engineer",
                LocalDate.of(2020, 1, 1),
                new BigDecimal("15.00"),
                null,
                null,
                null,
                null
        );
    }

    @Test
    @DisplayName("POST /api/v1/employees - Create Employee Success (200 OK)")
    void createEmployee_Success() {
        EmployeeCreateRequest request = new EmployeeCreateRequest(
                "Sarah Connor",
                LocalDate.of(1990, 5, 15),
                new BigDecimal("125000.00"),
                "123 Tech Lane",
                "Senior Staff Engineer",
                LocalDate.of(2022, 3, 1),
                new BigDecimal("15.50"),
                1L,
                null
        );

        when(employeeService.createEmployee(any())).thenReturn(employeeResponse);

        ResponseEntity<?> response = employeeController.createEmployee(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("GET /api/v1/employees/{id} - Fetch Employee By ID Success (200 OK)")
    void getEmployeeById_Success() {
        when(employeeService.getEmployeeById(1L)).thenReturn(employeeResponse);

        ResponseEntity<?> response = employeeController.getEmployeeById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT /api/v1/employees/{id} - Update Employee Success (200 OK)")
    void updateEmployee_Success() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                "Sarah Connor",
                LocalDate.of(1990, 5, 15),
                new BigDecimal("130000.00"),
                "Updated Address",
                "Principal Engineer",
                LocalDate.of(2022, 3, 1),
                new BigDecimal("20.00"),
                1L
        );

        when(employeeService.updateEmployee(eq(1L), any())).thenReturn(employeeResponse);

        ResponseEntity<?> response = employeeController.updateEmployee(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("PATCH /api/v1/employees/{id}/department - Move Department Success (200 OK)")
    void moveEmployeeDepartment_Success() {
        EmployeeDepartmentUpdateRequest request = new EmployeeDepartmentUpdateRequest(2L);
        when(employeeService.moveEmployeeDepartment(1L, 2L)).thenReturn(employeeResponse);

        ResponseEntity<?> response = employeeController.moveEmployeeDepartment(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET /api/v1/employees - Standard Paginated Employees Fetch (200 OK)")
    void getEmployees_Standard_Success() {
        PagedResponse<EmployeeResponse> pagedResponse = PagedResponse.<EmployeeResponse>builder()
                .items(Collections.singletonList(employeeResponse))
                .pagination(PaginationResponse.builder().page(1).size(20).totalElements(1).totalPages(1).build())
                .build();

        when(employeeService.getEmployees(any())).thenReturn(pagedResponse);

        ResponseEntity<?> response = employeeController.getEmployees(false, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET /api/v1/employees?lookup=true - Lightweight Lookup List Success (200 OK)")
    void getEmployees_Lookup_Success() {
        EmployeeLookupResponse lookupItem = new EmployeeLookupResponse(1L, "Sarah Connor");
        PagedResponse<EmployeeLookupResponse> pagedLookup = PagedResponse.<EmployeeLookupResponse>builder()
                .items(Collections.singletonList(lookupItem))
                .pagination(PaginationResponse.builder().page(1).size(20).totalElements(1).totalPages(1).build())
                .build();

        when(employeeService.getEmployeeLookup(any())).thenReturn(pagedLookup);

        ResponseEntity<?> response = employeeController.getEmployees(true, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
