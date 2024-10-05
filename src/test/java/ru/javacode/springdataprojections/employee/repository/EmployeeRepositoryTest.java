package ru.javacode.springdataprojections.employee.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import ru.javacode.springdataprojections.employee.model.Employee;
import ru.javacode.springdataprojections.employee.projection.EmployeeProjection;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    @Test
    @Sql(statements = {
            "INSERT INTO departments (department_id, department_name) VALUES (1, 'IT')",
            "INSERT INTO employees (employee_id, employee_firstname, employee_lastname, employee_position, employee_salary, department_id) VALUES (1, 'John', 'Doe', 'Developer', 80000, 1)"
    })
    void testFindAllProjectedBy() {
        // Act
        List<EmployeeProjection> employeeProjections = employeeRepository.findAllProjectedBy();

        // Assert
        assertThat(employeeProjections).hasSize(1);
        EmployeeProjection projection = employeeProjections.get(0);
        assertThat(projection.getFullName()).isEqualTo("John Doe");
        assertThat(projection.getPosition()).isEqualTo("Developer");
        assertThat(projection.getDepartmentName()).isEqualTo("IT");
    }

    @Test
    @Sql(statements = {
            "INSERT INTO departments (department_id, department_name) VALUES (1, 'IT')",
            "INSERT INTO employees (employee_id, employee_firstname, employee_lastname, employee_position, employee_salary, department_id) VALUES (1, 'Jane', 'Doe', 'Manager', 90000, 1)"
    })
    void testFindProjectedById() {
        // Act
        Optional<EmployeeProjection> employeeProjection = employeeRepository.findProjectedById(1L);

        // Assert
        assertTrue(employeeProjection.isPresent());
        EmployeeProjection projection = employeeProjection.get();
        assertThat(projection.getFullName()).isEqualTo("Jane Doe");
        assertThat(projection.getPosition()).isEqualTo("Manager");
        assertThat(projection.getDepartmentName()).isEqualTo("IT");
    }

    @Test
    @Sql(statements = {
            "INSERT INTO departments (department_id, department_name) VALUES (1, 'HR')",
            "INSERT INTO employees (employee_id, employee_firstname, employee_lastname, employee_position, employee_salary, department_id) VALUES (1, 'Alice', 'Smith', 'HR Specialist', 70000, 1)"
    })
    void testFindWithAllDetailsByEmployeeId() {
        // Act
        Optional<Employee> employeeOptional = employeeRepository.findWithAllDetailsByEmployeeId(1L);

        // Assert
        assertTrue(employeeOptional.isPresent());
        Employee employee = employeeOptional.get();
        assertNotNull(employee.getDepartment());
        assertThat(employee.getFirstName()).isEqualTo("Alice");
        assertThat(employee.getLastName()).isEqualTo("Smith");
        assertThat(employee.getPosition()).isEqualTo("HR Specialist");
        assertThat(employee.getDepartment().getName()).isEqualTo("HR");
    }
}