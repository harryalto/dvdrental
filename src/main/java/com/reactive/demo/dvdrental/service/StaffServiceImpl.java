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

    public StaffServiceImpl(final StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Mono<Staff> findById(final Long id) {

        return staffRepository.findFirstByStaffId(id).
                switchIfEmpty(Mono.error(new DataNotFoundException(id.intValue()))).
                single();
    }

    @Override
    public Mono<Pair<Boolean, Staff>> save(StaffCoreModel staffCoreModel) {
        Staff staff = GenericMapper.INSTANCE.convertToStaff(staffCoreModel);
        // first find and if not found then insert
        return Mono.just(staff).
                flatMap(input ->
                        staffRepository
                                .findFirstByFirstNameAndLastNameAndEmailAndUsername(
                                        input.getFirstName(),
                                        input.getLastName(),
                                        input.getEmail(),
                                        input.getUsername())
                                .map(existingStaff -> Pair.of(true, existingStaff))
                                .defaultIfEmpty(Pair.of(false, staff))
                ).
                flatMap(data ->
                        {
                            if (data.getFirst().booleanValue() == false) {
                                return staffRepository.save(staff).map(savedData -> Pair.of(false, savedData));
                            } else
                                return Mono.just(data);
                        }
                );
    }

    @Override
    public Flux<Staff> findAll() {
        return staffRepository.findAll()
                .switchIfEmpty(Mono.error(new DataNotFoundException(0)));
    }
}
