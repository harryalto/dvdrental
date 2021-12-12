package com.reactive.demo.dvdrental.integration;

import com.reactive.demo.dvdrental.api.model.ActorModel;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import com.reactive.demo.dvdrental.controller.ActorController;
import com.reactive.demo.dvdrental.data.entity.Actor;
import com.reactive.demo.dvdrental.data.repository.ActorRepository;
import com.reactive.demo.dvdrental.service.implementation.ActorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = ActorController.class)
@Import(ActorServiceImpl.class)
public class ActorControllerIT {

    @MockBean
    private ActorRepository actorRepositoryMock;

    @Autowired
    private WebTestClient testClient;

    private final Actor actorSample = Actor.builder().actorId(1L).firstName("John").lastName("Doe")
            .build();
    private final ActorRequest actorCoreModel = ActorRequest.builder().firstName("John").lastName("Doe")
            .build();
    private final ActorModel actorResponse = ActorModel.builder().actorId(1L).firstName("John").lastName("Doe")
            .build();


    @BeforeEach
    public void setUp() {
        BDDMockito.when(actorRepositoryMock.findById(1L))
                .thenReturn(Mono.just(actorSample));
        BDDMockito.when(actorRepositoryMock.findAll())
                .thenReturn(Flux.just(actorSample));
        BDDMockito.when(actorRepositoryMock.save(
                        ArgumentMatchers.any(Actor.class)))
                .thenReturn(Mono.just(actorSample));
        BDDMockito.when(actorRepositoryMock.findFirstByFirstNameAndLastName(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("Get All Actor ")
    public void getAllActor_ReturnFluxOfActor_Successful() {
        testClient
                .get()
                .uri("/v1/actor")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.[0].actorId").isEqualTo(actorSample.getActorId())
                .jsonPath("$.[0].firstName").isEqualTo(actorSample.getFirstName());
    }

    @Test
    @DisplayName("Get Actor resource by Actor Id")
    public void getActorById_ReturnMonoOfActor_WhenSuccessful() {
        testClient
                .get()
                .uri("/v1/actor/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.actorId").isEqualTo(actorSample.getActorId())
                .jsonPath("$.firstName").isEqualTo(actorSample.getFirstName());
    }

    @Test
    @DisplayName("Create Actor Resource")
    public void createActor_ReturnMono_WhenSuccessful() {
        testClient.post().uri("/v1/actor")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(actorCoreModel), ActorRequest.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.firstName").isEqualTo("John");
    }

    @Test
    @DisplayName("Delete Actor Resource by Id")
    public void deleteActor_ReturnMono_WhenSuccessful() {
        BDDMockito.when(actorRepositoryMock.findById(1L))
                .thenReturn(Mono.just(actorSample));
        BDDMockito.when(actorRepositoryMock.delete(
                        ArgumentMatchers.any(Actor.class)))
                .thenReturn(Mono.empty());
        testClient
                .delete()
                .uri("/v1/actor/1")
                .exchange()
                .expectStatus().isNoContent();
    }
}
