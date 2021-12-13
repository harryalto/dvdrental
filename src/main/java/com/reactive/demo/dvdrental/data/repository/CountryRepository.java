package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Country;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CountryRepository extends ReactiveCrudRepository<Country, Long> {
    Mono<Country> findFirstByCountryAndAndCountryId(String country, Long Id);

    Mono<Country> findFirstByCountry(String country);
}
