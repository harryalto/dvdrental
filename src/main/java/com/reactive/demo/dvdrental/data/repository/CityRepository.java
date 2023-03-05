package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.City;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CityRepository extends ReactiveCrudRepository<City, Long> {
    Mono<City> findFirstByCity(String cityName);
    Mono<City> findFirstByCityAndCountryId( String cityName, Long countryId);
}
