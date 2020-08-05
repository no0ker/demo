package com.example.demo.repo;

import com.example.demo.entity.Person;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

import java.util.UUID;

public interface PersonRepository<T extends Person> extends R2dbcRepository<T, UUID>, PersonCustomRepository<T> {
}
