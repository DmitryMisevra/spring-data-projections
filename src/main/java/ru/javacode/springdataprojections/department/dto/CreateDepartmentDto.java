package ru.javacode.springdataprojections.department.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CreateDepartmentDto {

    @NotBlank(message = "не указано имя департамента")
    private String name;
}
