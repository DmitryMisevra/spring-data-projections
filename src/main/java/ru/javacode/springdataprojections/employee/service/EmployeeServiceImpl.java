package ru.javacode.springdataprojections.employee.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javacode.springdataprojections.department.model.Department;
import ru.javacode.springdataprojections.department.repository.DepartmentRepository;
import ru.javacode.springdataprojections.employee.dto.CreateEmployeeDto;
import ru.javacode.springdataprojections.employee.dto.EmployeeDto;
import ru.javacode.springdataprojections.employee.dto.UpdateEmployeeDto;
import ru.javacode.springdataprojections.employee.model.Employee;
import ru.javacode.springdataprojections.employee.projection.EmployeeProjection;
import ru.javacode.springdataprojections.employee.repository.EmployeeRepository;
import ru.javacode.springdataprojections.exception.ResourceNotFoundException;
import ru.javacode.springdataprojections.mapper.EmployeeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final DepartmentRepository departmentRepository;


    @Override
    public List<EmployeeProjection> getAllEmployeeProjections() {
        return employeeRepository.findAllProjectedBy();
    }

    @Override
    public EmployeeDto getEmployeeById(Long employeeId) {
        Employee employee = employeeRepository.findWithAllDetailsByEmployeeId(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Сотрудник с id " + employeeId + " не найден"));
        return employeeMapper.EmployeeToEmployeeDto(employee);
    }


    @Override
    public EmployeeProjection getEmployeeProjectionById(Long employeeId) {
        return employeeRepository.findProjectedById(employeeId).orElseThrow(() -> new ResourceNotFoundException(
                "Сотрудник с id " + employeeId + " не найден"));
    }

    @Transactional
    @Override
    public EmployeeDto createEmployee(CreateEmployeeDto createEmployeeDto) {
        Long departmentId = createEmployeeDto.getDepartmentId();
        Department department = departmentRepository.findById(departmentId).orElseThrow(() ->
                new ResourceNotFoundException("Сотрудник с id " + departmentId + " не найден"));
        Employee employee = employeeMapper.createEmployeeToEmployee(createEmployeeDto);
        employee.setDepartment(department);
        Employee savedEmployee = employeeRepository.save(employee);
        return getEmployeeById(savedEmployee.getEmployeeId());
    }

    @Transactional
    @Override
    public EmployeeDto updateEmployee(Long employeeId, UpdateEmployeeDto updateEmployeeDto) {
        Employee employee = employeeRepository.findWithAllDetailsByEmployeeId(employeeId).orElseThrow(() ->
                new ResourceNotFoundException("Сотрудник с id " + employeeId + " не найден"));
        updateEmployee(employee, updateEmployeeDto);
        return getEmployeeById(employeeId);
    }

    @Transactional
    @Override
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    private void updateEmployee(Employee employee, UpdateEmployeeDto updateEmployeeDto) {
        if (updateEmployeeDto.getFirstName() != null) {
            employee.setFirstName(updateEmployeeDto.getFirstName());
        }
        if (updateEmployeeDto.getLastName() != null) {
            employee.setLastName(updateEmployeeDto.getLastName());
        }
        if (updateEmployeeDto.getPosition() != null) {
            employee.setPosition(updateEmployeeDto.getPosition());
        }
        if (updateEmployeeDto.getSalary() != null) {
            employee.setSalary(updateEmployeeDto.getSalary());
        }
        if (updateEmployeeDto.getDepartmentId() != null) {
            Long departmentId = updateEmployeeDto.getDepartmentId();
            Department department = departmentRepository.findById(departmentId).orElseThrow(() ->
                    new ResourceNotFoundException("Сотрудник с id " + departmentId + " не найден"));
            employee.setDepartment(department);
        }
        employeeRepository.save(employee);
    }
}
