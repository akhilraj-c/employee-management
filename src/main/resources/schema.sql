-- Disable foreign key checks for dropping existing tables
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS employees;
DROP TABLE IF EXISTS departments;
SET FOREIGN_KEY_CHECKS = 1;

-- Table: departments
CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    head_employee_id BIGINT NULL,
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT uk_department_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Table: employees
CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    date_of_birth DATE NULL,
    salary DECIMAL(15, 2) NOT NULL,
    address VARCHAR(255) NULL,
    role VARCHAR(255) NULL,
    joining_date DATE NULL,
    yearly_bonus_percentage DECIMAL(5, 2) NULL,
    department_id BIGINT NOT NULL,
    reporting_manager_id BIGINT NULL,
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    CONSTRAINT fk_employee_department FOREIGN KEY (department_id) REFERENCES departments (id),
    CONSTRAINT fk_employee_manager FOREIGN KEY (reporting_manager_id) REFERENCES employees (id),
    INDEX idx_employee_department (department_id),
    INDEX idx_employee_manager (reporting_manager_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Add foreign key constraint for department head back-reference
ALTER TABLE departments
    ADD CONSTRAINT fk_department_head FOREIGN KEY (head_employee_id) REFERENCES employees (id);