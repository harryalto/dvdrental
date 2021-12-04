package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.StaffCoreModel;
import com.reactive.demo.dvdrental.api.model.StaffModel;
import com.reactive.demo.dvdrental.controller.implementation.StaffControllerImpl;
import com.reactive.demo.dvdrental.data.entity.Staff;
import com.reactive.demo.dvdrental.service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
    @DisplayName("findAll returns Flux ")
    public void findAll_ReturnFluxStaff_Success() {
        BDDMockito.when(staffService.findAll())
                .thenReturn(Flux.just(staffSample));
        StepVerifier.create(staffController.findAll())
                .expectSubscription()
                .expectNext(new StaffModel[]{staffResponse})
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns a Mono with staff when it exists")
    public void findById_ReturnMonoStaff_Success() {
        BDDMockito.when(staffService.findById(2L))
                .thenReturn(Mono.just(staffSample));
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

    @Test
    @DisplayName("deleteById returns a Mono void when it exists")
    public void deleteById_ReturnHTTP204OK_Success() {
        BDDMockito.when(staffService.delete(2L))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(staffController.deleteById(2L))
                .expectSubscription()
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();
    }

    @Test
    @DisplayName("deleteById returns a Mono void when it doesnt exit")
    public void deleteById_ReturnMonoVoid_Success() {
        BDDMockito.when(staffService.delete(2L))
                .thenReturn(Mono.just(Boolean.FALSE));
        StepVerifier.create(staffController.deleteById(2L))
                .expectSubscription()
                .expectNext(ResponseEntity.notFound().build())
                .verifyComplete();
    }

}