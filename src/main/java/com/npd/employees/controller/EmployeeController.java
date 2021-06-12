package com.npd.employees.controller;

import com.npd.core.controller.BaseControllerImpl;
import com.npd.employees.entity.Employee;
import com.npd.employees.entity.EmployeeDTO;
import com.npd.employees.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController extends BaseControllerImpl<Employee, EmployeeDTO, EmployeeService> {

    private EmployeeService employeeService;

    @Autowired
    EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @Override
    public EmployeeService getService() {
        return employeeService;
    }

}
