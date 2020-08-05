package com.example.demo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("person")
public class Person {
    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private String name;

    @Column("lastName")
    private String lastName;

    @Column("age")
    private Integer age;
}
