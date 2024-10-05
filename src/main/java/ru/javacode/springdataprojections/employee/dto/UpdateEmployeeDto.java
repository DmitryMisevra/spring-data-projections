package ru.javacode.springdataprojections.employee.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateEmployeeDto {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private BigDecimal salary;
    private Long departmentId;
}
