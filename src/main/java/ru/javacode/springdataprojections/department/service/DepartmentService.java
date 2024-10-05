package ru.javacode.springdataprojections.department.service;

import ru.javacode.springdataprojections.department.dto.CreateDepartmentDto;
import ru.javacode.springdataprojections.department.dto.DepartmentDto;
import ru.javacode.springdataprojections.department.dto.UpdateDepartmentDto;

import java.util.List;

public interface DepartmentService {

    DepartmentDto getDepartmentById(Long departmentId);

    DepartmentDto createDepartment(CreateDepartmentDto createDepartmentDto);

    DepartmentDto updateDepartment(Long departmentId, UpdateDepartmentDto updateDepartmentDto);

    void deleteDepartment(Long departmentId);

    List<DepartmentDto> getAllDepartments();
}

