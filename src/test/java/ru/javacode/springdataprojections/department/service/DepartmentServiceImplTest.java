package ru.javacode.springdataprojections.department.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.javacode.springdataprojections.department.dto.CreateDepartmentDto;
import ru.javacode.springdataprojections.department.dto.DepartmentDto;
import ru.javacode.springdataprojections.department.dto.UpdateDepartmentDto;
import ru.javacode.springdataprojections.department.mapper.DepartmentMapper;
import ru.javacode.springdataprojections.department.model.Department;
import ru.javacode.springdataprojections.department.repository.DepartmentRepository;
import ru.javacode.springdataprojections.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDepartmentById() {
        // Arrange
        Department department = Department.builder().departmentId(1L).name("HR").build();
        DepartmentDto departmentDto = DepartmentDto.builder().departmentId(1L).name("HR").build();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentMapper.DepartmentToDepartmentDto(department)).thenReturn(departmentDto);

        // Act
        DepartmentDto result = departmentService.getDepartmentById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDepartmentId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("HR");
    }

    @Test
    void testGetDepartmentById_NotFound() {
        // Arrange
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> departmentService.getDepartmentById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Автор с id 1 не найден");
    }

    @Test
    void testCreateDepartment() {
        // Arrange
        CreateDepartmentDto createDepartmentDto = CreateDepartmentDto.builder().name("Finance").build();
        Department department = Department.builder().name("Finance").build();
        Department savedDepartment = Department.builder().departmentId(1L).name("Finance").build();
        DepartmentDto departmentDto = DepartmentDto.builder().departmentId(1L).name("Finance").build();

        when(departmentMapper.createDepartmentDtoToDepartment(createDepartmentDto)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(savedDepartment);
        when(departmentMapper.DepartmentToDepartmentDto(savedDepartment)).thenReturn(departmentDto);

        // Act
        DepartmentDto result = departmentService.createDepartment(createDepartmentDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDepartmentId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Finance");
    }

    @Test
    void testUpdateDepartment() {
        // Arrange
        UpdateDepartmentDto updateDepartmentDto = UpdateDepartmentDto.builder().name("Marketing").build();
        Department department = Department.builder().departmentId(1L).name("Finance").build();
        Department updatedDepartment = Department.builder().departmentId(1L).name("Marketing").build();
        DepartmentDto departmentDto = DepartmentDto.builder().departmentId(1L).name("Marketing").build();

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(departmentRepository.save(department)).thenReturn(updatedDepartment);
        when(departmentMapper.DepartmentToDepartmentDto(updatedDepartment)).thenReturn(departmentDto);

        // Act
        DepartmentDto result = departmentService.updateDepartment(1L, updateDepartmentDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getDepartmentId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Marketing");
    }

    @Test
    void testUpdateDepartment_NotFound() {
        // Arrange
        UpdateDepartmentDto updateDepartmentDto = UpdateDepartmentDto.builder().name("Marketing").build();
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> departmentService.updateDepartment(1L, updateDepartmentDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Автор с id 1 не найден");
    }

    @Test
    void testDeleteDepartment() {
        // Arrange
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(Department.builder().departmentId(1L).build()));
        doNothing().when(departmentRepository).deleteById(1L);

        // Act
        departmentService.deleteDepartment(1L);

        // Assert
        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAllDepartments() {
        // Arrange
        Department department1 = Department.builder().departmentId(1L).name("HR").build();
        Department department2 = Department.builder().departmentId(2L).name("Finance").build();
        DepartmentDto departmentDto1 = DepartmentDto.builder().departmentId(1L).name("HR").build();
        DepartmentDto departmentDto2 = DepartmentDto.builder().departmentId(2L).name("Finance").build();

        when(departmentRepository.findAll()).thenReturn(List.of(department1, department2));
        when(departmentMapper.DepartmentToDepartmentDto(department1)).thenReturn(departmentDto1);
        when(departmentMapper.DepartmentToDepartmentDto(department2)).thenReturn(departmentDto2);

        // Act
        List<DepartmentDto> result = departmentService.getAllDepartments();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).extracting(DepartmentDto::getDepartmentId).containsExactlyInAnyOrder(1L, 2L);
    }
}