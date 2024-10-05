package ru.javacode.springdataprojections.department.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javacode.springdataprojections.department.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
