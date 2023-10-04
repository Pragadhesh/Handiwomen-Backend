package com.example.handiwomen.service.Employee.impl;

import com.example.handiwomen.models.DailyTask;
import com.example.handiwomen.models.Employee;
import com.example.handiwomen.repository.EmployeeRepository;
import com.example.handiwomen.request.Employeedata;
import com.example.handiwomen.service.Task.helpers.SignatureRequest;
import com.example.handiwomen.service.Task.response.SignatureRequestResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Value("${hellosignkey}")
    private String hellosignkey;


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

            if(employee.isPresent()) {
                Employee employeedetails = employee.get();
                List<DailyTask> dailyTasks = employeedetails.getDailyTasks();

                RestTemplate restTemplate = new RestTemplate();
                ObjectMapper objectMapper = new ObjectMapper();

                for(int index=0;index<dailyTasks.size();index++)
                {
                    if(!dailyTasks.get(index).getIsApproved())
                    {
                        String hellosignUrl = "https://api.hellosign.com/v3/signature_request/"+dailyTasks.get(index).getRequestid();

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.setBasicAuth(hellosignkey, "");

                        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

                        ResponseEntity<String> response = restTemplate.exchange(
                                hellosignUrl,
                                HttpMethod.GET,
                                requestEntity,
                                String.class
                        );
                        String responseBody = response.getBody();

                        SignatureRequestResponse signatureRequestResponse = objectMapper.readValue(responseBody, SignatureRequestResponse.class);

                        Boolean status = signatureRequestResponse.getSignature_request().getIs_complete();

                        if(status)
                        {
                            dailyTasks.get(index).setIsApproved(true);
                        }

                    }

                }

                employeedetails.setDailyTasks(dailyTasks);
                Employee emp = employeeRepository.save(employeedetails);

                return new ResponseEntity<>(emp, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(employee, HttpStatus.OK);
            }
        }
        catch (Exception exception)
        {
            LOG.error("Exception occurred when gettting Employee : {}", exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
