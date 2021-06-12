package com.npd.employees.service;

import com.npd.core.service.BaseService;
import com.npd.employees.entity.Employee;
import com.npd.employees.entity.EmployeeDTO;
import org.springframework.beans.factory.annotation.Qualifier;


public interface EmployeeService extends BaseService<Employee, EmployeeDTO>{

}
