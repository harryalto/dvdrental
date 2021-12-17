package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.FilmCategory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FilmCategoryRepository extends ReactiveCrudRepository<FilmCategory, Long> {
    Mono<FilmCategory> findFirstByCategoryIdAndFilmId(Long categoryId, Long filmId);
    Mono<FilmCategory> findFirstByFilmId(Long filmId);
}
