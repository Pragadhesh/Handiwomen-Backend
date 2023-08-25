package com.example.handiwomen.service.impl;

import com.example.handiwomen.controller.EmployeeController;
import com.example.handiwomen.models.Employee;
import com.example.handiwomen.repository.EmployeeRepository;
import com.example.handiwomen.request.Employeedata;
import com.example.handiwomen.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);


    @Autowired
    public EmployeeServiceImpl(
        EmployeeRepository employeeRepository
    )
    {
        this.employeeRepository = employeeRepository;
    }


    public ResponseEntity<Object> addEmployee(Employeedata employeedata)
    {
        LOG.debug("Calling addEmployee method from Employee service");
        try
        {
            Employee emp = employeeRepository.save(new Employee(employeedata.getName(),
                    employeedata.getEmail(),employeedata.getAge()));

            return new ResponseEntity<>(emp, HttpStatus.OK);

        }
        catch (Exception exception)
        {
            LOG.error("Exception occurred when creating Employee : {}", exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getEmployees()
    {
        LOG.debug("Calling getEmployee method from Employee service");
        try
        {
            List<Employee> employees = employeeRepository.findAll();
            return new ResponseEntity<>(employees, HttpStatus.OK);
        }
        catch (Exception exception)
        {
            LOG.error("Exception occurred when gettting all Employee : {}", exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getEmployee(Long id)
    {
        LOG.debug("Calling getEmployee method from Employee service");
        try
        {
            Optional<Employee> employee = employeeRepository.findById(id);
            return new ResponseEntity<>(employee,HttpStatus.OK);
        }
        catch (Exception exception)
        {
            LOG.error("Exception occurred when gettting Employee : {}", exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
