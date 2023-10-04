package com.example.handiwomen.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "DailyTask")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DailyTask {

    @Id
    private String requestid;

    private String date;

    private String type;

    private Integer count;

    private Boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties("dailyTasks")
    private Employee employee;

}
