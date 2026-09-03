package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.response.EmployeeLookupResponse;
import com.example.employeemanagement.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByDepartmentId(Long departmentId);

    List<Employee> findByDepartmentId(Long departmentId);
    long countByReportingManagerIsNull();

    @Query("""
        SELECT e.reportingManager.id
        FROM Employee e
        WHERE e.id = :employeeId
        """)
    Optional<Long> findReportingManagerId(
            @Param("employeeId") Long employeeId
    );
    List<Employee> findAllByDepartmentId(Long departmentId);

    @Query("""
        SELECT new com.example.employeemanagement.dto.response.EmployeeLookupResponse(
            e.id,
            e.name
        )
        FROM Employee e
        ORDER BY e.name ASC, e.id ASC
        """)
    Page<EmployeeLookupResponse> findEmployeeLookup(Pageable pageable);

    List<Employee> findAllByDepartmentIdIn(List<Long> departmentIds);
}