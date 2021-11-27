package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.data.entity.Staff;
import reactor.core.publisher.Mono;

public interface StaffService {

    Mono<Staff> findById(final Integer id);
}
