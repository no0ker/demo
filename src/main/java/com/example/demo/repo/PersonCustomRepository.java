package com.example.demo.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.data.repository.NoRepositoryBean;
import reactor.core.publisher.Flux;


@NoRepositoryBean
public interface PersonCustomRepository<T> {
    Flux<T> getPage(Criteria criteria, Sort sort, Pageable pageable);
}
