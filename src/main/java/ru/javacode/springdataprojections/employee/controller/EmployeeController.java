package ru.javacode.springdataprojections.employee.controller;

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
import ru.javacode.springdataprojections.employee.dto.CreateEmployeeDto;
import ru.javacode.springdataprojections.employee.dto.EmployeeDto;
import ru.javacode.springdataprojections.employee.dto.UpdateEmployeeDto;
import ru.javacode.springdataprojections.employee.projection.EmployeeProjection;
import ru.javacode.springdataprojections.employee.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable @Min(1) Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @GetMapping(path = "/projections/{employeeId}")
    public ResponseEntity<EmployeeProjection> getEmployeeProjectionById(@PathVariable @Min(1) Long employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeProjectionById(employeeId));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody CreateEmployeeDto createEmployeeDto) {
        return new ResponseEntity<>(employeeService.createEmployee(createEmployeeDto), HttpStatus.CREATED);
    }

    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable @Min(1) Long employeeId,
                                                      @Valid @RequestBody UpdateEmployeeDto updatedEmployeeDto) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, updatedEmployeeDto));
    }

    @DeleteMapping(path = "/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable @Min(1) Long employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/projections")
    public ResponseEntity<List<EmployeeProjection>> getAllEmployeeProjections() {
        return ResponseEntity.ok(employeeService.getAllEmployeeProjections());
    }
}
