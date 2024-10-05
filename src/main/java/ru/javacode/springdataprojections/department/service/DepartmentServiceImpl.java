package ru.javacode.springdataprojections.department.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.javacode.springdataprojections.department.dto.CreateDepartmentDto;
import ru.javacode.springdataprojections.department.dto.DepartmentDto;
import ru.javacode.springdataprojections.department.dto.UpdateDepartmentDto;
import ru.javacode.springdataprojections.department.mapper.DepartmentMapper;
import ru.javacode.springdataprojections.department.model.Department;
import ru.javacode.springdataprojections.department.repository.DepartmentRepository;
import ru.javacode.springdataprojections.exception.ResourceNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public DepartmentDto getDepartmentById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() ->
                new ResourceNotFoundException("Автор с id " + departmentId + " не найден"));
        return departmentMapper.DepartmentToDepartmentDto(department);
    }

    @Override
    public DepartmentDto createDepartment(CreateDepartmentDto createDepartmentDto) {
        Department department = departmentMapper.createDepartmentDtoToDepartment(createDepartmentDto);
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.DepartmentToDepartmentDto(savedDepartment);
    }

    @Override
    public DepartmentDto updateDepartment(Long departmentId, UpdateDepartmentDto updateDepartmentDto) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() ->
                new ResourceNotFoundException("Автор с id " + departmentId + " не найден"));
        if (updateDepartmentDto.getName() != null) {
            department.setName(updateDepartmentDto.getName());
        }
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.DepartmentToDepartmentDto(savedDepartment);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        departmentRepository.deleteById(departmentId);
    }

    @Override
    public List<DepartmentDto> getAllDepartments() {
        return departmentRepository.findAll().stream().map(departmentMapper::DepartmentToDepartmentDto).toList();
    }
}
