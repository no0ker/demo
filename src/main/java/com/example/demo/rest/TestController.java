package com.example.demo.rest;

import com.example.demo.entity.Person;
import com.example.demo.repo.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class TestController {
    @Autowired
    private PersonRepository<Person> personRepository;

    @GetMapping(value = "/byCriteria")
    public Flux<Person> testMethod(Criteria criteria, Pageable pageable, Sort sort) {
        return personRepository.getPage(criteria, sort, pageable);
    }
}