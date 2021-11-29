package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Staff;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface StaffRepository extends ReactiveCrudRepository<Staff, Long> {

    Flux<Staff> findFirstByStaffId(Long staffId);

    Mono<Staff> save(Staff staff);

    Mono<Staff> findFirstByFirstNameAndLastNameAndEmailAndUsername(String firstName,
                                                                   String lastName,
                                                                   String email,
                                                                   String username);
}
