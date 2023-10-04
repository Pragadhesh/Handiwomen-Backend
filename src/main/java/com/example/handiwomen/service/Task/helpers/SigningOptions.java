package com.example.handiwomen.service.Task.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SigningOptions {
    private boolean draw;
    private boolean type;
    private boolean upload;
    private boolean phone;
    @JsonProperty("default_type")
    private String defaultType;

    // Getters and setters
}
