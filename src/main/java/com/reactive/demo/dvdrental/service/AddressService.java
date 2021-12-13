package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.AddressModel;
import com.reactive.demo.dvdrental.api.request.AddressRequest;
import com.reactive.demo.dvdrental.data.entity.Address;
import com.reactive.demo.dvdrental.data.entity.Staff;
import org.springframework.data.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AddressService {
    Flux<AddressModel> findAll(String postalCode);

    Mono<AddressModel> getById(Long id);

    Mono<Boolean> deleteById(Long id);

    Mono<Pair<Boolean, Address>> save(AddressRequest addressRequest);
}
