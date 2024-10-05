package ru.javacode.springdataprojections.employee.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.javacode.springdataprojections.employee.model.Employee;
import ru.javacode.springdataprojections.employee.projection.EmployeeProjection;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e")
    List<EmployeeProjection> findAllProjectedBy();

    @Query("SELECT e FROM Employee e WHERE e.employeeId = :employeeId")
    Optional<EmployeeProjection> findProjectedById(Long employeeId);

    @EntityGraph(attributePaths = {"department"})
    Optional<Employee> findWithAllDetailsByEmployeeId(Long employeeId);
}

