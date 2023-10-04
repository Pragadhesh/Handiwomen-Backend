package com.example.handiwomen.service.Employee.impl;

import com.example.handiwomen.request.Employeedata;
import org.springframework.http.ResponseEntity;

public interface EmployeeService {


    // Method to add Employee
    ResponseEntity<Object> addEmployee(Employeedata employeedata);

    // Method to get all Employees
    ResponseEntity<Object> getEmployees();

    ResponseEntity<Object> getEmployee(Long id);

}