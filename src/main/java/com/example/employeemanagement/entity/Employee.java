package com.example.employeemanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "employees",
        indexes = {
                @Index(name = "idx_employee_department", columnList = "department_id"),
                @Index(name = "idx_employee_manager", columnList = "reporting_manager_id")
        }
)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal salary;

    private String address;

    private String role;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "yearly_bonus_percentage",
            precision = 5,
            scale = 2)
    private BigDecimal yearlyBonusPercentage;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "reporting_manager_id")
    private Employee reportingManager;
}