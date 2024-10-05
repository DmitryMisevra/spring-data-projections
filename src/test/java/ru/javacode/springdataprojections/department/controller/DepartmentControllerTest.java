package ru.javacode.springdataprojections.department.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.javacode.springdataprojections.department.dto.CreateDepartmentDto;
import ru.javacode.springdataprojections.department.dto.DepartmentDto;
import ru.javacode.springdataprojections.department.dto.UpdateDepartmentDto;
import ru.javacode.springdataprojections.department.service.DepartmentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Test
    void testGetDepartmentById() throws Exception {
        DepartmentDto departmentDto = DepartmentDto.builder()
                .departmentId(1L)
                .name("HR")
                .build();

        when(departmentService.getDepartmentById(1L)).thenReturn(departmentDto);

        mockMvc.perform(get("/api/v1/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value(1L))
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    void testCreateDepartment() throws Exception {
        CreateDepartmentDto createDepartmentDto = CreateDepartmentDto.builder()
                .name("Finance")
                .build();

        DepartmentDto departmentDto = DepartmentDto.builder()
                .departmentId(1L)
                .name("Finance")
                .build();

        when(departmentService.createDepartment(any(CreateDepartmentDto.class))).thenReturn(departmentDto);

        mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Finance\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.departmentId").value(1L))
                .andExpect(jsonPath("$.name").value("Finance"));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        UpdateDepartmentDto updateDepartmentDto = UpdateDepartmentDto.builder()
                .name("R&D")
                .build();

        DepartmentDto departmentDto = DepartmentDto.builder()
                .departmentId(1L)
                .name("R&D")
                .build();

        when(departmentService.updateDepartment(anyLong(), any(UpdateDepartmentDto.class))).thenReturn(departmentDto);

        mockMvc.perform(put("/api/v1/departments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"R&D\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.departmentId").value(1L))
                .andExpect(jsonPath("$.name").value("R&D"));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/api/v1/departments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllDepartments() throws Exception {
        DepartmentDto departmentDto1 = DepartmentDto.builder()
                .departmentId(1L)
                .name("HR")
                .build();

        DepartmentDto departmentDto2 = DepartmentDto.builder()
                .departmentId(2L)
                .name("Finance")
                .build();

        when(departmentService.getAllDepartments()).thenReturn(List.of(departmentDto1, departmentDto2));

        mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].departmentId").value(1L))
                .andExpect(jsonPath("$[0].name").value("HR"))
                .andExpect(jsonPath("$[1].departmentId").value(2L))
                .andExpect(jsonPath("$[1].name").value("Finance"));
    }
}