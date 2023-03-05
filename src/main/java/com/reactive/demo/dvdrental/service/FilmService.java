package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.FilmModel;
import com.reactive.demo.dvdrental.api.request.FilmRequest;
import reactor.core.publisher.Mono;

public interface FilmService {
    Mono<FilmModel> getById(Long id);

    Mono<Boolean> delete(Long id);

    Mono<Pair<Boolean, FilmModel>> create(FilmRequest filmRequest);
}
