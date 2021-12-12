package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.api.model.AddressModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AddressService {
    Flux<AddressModel> findAll();

    Mono<AddressModel> getById(Long id);
}
