package com.reactive.demo.dvdrental.controller;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.ActorModel;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import com.reactive.demo.dvdrental.controller.implementation.ActorControllerImpl;
import com.reactive.demo.dvdrental.data.entity.Actor;
import com.reactive.demo.dvdrental.service.ActorService;
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
class ActorControllerImplTest {

    @InjectMocks
    private ActorControllerImpl actorController;

    @Mock
    private ActorService actorService;

    private final Actor actorSample = Actor.builder().actorId(1L).firstName("John").lastName("Doe").build();
    private final ActorRequest actorCoreModel = ActorRequest.builder().firstName("John").lastName("Doe").build();
    private final ActorModel actorResponse = ActorModel.builder().actorId(1L).firstName("John").lastName("Doe").build();

    @BeforeEach
    public void setUp() {
        BDDMockito.when(actorService.findById(1L))
                .thenReturn(Mono.just(actorSample));
        BDDMockito.when(actorService.save(actorCoreModel))
                .thenReturn(Mono.just(Pair.of(false, actorSample)));
    }

    @Test
    @DisplayName("findAll returns Flux ")
    public void findAll_ReturnFluxActor_Success() {
        BDDMockito.when(actorService.findAll())
                .thenReturn(Flux.just(actorSample));
        StepVerifier.create(actorController.findAll())
                .expectSubscription()
                .expectNext(new ActorModel[]{actorResponse})
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns a Mono with actor when it exists")
    public void findById_ReturnMonoActor_Success() {
        BDDMockito.when(actorService.findById(2L))
                .thenReturn(Mono.just(actorSample));
        StepVerifier.create(actorController.findById(1L))
                .expectSubscription()
                .expectNext(actorResponse)
                .verifyComplete();
    }

    @Test
    @DisplayName("findById returns empty with actor when it doesnt exists")
    public void findById_ReturnMonoEmpty_Failure() {
        BDDMockito.when(actorService.findById(2L))
                .thenReturn(Mono.empty());
        StepVerifier.create(actorController.findById(2L))
                .expectSubscription()
                .expectNextCount(0).
                verifyComplete();
    }

    @Test
    @DisplayName("Create Actor new")
    public void createNew_Success() {
        BDDMockito.when(actorService.save(actorCoreModel))
                .thenReturn(Mono.just(Pair.of(true, actorSample)));
        StepVerifier.create(actorController.create(actorCoreModel))
                .expectSubscription()
                .expectNextCount(1).
                verifyComplete();
    }

    @Test
    @DisplayName("Return existing actor")
    public void createReturnExisting_Success() {
        BDDMockito.when(actorService.save(actorCoreModel))
                .thenReturn(Mono.just(Pair.of(false, actorSample)));
        StepVerifier.create(actorController.create(actorCoreModel))
                .expectSubscription()
                .expectNextCount(1).
                verifyComplete();
    }

    @Test
    @DisplayName("deleteById returns a Mono void when it exists")
    public void deleteById_ReturnHTTP204OK_Success() {
        BDDMockito.when(actorService.delete(2L))
                .thenReturn(Mono.just(Boolean.TRUE));
        StepVerifier.create(actorController.deleteById(2L))
                .expectSubscription()
                .expectNext(ResponseEntity.noContent().build())
                .verifyComplete();
    }

    @Test
    @DisplayName("deleteById returns a Mono void when it doesnt exit")
    public void deleteById_ReturnMonoVoid_Success() {
        BDDMockito.when(actorService.delete(2L))
                .thenReturn(Mono.just(Boolean.FALSE));
        StepVerifier.create(actorController.deleteById(2L))
                .expectSubscription()
                .expectNext(ResponseEntity.notFound().build())
                .verifyComplete();
    }

}