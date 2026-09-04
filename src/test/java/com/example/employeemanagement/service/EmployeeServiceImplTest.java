package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.request.EmployeeCreateRequest;
import com.example.employeemanagement.dto.request.EmployeeUpdateRequest;
import com.example.employeemanagement.dto.response.EmployeeResponse;
import com.example.employeemanagement.entity.Department;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.BusinessException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.mapper.EmployeeMapper;
import com.example.employeemanagement.repository.DepartmentRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Department department;
    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Engineering");

        employee1 = new Employee();
        employee1.setId(1L);
        employee1.setName("Alex Mercer");
        employee1.setDepartment(department);

        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Sarah Connor");
        employee2.setDepartment(department);
        employee2.setReportingManager(employee1);
    }

    @Test
    @DisplayName("Should throw BusinessException when joining date is before date of birth")
    void createEmployee_InvalidDates_ThrowsException() {
        EmployeeCreateRequest request = new EmployeeCreateRequest(
                "John Doe",
                LocalDate.of(2000, 1, 1),
                new BigDecimal("50000"),
                "123 Main St",
                "Engineer",
                LocalDate.of(1995, 1, 1), // Joining date before DOB
                new BigDecimal("10"),
                1L,
                null
        );

        assertThrows(
                BusinessException.class,
                () -> employeeService.createEmployee(request)
        );
    }

    @Test
    @DisplayName("Should throw BusinessException when updating employee to report to themselves")
    void updateEmployee_SelfManager_ThrowsException() {
        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                "Alex Mercer",
                LocalDate.of(1990, 1, 1),
                new BigDecimal("100000"),
                "Address",
                "Manager",
                LocalDate.of(2020, 1, 1),
                new BigDecimal("10"),
                1L // Setting self as manager
        );

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> employeeService.updateEmployee(1L, request)
        );

        assertTrue(exception.getMessage().contains("themselves"));
    }

    @Test
    @DisplayName("Should throw BusinessException when circular reporting is detected")
    void updateEmployee_CircularReporting_ThrowsException() {
        // Emp 1 reports to Emp 2. Now try setting Emp 2 to report to Emp 1.
        employee1.setReportingManager(employee2);

        EmployeeUpdateRequest request = new EmployeeUpdateRequest(
                "Sarah Connor",
                LocalDate.of(1992, 1, 1),
                new BigDecimal("90000"),
                "Address",
                "Engineer",
                LocalDate.of(2021, 1, 1),
                new BigDecimal("10"),
                1L // Making Emp 2 report to Emp 1 while Emp 1 reports to Emp 2
        );

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(employee2));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.findReportingManagerId(1L)).thenReturn(Optional.of(2L));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> employeeService.updateEmployee(2L, request)
        );

        assertTrue(exception.getMessage().contains("Circular reporting hierarchy"));
    }

    @Test
    @DisplayName("Should move employee department successfully using pessimistic locks")
    void moveEmployeeDepartment_Success() {
        Department targetDepartment = new Department();
        targetDepartment.setId(2L);
        targetDepartment.setName("Marketing");

        when(employeeRepository.findByIdWithLock(2L)).thenReturn(Optional.of(employee2));
        when(departmentRepository.findByIdWithLock(2L)).thenReturn(Optional.of(targetDepartment));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee2);

        employeeService.moveEmployeeDepartment(2L, 2L);

        verify(employeeRepository, times(1)).save(employee2);
        assertEquals(targetDepartment, employee2.getDepartment());
    }

    @Test
    @DisplayName("Should throw BusinessException when trying to move department head to another department")
    void moveEmployeeDepartment_DepartmentHead_ThrowsException() {
        department.setHead(employee1); // Emp 1 is head of department 1

        when(employeeRepository.findByIdWithLock(1L)).thenReturn(Optional.of(employee1));
        when(departmentRepository.findByIdWithLock(2L)).thenReturn(Optional.of(department));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> employeeService.moveEmployeeDepartment(1L, 2L)
        );

        assertTrue(exception.getMessage().contains("Department head cannot be moved"));
    }
}
