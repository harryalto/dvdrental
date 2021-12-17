package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.api.model.FilmModel;
import reactor.core.publisher.Mono;

public interface FilmService {
    Mono<FilmModel> getById(Long id);

    Mono<Boolean> delete(Long id);
}
