package ru.javacode.springdataprojections.employee.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllEmployeeProjections() {
        // Arrange
        EmployeeProjection projection = mock(EmployeeProjection.class);
        when(employeeRepository.findAllProjectedBy()).thenReturn(List.of(projection));

        // Act
        List<EmployeeProjection> result = employeeService.getAllEmployeeProjections();

        // Assert
        assertThat(result).hasSize(1);
        verify(employeeRepository, times(1)).findAllProjectedBy();
    }

    @Test
    void testGetEmployeeById() {
        // Arrange
        Employee employee = Employee.builder().employeeId(1L).firstName("John").lastName("Doe").build();
        when(employeeRepository.findWithAllDetailsByEmployeeId(anyLong())).thenReturn(Optional.of(employee));
        EmployeeDto employeeDto = EmployeeDto.builder().employeeId(1L).firstName("John").lastName("Doe").build();
        when(employeeMapper.EmployeeToEmployeeDto(any(Employee.class))).thenReturn(employeeDto);

        // Act
        EmployeeDto result = employeeService.getEmployeeById(1L);

        // Assert
        assertThat(result.getEmployeeId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).findWithAllDetailsByEmployeeId(anyLong());
    }

    @Test
    void testGetEmployeeById_NotFound() {
        // Arrange
        when(employeeRepository.findWithAllDetailsByEmployeeId(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Сотрудник с id 1 не найден");
        verify(employeeRepository, times(1)).findWithAllDetailsByEmployeeId(anyLong());
    }

    @Test
    void testCreateEmployee() {
        // Arrange
        CreateEmployeeDto createEmployeeDto = CreateEmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .position("Developer")
                .salary(BigDecimal.valueOf(50000))
                .departmentId(1L)
                .build();
        Department department = Department.builder().departmentId(1L).name("IT").build();
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        Employee employee = Employee.builder().employeeId(1L).firstName("John").lastName("Doe").department(department).build();
        when(employeeMapper.createEmployeeToEmployee(any(CreateEmployeeDto.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        EmployeeDto employeeDto = EmployeeDto.builder().employeeId(1L).firstName("John").lastName("Doe").build();
        when(employeeMapper.EmployeeToEmployeeDto(any(Employee.class))).thenReturn(employeeDto);

        // Act
        EmployeeDto result = employeeService.createEmployee(createEmployeeDto);

        // Assert
        assertThat(result.getEmployeeId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee() {
        // Arrange
        UpdateEmployeeDto updateEmployeeDto = UpdateEmployeeDto.builder().firstName("Jane").build();
        Employee employee = Employee.builder().employeeId(1L).firstName("John").build();
        when(employeeRepository.findWithAllDetailsByEmployeeId(anyLong())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        EmployeeDto employeeDto = EmployeeDto.builder().employeeId(1L).firstName("Jane").build();
        when(employeeMapper.EmployeeToEmployeeDto(any(Employee.class))).thenReturn(employeeDto);

        // Act
        EmployeeDto result = employeeService.updateEmployee(1L, updateEmployeeDto);

        // Assert
        assertThat(result.getFirstName()).isEqualTo("Jane");
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        // Act
        employeeService.deleteEmployee(1L);

        // Assert
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        // Arrange
        doThrow(new EmptyResultDataAccessException(1)).when(employeeRepository).deleteById(anyLong());

        // Act & Assert
        assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }
}