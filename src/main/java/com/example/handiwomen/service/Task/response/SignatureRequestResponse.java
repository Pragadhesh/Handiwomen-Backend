package com.example.handiwomen.service.Task.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignatureRequestResponse {
    private SignatureRequestData signature_request;

}



