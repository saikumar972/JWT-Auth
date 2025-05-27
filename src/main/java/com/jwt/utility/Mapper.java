package com.jwt.utility;

import com.jwt.entity.Employee;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.stream.Collectors;

@UtilityClass
public class Mapper {
    public EmployeeDto convertEmployeeToEmployeeDto(Employee employee){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setPassword(employee.getPassword());
        employeeDto.setRoles(Arrays.stream(employee.getRoles().split(",")).toList());
        return employeeDto;
    }

    public Employee convertEmployeeDtoToEmployee(EmployeeDto employeeDto){
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setPassword(employeeDto.getPassword());
        employee.setRoles(employeeDto.getRoles().stream().map(String::toUpperCase).collect(Collectors.joining(",")));
        return employee;
    }
}
