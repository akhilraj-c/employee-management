package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeemanagement.dto.request.DepartmentUpdateRequest;
import com.example.employeemanagement.dto.request.PaginationRequest;
import com.example.employeemanagement.dto.response.DepartmentAnalyticsResponse;
import com.example.employeemanagement.dto.response.DepartmentExpandedResponse;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface DepartmentService {

    DepartmentResponse createDepartment(
            DepartmentCreateRequest request
    );

    DepartmentResponse getDepartmentById(
            Long departmentId
    );

    PagedResponse<DepartmentResponse> getDepartments(
            PaginationRequest request
    );

    DepartmentResponse updateDepartment(
            Long departmentId,
            DepartmentUpdateRequest request
    );

    void deleteDepartment(
            Long departmentId
    );
    DepartmentExpandedResponse getDepartmentWithEmployees(
            Long departmentId
    );

    PagedResponse<DepartmentAnalyticsResponse> getDepartmentAnalytics(
            PaginationRequest pageable
    );
}