package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Staff;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StaffRepository extends ReactiveCrudRepository<Staff, Integer> {

    Flux<Staff> findByStaffId(Integer staffId);
    Flux<Staff> findAllByStaffId(Integer staffId);

    //Mono<Integer> deleteAllByStaffId(Long staffId);
}
