package com.example.handiwomen.models;


import javax.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "Employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {

    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private Long id;

    private String name;

    private String email;

    private Integer age;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyTask> dailyTasks;

    public Employee(String name, String email, Integer age) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}
