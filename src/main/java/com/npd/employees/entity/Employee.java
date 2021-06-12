package com.npd.employees.entity;

import com.npd.core.entity.Base;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
@AllArgsConstructor @NoArgsConstructor @SuperBuilder
public class Employee extends Base {

    private String name;
    private String lastname;
    private String username;
    private String password;

    private String birthDate;
}
