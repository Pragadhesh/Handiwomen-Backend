package com.example.handiwomen.controller;

import com.example.handiwomen.request.Announcementdata;
import com.example.handiwomen.request.Taskdata;
import com.example.handiwomen.service.Task.impl.SignatureRequests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@CrossOrigin(origins = "*")
public class TaskController {

    private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);

    SignatureRequests signatureRequests;

    @Autowired
    TaskController(SignatureRequests signatureRequests)
    {
        this.signatureRequests = signatureRequests;
    }

    @PostMapping("/sign")
    public ResponseEntity<Object> createSignatureRequest(@Valid @RequestBody Taskdata taskdata)
    {
        LOG.debug("Calling create SignatureRequest...");
        try {
            return signatureRequests.createSignatureRequest(taskdata);
        }
        catch (Exception exception)
        {
            LOG.error("{}", "Invalid status");
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/announcement")
    public ResponseEntity<Object> createAnnouncement(@Valid @RequestBody Announcementdata announcementdata)
    {
        LOG.debug("Calling create Announcement...");
        try {
            return signatureRequests.createAnnouncement(announcementdata);
        }
        catch (Exception exception)
        {
            LOG.error("{}", "Invalid status");
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
