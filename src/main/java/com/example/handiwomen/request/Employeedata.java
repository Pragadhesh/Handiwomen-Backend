package com.example.handiwomen.request;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Employeedata {

    @NotNull(message = "Mandatory attribute name is missing")
    private String name;

    @NotNull(message = "Mandatory attribute age is missing")
    private Integer age;


    @NotNull(message = "Mandatory attribute email is missing")
    private String email;

}
