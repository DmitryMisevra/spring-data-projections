package ru.javacode.springdataprojections.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.javacode.springdataprojections.department.model.Department;
import ru.javacode.springdataprojections.employee.dto.CreateEmployeeDto;
import ru.javacode.springdataprojections.employee.dto.EmployeeDto;
import ru.javacode.springdataprojections.employee.dto.UpdateEmployeeDto;
import ru.javacode.springdataprojections.employee.projection.EmployeeProjection;
import ru.javacode.springdataprojections.employee.service.EmployeeService;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetEmployeeById() throws Exception {
        // Arrange
        EmployeeDto employeeDto = EmployeeDto.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .position("Developer")
                .salary(new BigDecimal("50000"))
                .department(Department.builder().departmentId(1L).name("IT").build())
                .build();

        when(employeeService.getEmployeeById(anyLong())).thenReturn(employeeDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.departmentName").value("IT"));
    }

    @Test
    void testGetEmployeeProjectionById() throws Exception {
        // Arrange
        EmployeeProjection employeeProjection = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "John Doe";
            }

            @Override
            public String getPosition() {
                return "Developer";
            }

            @Override
            public String getDepartmentName() {
                return "IT";
            }
        };

        when(employeeService.getEmployeeProjectionById(anyLong())).thenReturn(employeeProjection);

        // Act & Assert
        mockMvc.perform(get("/api/v1/employees/projections/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.departmentName").value("IT"));
    }

    @Test
    void testCreateEmployee() throws Exception {
        // Arrange
        CreateEmployeeDto createEmployeeDto = CreateEmployeeDto.builder()
                .firstName("John")
                .lastName("Doe")
                .position("Developer")
                .salary(new BigDecimal("50000"))
                .departmentId(1L)
                .build();

        EmployeeDto employeeDto = EmployeeDto.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .position("Developer")
                .department(Department.builder().departmentId(1L).name("IT").build())
                .build();

        when(employeeService.createEmployee(any(CreateEmployeeDto.class))).thenReturn(employeeDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.position").value("Developer"))
                .andExpect(jsonPath("$.departmentName").value("IT"));
    }

    @Test
    void testUpdateEmployee() throws Exception {
        // Arrange
        UpdateEmployeeDto updateEmployeeDto = UpdateEmployeeDto.builder()
                .position("Senior Developer")
                .salary(new BigDecimal("60000"))
                .build();

        EmployeeDto employeeDto = EmployeeDto.builder()
                .employeeId(1L)
                .firstName("John")
                .lastName("Doe")
                .position("Senior Developer")
                .salary(new BigDecimal("60000"))
                .department(Department.builder().departmentId(1L).name("IT").build())
                .build();

        when(employeeService.updateEmployee(anyLong(), any(UpdateEmployeeDto.class))).thenReturn(employeeDto);

        // Act & Assert
        mockMvc.perform(put("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateEmployeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.position").value("Senior Developer"))
                .andExpect(jsonPath("$.departmentName").value("IT"));
    }

    @Test
    void testDeleteEmployee() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllEmployeeProjections() throws Exception {
        // Arrange
        EmployeeProjection employeeProjection = new EmployeeProjection() {
            @Override
            public String getFullName() {
                return "John Doe";
            }

            @Override
            public String getPosition() {
                return "Developer";
            }

            @Override
            public String getDepartmentName() {
                return "IT";
            }
        };

        when(employeeService.getAllEmployeeProjections()).thenReturn(List.of(employeeProjection));

        // Act & Assert
        mockMvc.perform(get("/api/v1/employees/projections")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$[0].position").value("Developer"))
                .andExpect(jsonPath("$[0].departmentName").value("IT"));
    }
}