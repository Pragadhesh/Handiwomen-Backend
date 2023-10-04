package com.example.handiwomen.service.Task.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Signer {
    private String role;
    private String name;
    @JsonProperty("email_address")
    private String emailAddress;
}
