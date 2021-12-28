package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.FilmActor;
import com.reactive.demo.dvdrental.data.entity.FilmActorPK;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface FilmActorRepository extends ReactiveCrudRepository<FilmActor, FilmActorPK> {

    @Query("select * from film_actor  where film_id = :film_id")
    Flux<FilmActor> findAllByFilmId(@Param("film_id") final Long filmId);

    @Query("select * from film_actor  where film_id = :film_id and actor_id = :actor_id limit 1")
    Mono<FilmActor> findByFilmIdAndActorId(@Param("film_id") final Long filmId, @Param("actor_id") final Long actorId);

    @Modifying
    @Query("insert into film_actor (film_id, actor_id, last_update) values (:#{#filmActor.filmId}, :#{#filmActor.actorId}, now()) on conflict DO NOTHING")
    Mono<FilmActor> save(final FilmActor filmActor);
}
