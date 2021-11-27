package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.api.StaffResource;
import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

@RestController
@Slf4j
public class StaffControllerImpl implements StaffController {

    private StaffService staffService;

    private GenericMapper genericMapper;

    public StaffControllerImpl(final StaffService staffService,
                               final GenericMapper genericMapper) {
        this.staffService = staffService;
        this.genericMapper = genericMapper;
    }

    //@ApiOperation("Find Staff by its id")
    @Override
    public Mono<StaffResource> findById(final Integer id) {
        return staffService.findById(id).
                map(data -> {
                    StaffResource staffResource = genericMapper.staffToStaffResource(data);
                    System.out.println(staffResource.toString());
                    return staffResource;
                });

    }

    private Consumer<Staff> getStaffConsumer() {
        return data -> {
            System.out.println("Data " + data);
        };
    }
}
