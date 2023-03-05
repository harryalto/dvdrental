package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import com.reactive.demo.dvdrental.data.entity.Actor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ActorService {
    Flux<Actor> findAll();

    Mono<Actor> findById(Long id);

    Mono<Pair<Boolean, Actor>> save(ActorRequest actorRequest);

    Mono<Boolean> delete(Long id);
}
