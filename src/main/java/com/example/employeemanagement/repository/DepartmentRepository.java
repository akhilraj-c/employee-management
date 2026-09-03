package com.example.employeemanagement.repository;

import com.example.employeemanagement.dto.response.DepartmentAnalyticsResponse;
import com.example.employeemanagement.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByNameIgnoreCase(String name);

    @Query(value = """
        SELECT
            d.id AS departmentId,
            d.name AS departmentName,
            COUNT(e.id) AS headCount,
            COALESCE(SUM(e.salary), 0) AS totalPayrollCost,
            COALESCE(ROUND(AVG(e.salary), 2), 0) AS averageSalary,
            h.name AS departmentHead
        FROM departments d
        LEFT JOIN employees e
            ON e.department_id = d.id
        LEFT JOIN employees h
            ON d.head_employee_id = h.id
        GROUP BY
            d.id,
            d.name,
            h.id,
            h.name
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM departments
        """,
            nativeQuery = true)
    Page<DepartmentAnalyticsResponse> findDepartmentAnalytics(
            Pageable pageable
    );
}