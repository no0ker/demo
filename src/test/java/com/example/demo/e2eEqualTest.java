package com.example.demo;


import com.example.demo.entity.Person;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class e2eEqualTest extends BaseSpringJunitTest {
    private static final ParameterizedTypeReference<List<Person>> PERSONS_TYPE = new ParameterizedTypeReference<List<Person>>() {
    };

    @Test
    public void whenFindByStringColumn_thenFoundItems() throws Exception {
        List<Person> persons = webTestClient.get()
                .uri("/byCriteria?filter=name:eq:name1")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PERSONS_TYPE)
                .returnResult()
                .getResponseBody();

        for (Person person : persons) {
            assertThat(person.getName(), equalTo("name1"));
        }
    }

    @Test
    public void whenFindByIntegerColumn_thenFoundItems() throws Exception {
        List<Person> persons = webTestClient.get()
                .uri("/byCriteria?filter=lastName:eq:" + StringEscapeUtils.escapeHtml4("last name 3"))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PERSONS_TYPE)
                .returnResult()
                .getResponseBody();

        for (Person person : persons) {
            assertThat(person.getLastName(), equalTo("last name 3"));
        }
    }

    @Test
    public void whenFindByStringAndIntegerColumn_thenFoundItems() throws Exception {
        List<Person> persons = webTestClient.get()
                .uri("/byCriteria?filter=name:eq:name1&filter=age:eq:2")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PERSONS_TYPE)
                .returnResult()
                .getResponseBody();

        for (Person person : persons) {
            assertThat(person.getName(), equalTo("name1"));
            assertThat(person.getAge(), equalTo(2));
        }
    }
}
