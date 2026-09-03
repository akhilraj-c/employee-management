package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.request.PaginationRequest;
import com.example.employeemanagement.dto.response.EmployeeLookupResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.dto.response.PagedResponse;


public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request
    );

    EmployeeResponse getEmployeeById(Long employeeId);


    EmployeeResponse moveEmployeeDepartment(
            Long employeeId,
            Long departmentId
    );

    PagedResponse<EmployeeResponse> getEmployees(
            PaginationRequest request
    );

    PagedResponse<EmployeeLookupResponse> getEmployeeLookup(PaginationRequest paginationRequest);

}