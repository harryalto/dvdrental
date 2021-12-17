package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Category;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Mono<Category> findByName(String category);
}
