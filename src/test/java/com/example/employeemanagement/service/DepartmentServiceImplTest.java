package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.DepartmentCreateRequest;
import com.example.employeemanagement.dto.request.DepartmentUpdateRequest;
import com.example.employeemanagement.dto.response.DepartmentResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.BusinessException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.mapper.DepartmentMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.impl.DepartmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private Department department;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Engineering");
    }

    @Test
    @DisplayName("Should create department successfully when name is unique")
    void createDepartment_Success() {
        DepartmentCreateRequest request = new DepartmentCreateRequest("Engineering");
        DepartmentResponse response = new DepartmentResponse(1L, "Engineering", null, null, null);

        when(departmentRepository.existsByNameIgnoreCase("Engineering")).thenReturn(false);
        when(departmentMapper.toEntity(request)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(department);
        when(departmentMapper.toResponse(department)).thenReturn(response);

        DepartmentResponse result = departmentService.createDepartment(request);

        assertNotNull(result);
        assertEquals("Engineering", result.name());
        verify(departmentRepository, times(1)).save(department);
    }

    @Test
    @DisplayName("Should throw BusinessException when department name already exists")
    void createDepartment_DuplicateName_ThrowsException() {
        DepartmentCreateRequest request = new DepartmentCreateRequest("Engineering");

        when(departmentRepository.existsByNameIgnoreCase("Engineering")).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> departmentService.createDepartment(request)
        );

        assertTrue(exception.getMessage().contains("already exists"));
        verify(departmentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete department successfully when no employees are assigned")
    void deleteDepartment_NoEmployees_Success() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.existsByDepartmentId(1L)).thenReturn(false);

        assertDoesNotThrow(() -> departmentService.deleteDepartment(1L));
        verify(departmentRepository, times(1)).delete(department);
    }

    @Test
    @DisplayName("Should throw BusinessException when deleting department with assigned employees")
    void deleteDepartment_HasEmployees_ThrowsException() {
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(employeeRepository.existsByDepartmentId(1L)).thenReturn(true);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> departmentService.deleteDepartment(1L)
        );

        assertTrue(exception.getMessage().contains("employees are assigned to it"));
        verify(departmentRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when department is not found")
    void getDepartmentById_NotFound_ThrowsException() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> departmentService.getDepartmentById(99L)
        );
    }
}
