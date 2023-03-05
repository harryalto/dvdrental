package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Film;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FilmRepository extends ReactiveCrudRepository<Film, Long> {

    Mono<Film> findFirstByTitleAndReleaseYearAndLanguageId(final String title,
                                                           final Integer releaseYear,
                                                           final Long languageId);
}
