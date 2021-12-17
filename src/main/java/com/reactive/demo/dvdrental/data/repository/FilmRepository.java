package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Film;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends ReactiveCrudRepository<Film, Long> {
}
