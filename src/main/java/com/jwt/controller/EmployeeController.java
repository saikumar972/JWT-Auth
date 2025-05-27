package com.jwt.controller;

import com.jwt.service.EmployeeService;
import com.jwt.utility.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;

    @PostMapping("/add")
    public EmployeeDto addEmployee(@RequestBody EmployeeDto employeeDto){
        return employeeService.addEmployeeDetails(employeeDto);
    }

    @GetMapping("/id/{id}")
    public EmployeeDto getEmployeeById(@PathVariable Long id){
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/all")
    public List<EmployeeDto> employeeList(){
        return employeeService.fetchAllEmployees();
    }

    @DeleteMapping("/id/{id}")
    public String deleteEmployeeById(@PathVariable Long id){
        return employeeService.deleteEmployeeId(id);
    }
}
