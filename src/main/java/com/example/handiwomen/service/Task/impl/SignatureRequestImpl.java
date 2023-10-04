package com.example.handiwomen.service.Task.impl;

import com.example.handiwomen.models.DailyTask;
import com.example.handiwomen.models.Employee;
import com.example.handiwomen.repository.EmployeeRepository;
import com.example.handiwomen.repository.TaskRepository;
import com.example.handiwomen.request.Announcementdata;
import com.example.handiwomen.request.Taskdata;
import com.example.handiwomen.service.Employee.impl.EmployeeServiceImpl;
import com.example.handiwomen.service.Task.helpers.CustomField;
import com.example.handiwomen.service.Task.helpers.SignatureRequest;
import com.example.handiwomen.service.Task.helpers.Signer;
import com.example.handiwomen.service.Task.helpers.SigningOptions;
import com.example.handiwomen.service.Task.response.SignatureRequestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class SignatureRequestImpl implements SignatureRequests {


    EmployeeRepository employeeRepository;

    TaskRepository taskRepository;

    @Autowired
    SignatureRequestImpl(EmployeeRepository employeeRepository, TaskRepository taskRepository)
    {
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
    }


    @Value("${hellosignkey}")
    private String hellosignkey;


    @Value("${openaikey}")
    private String openaikey;

    private static final Logger LOG = LoggerFactory.getLogger(SignatureRequestImpl.class);


    public ResponseEntity<Object> createSignatureRequest(Taskdata taskdata) {
        try {

            Optional<Employee> employee = employeeRepository.findById(taskdata.getId());
            if(employee.isPresent()) {

                Employee employeedetails = employee.get();

                RestTemplate restTemplate = new RestTemplate();
                String hellosignUrl = "https://api.hellosign.com/v3/signature_request/send_with_template";

                SignatureRequest signatureRequest = new SignatureRequest();

                signatureRequest.setTemplateIds(new String[]{"61a687efba8250a630553170f64552399d0905de"});

                signatureRequest.setSubject("Daily Task Report");
                signatureRequest.setMessage("Action Required: Sign Task Report");

                Signer signer1 = new Signer();
                signer1.setRole("Employee");
                signer1.setName(employeedetails.getName());
                signer1.setEmailAddress(employeedetails.getEmail());


                Signer signer2 = new Signer();
                signer2.setRole("Manager");
                signer2.setName("Manager");
                signer2.setEmailAddress("pragadhesh14@gmail.com");

                signatureRequest.setSigners(new Signer[]{signer1, signer2});


                CustomField employeename = new CustomField();
                employeename.setName("EmployeeName");
                employeename.setValue(employeedetails.getName());

                CustomField date = new CustomField();
                date.setName("Date");
                date.setValue(taskdata.getDate());

                CustomField type = new CustomField();
                type.setName("Type");
                type.setValue(taskdata.getType());

                CustomField count = new CustomField();
                count.setName("Count");
                count.setValue(String.valueOf(taskdata.getCount()));

                signatureRequest.setCustomFields(new CustomField[]{employeename, date, type, count});

                SigningOptions signingOptions = new SigningOptions();
                signingOptions.setDraw(true);
                signingOptions.setType(true);
                signingOptions.setUpload(true);
                signingOptions.setPhone(false);
                signingOptions.setDefaultType("draw");

                signatureRequest.setSigningOptions(signingOptions);


                signatureRequest.setTestMode(true);

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonRequestBody = objectMapper.writeValueAsString(signatureRequest);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.setBasicAuth(hellosignkey, "");

                HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(hellosignUrl, requestEntity, String.class);
                String responseBody = response.getBody();

                SignatureRequestResponse signatureRequestResponse = objectMapper.readValue(responseBody, SignatureRequestResponse.class);

                String requestId = signatureRequestResponse.getSignature_request().getSignature_request_id();

                DailyTask dailyTask = new DailyTask();
                dailyTask.setDate(taskdata.getDate());
                dailyTask.setRequestid(requestId);
                dailyTask.setCount(taskdata.getCount());
                dailyTask.setType(taskdata.getType());
                dailyTask.setIsApproved(false);
                dailyTask.setEmployee(employeedetails);

                List<DailyTask> tasks = employeedetails.getDailyTasks();
                tasks.add(dailyTask);

                employeeRepository.save(employeedetails);

                return new ResponseEntity<>(dailyTask, HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>("Employee not found",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        catch (Exception exception)
        {
            LOG.error("Exception occured while sending signature request : {}", exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<Object> createAnnouncement(Announcementdata announcementdata) {
        List<Map<String, String>> messagesList;
        try {
            RestTemplate restTemplate = new RestTemplate();
            // Set the request headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + openaikey);

            String url = "https://api.openai.com/v1/chat/completions";

            messagesList = new ArrayList<>();
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("role", "user");
            messageMap.put("content", "Send me the subject (1 line) and the body (a paragraph of maximum 8 lines) for the following announcement"
                    + " via email to all employees:\n\n" + announcementdata.getMessage()
                    + "\n\nPlease ensure that the generated body does not include any automatic greetings like Dear all or closing remarks like thanking you."
                    + "\n\nThe JSON format should be as follows:\n{\"subject\": \"subject of the email\", \"body\": \"body of the email\"}");

            messagesList.add(messageMap);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", messagesList);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Assuming you have received the response in the 'response' variable
            String responseBody = response.getBody();

            System.out.println(responseBody);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Get the content from the JSON object
            String responseContent = rootNode.get("choices").get(0).get("message").get("content").asText();

            // Parse the JSON content within the response
            JsonNode contentNode = objectMapper.readTree(responseContent);
            String subject = contentNode.get("subject").asText();
            String body = contentNode.get("body").asText();


            String hellosignUrl = "https://api.hellosign.com/v3/signature_request/send_with_template";

            SignatureRequest signatureRequest = new SignatureRequest();

            signatureRequest.setTemplateIds(new String[]{"f3b1755adb894cac98694023bf6ee7d9b292445a"});

            signatureRequest.setSubject("Announcement");
            signatureRequest.setMessage("Special Announcement");



            Signer signer1 = new Signer();
            signer1.setRole("Manager");
            signer1.setName("Manager");
            signer1.setEmailAddress("pragadhesh14@gmail.com");

            signatureRequest.setSigners(new Signer[]{signer1});


            CustomField mfsubject = new CustomField();
            mfsubject.setName("Subject");
            mfsubject.setValue(subject);

            CustomField mfbody = new CustomField();
            mfbody.setName("Message");
            mfbody.setValue(body);


            signatureRequest.setCustomFields(new CustomField[]{mfsubject,mfbody});

            SigningOptions signingOptions = new SigningOptions();
            signingOptions.setDraw(true);
            signingOptions.setType(true);
            signingOptions.setUpload(true);
            signingOptions.setPhone(false);
            signingOptions.setDefaultType("draw");

            signatureRequest.setSigningOptions(signingOptions);


            signatureRequest.setTestMode(true);


            ObjectMapper objectMapper1 = new ObjectMapper();
            String jsonRequestBody = objectMapper1.writeValueAsString(signatureRequest);

            HttpHeaders headers1 = new HttpHeaders();
            headers1.setContentType(MediaType.APPLICATION_JSON);
            headers1.setBasicAuth(hellosignkey, "");

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonRequestBody, headers1);

            ResponseEntity<String> response1 = restTemplate.postForEntity(hellosignUrl, requestEntity, String.class);
            String responseBody1 = response1.getBody();

            return new ResponseEntity<>(responseBody1,HttpStatus.OK);

        }
        catch (Exception exception)
        {
            System.out.println(exception);
            LOG.error("Exception occured while creating announcement : {}", exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
