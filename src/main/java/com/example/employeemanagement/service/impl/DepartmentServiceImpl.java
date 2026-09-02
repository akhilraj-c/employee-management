package com.example.employeemanagement.service.impl;

import com.example.employeemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeemanagement.dto.request.DepartmentUpdateRequest;
import com.example.employeemanagement.dto.request.PaginationRequest;
import com.example.employeemanagement.dto.response.DepartmentExpandedResponse;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.dto.response.PagedResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.BusinessException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.mapper.DepartmentMapper;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.DepartmentService;
import com.example.employeemanagement.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public DepartmentResponse createDepartment(
            DepartmentCreateRequest request
    ) {

        // Prevent duplicate department names
        if (departmentRepository.existsByNameIgnoreCase(request.name())) {
            throw new BusinessException(
                    "Department with name '" + request.name() +"' already exists"
            );
        }

        Department department = departmentMapper.toEntity(request);


        try {
            Department savedDepartment = departmentRepository.save(department);
            return departmentMapper.toResponse(savedDepartment);

        } catch (DataIntegrityViolationException ex) {
            // Protect against duplicate creation caused by concurrent requests
            throw new BusinessException(
                    "Department already exists: " + request.name()
            );
        }

    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(
            Long departmentId
    ) {

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found: "
                                                + departmentId
                                )
                        );

        return departmentMapper.toResponse(department);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<DepartmentResponse> getDepartments(
            PaginationRequest request
    ) {
        Pageable pageable = PaginationUtils.toPageable(request);

        Page<DepartmentResponse> page = departmentRepository
                .findAll(pageable)
                .map(departmentMapper::toResponse);

        return PaginationUtils.toPagedResponse(page);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(
            Long departmentId,
            DepartmentUpdateRequest request
    ) {

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found with id: "
                                                + departmentId
                                )
                        );

        String departmentName = request.name().trim();

        //Check duplicate department name
        if (!department.getName().equalsIgnoreCase(departmentName)
                && departmentRepository.existsByNameIgnoreCase(
                departmentName
        )) {

            throw new BusinessException(
                    "Department already exists with name: "
                            + departmentName
            );
        }

        // Find and validate department head
        Employee head = null;

        if (request.headEmployeeId() != null) {

            head = employeeRepository.findById(
                            request.headEmployeeId()
                    )
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Head employee not found: "
                                            + request.headEmployeeId()
                            )
                    );

            // Head must belong to this department
            if (head.getDepartment() == null
                    || !head.getDepartment()
                    .getId()
                    .equals(departmentId)) {

                throw new BusinessException(
                        "Head employee must belong to the department"
                );
            }
        }

        department.setName(departmentName);
        department.setHead(head);

        Department updatedDepartment =
                departmentRepository.save(department);


        return departmentMapper.toResponse(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long departmentId) {

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found: "
                                                + departmentId
                                )
                        );


        boolean hasEmployees =
                employeeRepository.existsByDepartmentId(
                        departmentId
                );

        if (hasEmployees) {
            throw new BusinessException(
                    "Department cannot be deleted because "
                            + "employees are assigned to it"
            );
        }

        departmentRepository.delete(department);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentExpandedResponse getDepartmentWithEmployees(
            Long departmentId
    ) {

        Department department =
                departmentRepository.findById(departmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Department not found: "
                                                + departmentId
                                )
                        );

        List<EmployeeResponse> employees =
                employeeRepository
                        .findAllByDepartmentId(departmentId)
                        .stream()
                        .map(employeeMapper::toResponse)
                        .toList();

        return departmentMapper.toExpandedResponse(
                department,
                employees
        );
    }
}