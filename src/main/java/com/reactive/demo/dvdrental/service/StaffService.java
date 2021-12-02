package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.data.entity.Staff;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StaffService {

    Mono<Staff> findById(final Long id);

    Mono<Pair<Boolean, Staff>> save(StaffCoreModel staffCoreModel);

    Flux<Staff> findAll();
}
