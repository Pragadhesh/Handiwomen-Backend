package com.example.handiwomen.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Announcementdata {

    @NotNull(message = "Mandatory attribute message is missing")
    String message;
}
