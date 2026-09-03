-- -------------------------------------------------------------
-- 1. Departments
-- -------------------------------------------------------------

INSERT INTO departments (id, name, head_employee_id, created_at, updated_at) VALUES
(1, 'Engineering', NULL, NOW(), NOW()),
(2, 'Human Resources', NULL, NOW(), NOW()),
(3, 'Finance', NULL, NOW(), NOW());


-- -------------------------------------------------------------
-- 2. Employees - 25 employees
-- -------------------------------------------------------------

INSERT INTO employees (id, name, date_of_birth, salary, address, role, joining_date, yearly_bonus_percentage, department_id, reporting_manager_id, created_at, updated_at) VALUES
(1, 'Arjun Nair', '1985-03-15', 145000.00, 'Kowdiar, Thiruvananthapuram, Kerala', 'Engineering Director', '2015-06-01', 15.00, 1, NULL, NOW(), NOW()),
(2, 'Fathima Basheer', '1988-07-22', 120000.00, 'Mavoor Road, Kozhikode, Kerala', 'Lead Software Engineer', '2017-02-15', 12.50, 1, 1, NOW(), NOW()),
(3, 'Rahul Mathew', '1992-11-10', 95000.00, 'Kakkanad, Kochi, Kerala', 'Senior Backend Engineer', '2019-08-10', 10.00, 1, 2, NOW(), NOW()),
(4, 'Anjali Menon', '1995-04-05', 88000.00, 'Vyttila, Kochi, Kerala', 'Frontend Engineer', '2021-01-20', 8.50, 1, 2, NOW(), NOW()),
(5, 'Shahul Hameed', '1994-09-18', 92000.00, 'Kondotty, Malappuram, Kerala', 'DevOps Engineer', '2020-04-12', 9.00, 1, 2, NOW(), NOW()),
(6, 'Meera Thomas', '1997-01-30', 75000.00, 'Kottayam, Kerala', 'Junior Software Engineer', '2022-07-01', 6.00, 1, 3, NOW(), NOW()),
(7, 'Vivek Krishnan', '1991-06-25', 105000.00, 'Pattom, Thiruvananthapuram, Kerala', 'QA Lead', '2018-11-05', 11.00, 1, 1, NOW(), NOW()),
(8, 'Aiswarya Suresh', '1996-12-14', 72000.00, 'Thrissur, Kerala', 'QA Engineer', '2023-03-15', 5.00, 1, 7, NOW(), NOW()),
(9, 'Nikhil Joseph', '1990-08-08', 130000.00, 'Kochi, Kerala', 'Principal Architect', '2016-09-01', 14.00, 1, 1, NOW(), NOW()),
(10, 'Sneha Pillai', '1993-05-19', 98000.00, 'Alappuzha, Kerala', 'Data Engineer', '2020-10-01', 10.00, 1, 9, NOW(), NOW()),

(11, 'Anand Kumar', '1986-10-12', 115000.00, 'Kowdiar, Thiruvananthapuram, Kerala', 'HR Director', '2016-01-10', 12.00, 2, 1, NOW(), NOW()),
(12, 'Amina Nazeer', '1989-02-28', 85000.00, 'Nadakkavu, Kozhikode, Kerala', 'Senior Recruiter', '2018-05-20', 8.00, 2, 11, NOW(), NOW()),
(13, 'Joseph George', '1994-07-04', 68000.00, 'Changanassery, Kottayam, Kerala', 'HR Generalist', '2021-03-15', 6.00, 2, 11, NOW(), NOW()),
(14, 'Lakshmi Warrier', '1992-12-01', 78000.00, 'Ottapalam, Palakkad, Kerala', 'Payroll Specialist', '2019-09-01', 7.50, 2, 11, NOW(), NOW()),
(15, 'Rashid Ali', '1996-03-22', 62000.00, 'Tirur, Malappuram, Kerala', 'Technical Recruiter', '2022-11-10', 5.00, 2, 12, NOW(), NOW()),
(16, 'Devika Rajan', '1998-08-15', 55000.00, 'Kollam, Kerala', 'HR Coordinator', '2023-06-01', 4.00, 2, 13, NOW(), NOW()),
(17, 'Maria Elizabeth', '1993-11-30', 72000.00, 'Angamaly, Ernakulam, Kerala', 'Benefits Administrator', '2020-08-15', 7.00, 2, 14, NOW(), NOW()),

(18, 'Sreeram Menon', '1984-05-05', 135000.00, 'Vazhuthacaud, Thiruvananthapuram, Kerala', 'Finance Director', '2014-04-01', 15.00, 3, 1, NOW(), NOW()),
(19, 'Haris Rahman', '1987-07-04', 110000.00, 'Feroke, Kozhikode, Kerala', 'Senior Financial Analyst', '2017-10-15', 11.50, 3, 18, NOW(), NOW()),
(20, 'Thomas Kurian', '1983-05-29', 125000.00, 'Kanjirappally, Kottayam, Kerala', 'Tax & Audit Manager', '2015-11-01', 13.00, 3, 18, NOW(), NOW()),
(21, 'Parvathy Nambiar', '1991-04-29', 82000.00, 'Cherthala, Alappuzha, Kerala', 'Senior Accountant', '2019-03-20', 8.00, 3, 20, NOW(), NOW()),
(22, 'Faisal Kareem', '1995-10-10', 74000.00, 'Manjeri, Malappuram, Kerala', 'Financial Analyst', '2021-08-01', 7.00, 3, 19, NOW(), NOW()),
(23, 'Greeshma Babu', '1996-02-10', 68000.00, 'Irinjalakuda, Thrissur, Kerala', 'Staff Accountant', '2022-02-15', 6.00, 3, 21, NOW(), NOW()),
(24, 'Mathew Abraham', '1990-10-02', 90000.00, 'Perumbavoor, Ernakulam, Kerala', 'Treasury Manager', '2018-07-12', 9.50, 3, 18, NOW(), NOW()),
(25, 'Hiba Salim', '1999-02-14', 58000.00, 'Tirur, Malappuram, Kerala', 'Junior Accountant', '2024-01-10', 4.00, 3, 21, NOW(), NOW());


-- -------------------------------------------------------------
-- 3. Update Department Heads
-- -------------------------------------------------------------

UPDATE departments SET head_employee_id = 1 WHERE id = 1;
UPDATE departments SET head_employee_id = 11 WHERE id = 2;
UPDATE departments SET head_employee_id = 18 WHERE id = 3;