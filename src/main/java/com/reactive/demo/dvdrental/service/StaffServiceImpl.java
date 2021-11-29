package com.reactive.demo.dvdrental.service;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
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
    private final GenericMapper genericMapper;

    public StaffServiceImpl(final StaffRepository staffRepository,
                            GenericMapper genericMapper) {
        this.staffRepository = staffRepository;
        this.genericMapper = genericMapper;
    }

    @Override
    public Mono<Staff> findById(final Long id) {

        Flux<Staff> results = staffRepository.findFirstByStaffId(id);

        return results.
                switchIfEmpty(Mono.error(new DataNotFoundException(id.intValue()))).
                single().
                log("Reached here");
    }

    @Override
    public Mono<Pair<Boolean, Staff>> save(StaffCoreModel staffCoreModel) {
        Staff staff = genericMapper.convertToStaff(staffCoreModel);
        // first find and if not found then insert
        return staffRepository
                .findFirstByFirstNameAndLastNameAndEmailAndUsername(
                        staff.getFirstName(),
                        staff.getLastName(),
                        staff.getEmail(),
                        staff.getUsername())
                .flatMap(
                        foundStaff -> null != foundStaff ?
                                Mono.just(Pair.of(true, foundStaff))
                                : Mono.just(Pair.of(false, staff)))
                .switchIfEmpty(
                        staffRepository.save(staff)
                                .map(saved -> Pair.of(false, saved)));
    }

}
