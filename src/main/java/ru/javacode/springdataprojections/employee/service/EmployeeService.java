package ru.javacode.springdataprojections.employee.service;

import ru.javacode.springdataprojections.employee.dto.CreateEmployeeDto;
import ru.javacode.springdataprojections.employee.dto.EmployeeDto;
import ru.javacode.springdataprojections.employee.dto.UpdateEmployeeDto;
import ru.javacode.springdataprojections.employee.projection.EmployeeProjection;

import java.util.List;

public interface EmployeeService {

    List<EmployeeProjection> getAllEmployeeProjections();

    EmployeeDto getEmployeeById(Long employeeId);

    EmployeeProjection getEmployeeProjectionById(Long employeeId);

    EmployeeDto createEmployee(CreateEmployeeDto createEmployeeDto);

    EmployeeDto updateEmployee(Long employeeId, UpdateEmployeeDto updateEmployeeDto);

    void deleteEmployee(Long employeeId);

}