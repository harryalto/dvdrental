package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Actor;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ActorRepository extends ReactiveCrudRepository<Actor, Long> {

    Mono<Actor> findFirstByFirstNameAndLastName(String firstName, String lastName);
}
