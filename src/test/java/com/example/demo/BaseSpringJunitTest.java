package com.example.demo;

import com.example.demo.entity.Person;
import com.example.demo.repo.PersonRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseSpringJunitTest {
    @Autowired
    protected PersonRepository<Person> personRepository;

    @Autowired
    protected WebTestClient webTestClient;
}
