package com.example.handiwomen.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Taskdata {


    @NotNull(message = "Mandatory attribute id is missing")
    private Long id;

    @NotNull(message = "Mandatory attribute type is missing")
    private String type;

    @NotNull(message = "Mandatory attribute count is missing")
    private Integer count;


    @NotNull(message = "Mandatory attribute date is missing")
    private String date;
}
