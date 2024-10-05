package ru.javacode.springdataprojections.department.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javacode.springdataprojections.department.dto.CreateDepartmentDto;
import ru.javacode.springdataprojections.department.dto.DepartmentDto;
import ru.javacode.springdataprojections.department.dto.UpdateDepartmentDto;
import ru.javacode.springdataprojections.department.service.DepartmentService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/departments")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;


    @GetMapping(path = "/{departmentId}")
    public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable @Min(1) Long departmentId) {
        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

    @PostMapping
    public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody CreateDepartmentDto createDepartmentDto) {
        return new ResponseEntity<>(departmentService.createDepartment(createDepartmentDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{departmentId}")
    public ResponseEntity<DepartmentDto> updateDepartment(@PathVariable @Min(1) Long departmentId,
                                                          @Valid @RequestBody UpdateDepartmentDto updatedDepartmentDto) {
        return ResponseEntity.ok(departmentService.updateDepartment(departmentId, updatedDepartmentDto));
    }

    @DeleteMapping(path = "/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable @Min(1) Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
}
