package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Address;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AddressRepository extends ReactiveCrudRepository<Address, Long> {
    Mono<Address> findFirstByAddressId(Long aLong);

    Mono<Address> findFirstByAddressAndAddress2AndPhoneAndCityIdAndDistrictAndPostalCode(
            final String address,
            final String address2,
            final String phone,
            final Long cityId,
            final String district,
            final String postalCode
    );

    Flux<Address> findByPostalCode(String postalCode);
}
