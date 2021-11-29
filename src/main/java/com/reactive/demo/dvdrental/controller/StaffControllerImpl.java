package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.api.model.StaffModel;
import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.service.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Mono<StaffModel> findById(final Long id) {
        return staffService.findById(id).
                map(data -> {
                    StaffModel staffResource = genericMapper.convert(data);
                    System.out.println(staffResource.toString());
                    return staffResource;
                });

    }

    @Override
    public Mono<ResponseEntity<StaffModel>> create(StaffCoreModel staffCoreModelRequest) {
        return staffService.save(staffCoreModelRequest)
                .map(data -> {
                    StaffModel staffModel = genericMapper.convert(data.getSecond());
                    System.out.println(staffModel.toString());
                    return Pair.of(data.getFirst(), staffModel);
                })
                .map(pairData -> {
                    if (pairData.getFirst()) {
                        return new ResponseEntity<>(pairData.getSecond(), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(pairData.getSecond(), HttpStatus.CREATED);
                    }
                });
    }

    private Consumer<Staff> getStaffConsumer() {
        return data -> {
            System.out.println("Data " + data);
        };
    }
}
