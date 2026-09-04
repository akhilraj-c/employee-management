package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeemanagement.dto.request.DepartmentUpdateRequest;
import com.example.employeemanagement.dto.response.DepartmentAnalyticsResponse;
import com.example.employeemanagement.dto.response.DepartmentExpandedResponse;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.dto.response.PagedResponse;
import com.example.employeemanagement.dto.response.PaginationResponse;
import com.example.employeemanagement.exception.BusinessException;
import com.example.employeemanagement.service.DepartmentService;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    private DepartmentResponse departmentResponse;

    @BeforeEach
    void setUp() {
        departmentResponse = new DepartmentResponse(1L, "Engineering", null, null, null);
    }

    @Test
    @DisplayName("POST /api/v1/departments - Create Department Success (201 Created)")
    void createDepartment_Success() {
        DepartmentCreateRequest request = new DepartmentCreateRequest("Engineering");
        when(departmentService.createDepartment(any())).thenReturn(departmentResponse);

        ResponseEntity<?> response = departmentController.createDepartment(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("GET /api/v1/departments/{id} - Standard Fetch Success (200 OK)")
    void getDepartmentById_Success() {
        when(departmentService.getDepartmentById(1L)).thenReturn(departmentResponse);

        ResponseEntity<?> response = departmentController.getDepartmentById(1L, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET /api/v1/departments/{id}?expand=employee - Relational Expansion Success (200 OK)")
    void getDepartmentById_ExpandEmployee_Success() {
        DepartmentExpandedResponse expandedResponse = new DepartmentExpandedResponse(1L, "Engineering", null, null, null, null);
        when(departmentService.getDepartmentWithEmployees(eq(1L), any())).thenReturn(expandedResponse);

        ResponseEntity<?> response = departmentController.getDepartmentById(1L, "employee", null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("GET /api/v1/departments/{id}?expand=invalid - Unsupported Expand Throws Exception")
    void getDepartmentById_InvalidExpand_ThrowsException() {
        assertThrows(BusinessException.class, () -> departmentController.getDepartmentById(1L, "invalid", null));
    }

    @Test
    @DisplayName("GET /api/v1/departments - Fetch All Paginated Departments Success (200 OK)")
    void getDepartments_Success() {
        PagedResponse<DepartmentResponse> pagedResponse = PagedResponse.<DepartmentResponse>builder()
                .items(Collections.singletonList(departmentResponse))
                .pagination(PaginationResponse.builder().page(1).size(20).totalElements(1).totalPages(1).build())
                .build();

        when(departmentService.getDepartments(any())).thenReturn(pagedResponse);

        ResponseEntity<?> response = departmentController.getDepartments(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("PUT /api/v1/departments/{id} - Update Department Success (200 OK)")
    void updateDepartment_Success() {
        DepartmentUpdateRequest request = new DepartmentUpdateRequest("Tech & Software", null);
        when(departmentService.updateDepartment(eq(1L), any())).thenReturn(departmentResponse);

        ResponseEntity<?> response = departmentController.updateDepartment(1L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("DELETE /api/v1/departments/{id} - Delete Department Success (200 OK)")
    void deleteDepartment_Success() {
        doNothing().when(departmentService).deleteDepartment(1L);

        ResponseEntity<?> response = departmentController.deleteDepartment(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(departmentService, times(1)).deleteDepartment(1L);
    }

    @Test
    @DisplayName("GET /api/v1/departments/analytics - Department Payroll Analytics Success (200 OK)")
    void getDepartmentAnalytics_Success() {
        DepartmentAnalyticsResponse analytics = new DepartmentAnalyticsResponse(1L, "Engineering", 15L, new BigDecimal("1500000.00"), new BigDecimal("100000.00"), "Alex Mercer");
        PagedResponse<DepartmentAnalyticsResponse> pagedAnalytics = PagedResponse.<DepartmentAnalyticsResponse>builder()
                .items(Collections.singletonList(analytics))
                .pagination(PaginationResponse.builder().page(1).size(20).totalElements(1).totalPages(1).build())
                .build();

        when(departmentService.getDepartmentAnalytics(any())).thenReturn(pagedAnalytics);

        ResponseEntity<?> response = departmentController.getDepartmentAnalytics(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
