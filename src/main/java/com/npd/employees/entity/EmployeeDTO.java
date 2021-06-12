package com.npd.employees.entity;

import com.npd.core.entity.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO extends BaseDTO {
    private String name;
    private String lastname;
    private String username;
    private String password;

    private String birthDate;
}
