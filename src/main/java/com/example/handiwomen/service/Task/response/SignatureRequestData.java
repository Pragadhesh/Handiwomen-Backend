package com.example.handiwomen.service.Task.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatureRequestData {
    private String signature_request_id;
    private boolean test_mode;
    private String title;
    private String original_title;
    private String subject;
    private String message;

    private List<CustomFieldResponse> custom_fields;
    private String signing_url;
    private Boolean is_complete;

}