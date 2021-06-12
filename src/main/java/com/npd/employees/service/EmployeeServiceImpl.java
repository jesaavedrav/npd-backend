package com.npd.employees.service;

import com.npd.core.repository.BaseRepository;
import com.npd.core.service.BaseServiceImpl;
import com.npd.employees.entity.Employee;
import com.npd.employees.entity.EmployeeDTO;
import com.npd.employees.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends BaseServiceImpl<Employee, EmployeeDTO> implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected BaseRepository getRepository() {
        return employeeRepository;
    }

    @Override
    protected Class<Employee> getManagedEntityClass() {
        return Employee.class;
    }

    @Override
    protected Class<EmployeeDTO> getManagedDTOClass() {
        return EmployeeDTO.class;
    }

    @Override
    protected void copySpecificFromDTOToEntity(Employee entity, EmployeeDTO dtoObject) {
        // nothing specific
    }
}
