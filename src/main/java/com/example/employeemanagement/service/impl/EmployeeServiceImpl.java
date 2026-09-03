package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.request.PaginationRequest;
import com.example.employeemanagement.dto.response.EmployeeLookupResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.dto.response.PagedResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.BusinessException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.EmployeeService;
import com.example.employeemanagement.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeCreateRequest request) {

        validateEmployeeBusinessRulesForCreate(request);

        Department department = getDepartment(request.departmentId());

        Employee manager =  validateReportingManager(request.managerId());

        Employee employee = employeeMapper.toEntity(request);
        employee.setDepartment(department);
        employee.setReportingManager(manager);
        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    private void validateEmployeeBusinessRulesForCreate(
            EmployeeCreateRequest request) {

        if (request.joiningDate()
                .isBefore(request.dateOfBirth())) {

            throw new BusinessException(
                    "Joining date not valid"
            );
        }
    }

    private void validateEmployeeBusinessRulesForUpdate(
            EmployeeUpdateRequest request) {

        if (request.joiningDate()
                .isBefore(request.dateOfBirth())) {

            throw new BusinessException(
                    "Joining date not valid"
            );
        }
    }

    private Employee validateReportingManager(
            Long managerId) {

        long topLevelEmployeeCount = employeeRepository.countByReportingManagerIsNull();

        // First employee
        if (topLevelEmployeeCount == 0) {

            if (managerId != null) {
                throw new BusinessException(
                        "First employee cannot have a reporting manager");
            }

            return null;
        }

        // All other employees
        if (managerId == null) {
            throw new BusinessException(
                    "Reporting manager is required");
        }

        return employeeRepository
                .findById(managerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reporting manager not found"));
    }

    private Department getDepartment(Long departmentId){
        return departmentRepository
                .findById(departmentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Department not found: " + departmentId
                        )
                );
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request) {

        // Find existing employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found: " + employeeId
                        )
                );

        //Validate business rules
        validateEmployeeBusinessRulesForUpdate(request);


        Employee manager =
                validateReportingManagerForUpdate(
                        employee,
                        request.managerId()
                );

        employeeMapper.updateEntity(employee, request);


        employee.setReportingManager(manager);

        // 7. Update audit timestamp
        employee.setUpdatedAt(LocalDateTime.now());

        // 8. Save
        Employee updatedEmployee =
                employeeRepository.save(employee);

        // 9. Return response
        return employeeMapper.toResponse(updatedEmployee);
    }

    private Employee validateReportingManagerForUpdate(
            Employee employee,
            Long managerId) {

        // for top level employee
        if (managerId == null) {

            if (employee.getReportingManager() == null) {
                return null;
            }

            throw new BusinessException(
                    "Reporting manager is required"
            );
        }

        // Self manager not allowed
        if (managerId.equals(employee.getId())) {

            throw new BusinessException(
                    "Employee cannot report to themselves"
            );
        }

        // Check manager exists
        Employee manager =
                employeeRepository.findById(managerId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reporting manager not found: "
                                                + managerId
                                )
                        );

        // Prevent circular reporting hierarchy.

        validateNoCircularReporting(
                employee.getId(),
                manager.getId()
        );

        return manager;
    }

    private void validateNoCircularReporting(
            Long employeeId,
            Long managerId) {

        Long currentId = managerId;

        Set<Long> visited = new HashSet<>();

        while (currentId != null) {

            if (currentId.equals(employeeId)) {
                throw new BusinessException(
                        "Circular reporting hierarchy is not allowed"
                );
            }

            if (!visited.add(currentId)) {
                throw new BusinessException(
                        "Invalid circular reporting hierarchy detected"
                );
            }

            currentId = employeeRepository
                    .findReportingManagerId(currentId)
                    .orElse(null);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Employee not found: " + employeeId
                        )
                );

        return employeeMapper.toResponse(employee);
    }

    @Override
    public EmployeeResponse moveEmployeeDepartment(Long employeeId, Long departmentId) {
        Employee employee =
                employeeRepository.findById(employeeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found: "
                                                + employeeId
                                )
                        );

        Department targetDepartment =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found: "
                                                + departmentId
                                )
                        );

        if (employee.getDepartment()
                .getId()
                .equals(departmentId)) {

            throw new BusinessException(
                    "Employee is already assigned to this department"
            );
        }

        Department currentDepartment = employee.getDepartment();

        if (currentDepartment.getHead() != null
                && currentDepartment.getHead()
                .getId()
                .equals(employeeId)) {

            throw new BusinessException(
                    "Department head cannot be moved to another department"
            );
        }

        employee.setDepartment(targetDepartment);

        employee.setUpdatedAt(LocalDateTime.now());

        Employee updatedEmployee =
                employeeRepository.save(employee);

        return employeeMapper.toResponse(updatedEmployee);
    }


    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EmployeeResponse> getEmployees(
            PaginationRequest request
    ) {
        Pageable pageable = PaginationUtils.toPageable(request);

        Page<EmployeeResponse> page = employeeRepository
                .findAll(pageable)
                .map(employeeMapper::toResponse);

        return PaginationUtils.toPagedResponse(page);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<EmployeeLookupResponse> getEmployeeLookup(PaginationRequest paginationRequest) {
        Pageable pageable = PaginationUtils.toPageable(
                paginationRequest,
                Sort.by(
                        Sort.Order.asc("name"),
                        Sort.Order.asc("id")
                )
        );
        PagedResponse<EmployeeLookupResponse> response = PaginationUtils.toPagedResponse(
                employeeRepository.findEmployeeLookup(pageable)
        );
        return response;
    }
}