package com.example.handiwomen.controller;

import com.example.handiwomen.request.Employeedata;

import com.example.handiwomen.service.Employee.impl.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;

@RestController
@Validated
@CrossOrigin(origins = "*")
public class EmployeeController {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    EmployeeService employeeService;

    @Autowired
    EmployeeController(EmployeeService employeeService)
    {
        this.employeeService = employeeService;
    }


    @PostMapping("/addemployee")
    public ResponseEntity<Object> addEmployee(@Valid @RequestBody Employeedata employeedata )
    {
        LOG.debug("Calling addEmployee method from addEmployeeApi...");
        try {
            return employeeService.addEmployee(employeedata);
        }
        catch (Exception exception)
        {
            LOG.error("{}", "Invalid status" + employeedata);
            return new ResponseEntity(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/employee")
    public ResponseEntity<Object> getEmployees()
    {
        LOG.debug("Calling getEmployees method from getEmployees...");
        try {
            return employeeService.getEmployees();
        }
        catch (Exception exception)
        {
            LOG.error("{}", "Invalid status");
            return new ResponseEntity(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<Object> getEmployee(@PathVariable Long id)
    {
        LOG.debug("Calling getEmployee method from getEmployee...");
        try {
            return employeeService.getEmployee(id);
        }
        catch (Exception exception)
        {
            LOG.error("{}", "Invalid status");
            return new ResponseEntity(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}
