package com.example.handiwomen.service.Task.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldResponse {
    private String name;
    private String type;
    private String value;
}