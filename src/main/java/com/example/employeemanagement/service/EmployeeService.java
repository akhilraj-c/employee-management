package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.request.PaginationRequest;
import com.example.employeemanagement.dto.response.EmployeeLookupResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.dto.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request
    );

    EmployeeResponse getEmployeeById(Long employeeId);

    void deleteEmployee(Long employeeId);

    EmployeeResponse moveEmployeeDepartment(
            Long employeeId,
            Long departmentId
    );

    PagedResponse<EmployeeResponse> getEmployees(
            PaginationRequest request
    );

    List<EmployeeLookupResponse> getEmployeeLookup();

}