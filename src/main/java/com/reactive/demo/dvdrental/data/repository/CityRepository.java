package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.City;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends ReactiveCrudRepository<City, Long> {
}
