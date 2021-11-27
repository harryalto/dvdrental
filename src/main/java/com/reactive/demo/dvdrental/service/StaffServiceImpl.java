package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.data.repository.StaffRepository;
import com.reactive.demo.dvdrental.exception.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class StaffServiceImpl implements StaffService {

    private final StaffRepository staffRepository;

    public StaffServiceImpl(final StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Mono<Staff> findById(final Integer id) {

        Flux<Staff> results = staffRepository.findAllByStaffId(id);

        return results.single().log("Reached here")
                .switchIfEmpty(Mono.error(new DataNotFoundException(id)));
    }
}
