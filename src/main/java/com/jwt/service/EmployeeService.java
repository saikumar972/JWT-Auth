package com.jwt.service;

import com.jwt.dao.EmployeeRepo;
import com.jwt.entity.Employee;
import com.jwt.utility.EmployeeDto;
import com.jwt.utility.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    public EmployeeDto addEmployeeDetails(EmployeeDto employeeDto){
        Employee employee=Mapper.convertEmployeeDtoToEmployee(employeeDto);
        employee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        Employee employee1=employeeRepo.save(employee);
        return Mapper.convertEmployeeToEmployeeDto(employee1);
    }

    public List<EmployeeDto> fetchAllEmployees(){
        List<Employee> employeeList=employeeRepo.findAll();
        List<EmployeeDto> employeeDtoList=employeeList.stream()
                .map(Mapper::convertEmployeeToEmployeeDto).toList();
        return employeeDtoList;
    }

    public EmployeeDto getEmployeeById(Long id){
        Employee employee=employeeRepo.findById(id).orElseThrow(()->new IllegalArgumentException("Invalid id"));
        return Mapper.convertEmployeeToEmployeeDto(employee);
    }

    public String deleteEmployeeId(Long id){
        employeeRepo.deleteById(id);
        return "Employee with "+id+" "+"is deleted successfully";
    }
}
