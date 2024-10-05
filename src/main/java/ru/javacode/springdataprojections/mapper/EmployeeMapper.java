package ru.javacode.springdataprojections.mapper;

import org.springframework.stereotype.Component;
import ru.javacode.springdataprojections.employee.dto.CreateEmployeeDto;
import ru.javacode.springdataprojections.employee.dto.EmployeeDto;
import ru.javacode.springdataprojections.employee.model.Employee;

@Component
public class EmployeeMapper {

    public Employee createEmployeeToEmployee(CreateEmployeeDto createEmployeeDto) {
        return Employee.builder()
                .firstName(createEmployeeDto.getFirstName())
                .lastName(createEmployeeDto.getLastName())
                .position(createEmployeeDto.getPosition())
                .salary(createEmployeeDto.getSalary())
                .build();
    }

    public EmployeeDto EmployeeToEmployeeDto(Employee employee) {
        return EmployeeDto.builder()
                .employeeId(employee.getEmployeeId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .department(employee.getDepartment())
                .build();
    }
}
