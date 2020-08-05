package com.example.demo.repo;

import com.example.demo.entity.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Objects;


@RequiredArgsConstructor
@Repository
public class PersonRepositoryImpl<T extends Person> implements PersonCustomRepository<T> {
    private final DatabaseClient databaseClient;

    @Override
    public Flux<T> getPage(Criteria criteria,
                           Sort sort,
                           Pageable pageable) {
        DatabaseClient.GenericSelectSpec selectSpec = databaseClient
                .select()
                .from("person");

        if (Objects.nonNull(criteria)) {
            selectSpec = selectSpec
                    .matching(criteria);
        }

        return selectSpec
                .orderBy(sort)
                .page(pageable)
                .as(Person.class)
                .fetch()
                .all()
                .map(person -> (T) person);
    }
}
