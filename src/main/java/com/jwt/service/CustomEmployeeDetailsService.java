package com.jwt.service;

import com.jwt.dao.EmployeeRepo;
import com.jwt.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomEmployeeDetailsService implements UserDetailsService {

    @Autowired
    EmployeeRepo employeeRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee=employeeRepo.findByName(username).orElseThrow();
        return new CustomEmployeeDetails(employee);
    }
}
