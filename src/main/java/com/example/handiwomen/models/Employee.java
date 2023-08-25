package com.example.handiwomen.models;


import javax.persistence.*;
import lombok.*;


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

    @Column(unique = true)
    private String email;

    private Integer age;

    public Employee(String name, String email, Integer age) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
}
