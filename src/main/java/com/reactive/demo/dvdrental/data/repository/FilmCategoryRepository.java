package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.FilmCategory;
import com.reactive.demo.dvdrental.data.entity.FilmCategoryPK;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface FilmCategoryRepository extends ReactiveCrudRepository<FilmCategory, FilmCategoryPK> {

    @Query("select * from film_category  where category_id = :category_id and film_id = :film_id limit 1")
    Mono<FilmCategory> findFirstByCategoryIdAndFilmId(@Param("category_id") Long categoryId, @Param("film_id") Long filmId);

    @Query("select * from film_category   where   film_id = :film_id limit 1")
    Mono<FilmCategory> findFirstByFilmId(@Param("film_id") Long filmId);

    @Modifying
    @Query(
            "insert into film_category (film_id, category_id, last_update) values (:#{#filmCategory.filmId}, :#{#filmCategory.categoryId}, now()) on conflict DO NOTHING"
    )
    @Override
    Mono<FilmCategory> save(final FilmCategory filmCategory);
}
