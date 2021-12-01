package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.api.model.StaffModel;
import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.service.StaffService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class StaffControllerImplTest {

    @InjectMocks
    private StaffControllerImpl staffController;

    @Mock
    private StaffService staffService;

    private Staff staffSample = Staff.builder().staffId(1L).firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();
    private StaffCoreModel staffCoreModel = StaffCoreModel.builder().firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();
    private StaffModel staffResponse = StaffModel.builder().staffId(1L).firstName("John").lastName("Doe").username("something").
            email("aa@gmail.com").build();


    @BeforeEach
    public void setUp() {

        BDDMockito.when(staffService.findById(1L))
                .thenReturn(Mono.just(staffSample));

        BDDMockito.when(staffService.save(staffCoreModel))
                .thenReturn(Mono.just(Pair.of(false, staffSample)));

    }


    @Test
    @DisplayName("findById returns a Mono with staff when it exists")
    public void findById_ReturnMonoStaff_Success() {
        StepVerifier.create(staffController.findById(1L))
                .expectSubscription()
                .expectNext(staffResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns empty with staff when it doesnt exists")
    public void findById_ReturnMonoEmpty_Failure() {
        BDDMockito.when(staffService.findById(2L))
                .thenReturn(Mono.empty());
        StepVerifier.create(staffController.findById(2L))
                .expectSubscription()
                .expectNextCount(0).
                verifyComplete();
    }

    @Test
    @DisplayName("Create Staff new")
    public void createNew_Success() {
        BDDMockito.when(staffService.save(staffCoreModel))
                .thenReturn(Mono.just(Pair.of(true, staffSample)));
        StepVerifier.create(staffController.create(staffCoreModel))
                .expectSubscription()
                .expectNextCount(1).
                verifyComplete();
    }

    @Test
    @DisplayName("Return existing staff")
    public void createReturnExisting_Success() {
        BDDMockito.when(staffService.save(staffCoreModel))
                .thenReturn(Mono.just(Pair.of(false, staffSample)));
        StepVerifier.create(staffController.create(staffCoreModel))
                .expectSubscription()
                .expectNextCount(1).
                verifyComplete();
    }


}