package ru.javacode.springdataprojections.department.mapper;

import org.springframework.stereotype.Component;
import ru.javacode.springdataprojections.department.dto.CreateDepartmentDto;
import ru.javacode.springdataprojections.department.dto.DepartmentDto;
import ru.javacode.springdataprojections.department.model.Department;

@Component
public class DepartmentMapper {

    public Department createDepartmentDtoToDepartment(CreateDepartmentDto createDepartmentDto) {
        return Department.builder()
                .name(createDepartmentDto.getName())
                .build();
    }

    public DepartmentDto DepartmentToDepartmentDto(Department department) {
        return DepartmentDto.builder()
                .departmentId(department.getDepartmentId())
                .name(department.getName())
                .build();
    }
}

