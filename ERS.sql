CREATE USER king
IDENTIFIED BY p4ssw0rd
DEFAULT TABLESPACE users
TEMPORARY TABLESPACE temp
QUOTA 10M ON users;

GRANT connect to king;
GRANT resource to king;
GRANT create session TO king;
GRANT create table TO king;
GRANT create view TO king;

CREATE TABLE employees (
    EmployeeID NUMBER NOT NULL,
    FirstName VARCHAR2(20) NOT NULL,
    LastName VARCHAR2(20) NOT NULL,
    Username VARCHAR2(20) UNIQUE NOT NULL,
    Password VARCHAR2(20) NOT NULL,
    Email VARCHAR2(20),
    CONSTRAINT PK_employees PRIMARY KEY (EmployeeID)
);

CREATE TABLE reimbursements (
    ReimbursementID NUMBER PRIMARY KEY,
    EmployeeID NUMBER NOT NULL,
    Amount NUMBER NOT NULL,
    Approved VARCHAR2(10) NOT NULL,
    CONSTRAINT FK_employeeID FOREIGN KEY (EmployeeID) REFERENCES employees (EmployeeID) ON DELETE CASCADE,
    CONSTRAINT CK_amount CHECK (Amount > 0),
    CONSTRAINT CK_approved CHECK (Approved = 'YES' OR Approved = 'NO' OR Approved = 'PENDING')
);

ALTER TABLE employees ADD securityGroup NUMBER NOT NULL;
ALTER TABLE employees MODIFY(securityGroup NUMBER DEFAULT 2);

CREATE SEQUENCE sq_employeeID_increment
START WITH 1
INCREMENT BY 1;

--DROP SEQUENCE sq_reimbursmentID_increment;

CREATE SEQUENCE sq_reimbursmentID_increment
START WITH 1
INCREMENT BY 1;

CREATE OR REPLACE TRIGGER insert_employee
BEFORE INSERT ON employees
FOR EACH ROW
BEGIN
    SELECT sq_employeeID_increment.NEXTVAL 
    INTO :new.employeeID FROM dual;
END;
/

CREATE OR REPLACE TRIGGER insert_reimbursement
BEFORE INSERT ON reimbursements
FOR EACH ROW
BEGIN
    SELECT sq_reimbursmentID_increment.NEXTVAL 
    INTO :new.reimbursementID FROM dual;
END;
/

CREATE OR REPLACE PROCEDURE
add_employee(   employee_fn IN employees.firstname%TYPE,
                employee_ln IN employees.lastname%TYPE,
                employee_username IN employees.username%TYPE,
                employee_password IN employees.password%TYPE,
                employee_email IN employees.email%TYPE,
                employee_sc IN employees.securitygroup%TYPE)
IS
BEGIN
    IF (employee_email = 'NULL') THEN
        INSERT INTO employees (firstname, lastname, username, employees.password, securitygroup) 
        VALUES (employee_fn, employee_ln, employee_username, employee_password, employee_sc);
    ELSE
        INSERT INTO employees (firstname, lastname, username, employees.password, email, securitygroup) 
        VALUES (employee_fn, employee_ln, employee_username, employee_password, employee_email, employee_sc);
    END IF;
    
    EXCEPTION
        WHEN DUP_VAL_ON_INDEX
        THEN DBMS_OUTPUT.PUT_LINE('That username is not available');
        
    COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE 
add_reimbursements( eid IN employees.employeeID%TYPE,
                    amt IN NUMBER,
                    approved IN reimbursements.approved%TYPE)
IS
BEGIN
    INSERT INTO reimbursements (employeeID, amount, approved) VALUES (eid, amt, approved);
    
    EXCEPTION
        WHEN OTHERS
        THEN DBMS_OUTPUT.PUT_LINE('No record inserted');
    
    COMMIT;
END;
/

CREATE OR REPLACE PROCEDURE
update_reimbursement(   rid IN NUMBER,
                        decision IN reimbursements.approved%TYPE)
IS
BEGIN
    UPDATE reimbursements SET approved=decision WHERE reimbursementid = rid;
END;
/

INSERT INTO employees (firstname, lastname, username, employees.password, securitygroup)
VALUES ('Jonathan', 'Fenstermacher', 'Jfen', 'nope', 1);


INSERT INTO employees (firstname, lastname, username, employees.password, securitygroup)
VALUES ('Danny', 'Ocano', 'Doca', 'pass', 2);

CREATE VIEW manager AS 
SELECT employeeID, SUM(amount) AS TOTAL, (SELECT
    MAX(CASE approved
      WHEN 'PENDING' THEN 1
      ELSE 0
    END)FROM reimbursements GROUP BY employeeID) AS Need_approval
 FROM reimbursements GROUP BY employeeID;

