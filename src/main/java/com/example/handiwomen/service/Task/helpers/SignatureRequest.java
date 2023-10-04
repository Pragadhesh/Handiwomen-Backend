package com.example.handiwomen.service.Task.helpers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class SignatureRequest {
    @JsonProperty("template_ids")
    private String[] templateIds;

    private String subject;
    private String message;
    private Signer[] signers;
    private CC[] ccs;

    @JsonProperty("custom_fields")
    private CustomField[] customFields;

    @JsonProperty("signing_options")
    private SigningOptions signingOptions;

    @JsonProperty("test_mode")
    private boolean testMode;

    // Getters and setters
}

