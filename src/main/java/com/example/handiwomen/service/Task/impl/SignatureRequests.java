package com.example.handiwomen.service.Task.impl;

import com.example.handiwomen.request.Announcementdata;
import com.example.handiwomen.request.Taskdata;
import org.springframework.http.ResponseEntity;

public interface SignatureRequests {

    ResponseEntity<Object> createSignatureRequest(Taskdata taskdata);

    ResponseEntity<Object> createAnnouncement(Announcementdata announcementdata);
}
