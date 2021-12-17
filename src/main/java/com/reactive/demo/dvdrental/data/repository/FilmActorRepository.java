package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.FilmActor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface FilmActorRepository extends ReactiveCrudRepository<FilmActor, Long> {
    Flux<FilmActor> findAllByFilmId(Long filmId);

}
