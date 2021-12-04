package com.reactive.demo.dvdrental.service.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import com.reactive.demo.dvdrental.data.entity.Actor;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.data.repository.ActorRepository;
import com.reactive.demo.dvdrental.exception.DataNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(SpringExtension.class)
class ActorServiceImplTest {

    @InjectMocks
    private ActorServiceImpl actorService;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private GenericMapper genericMapper;

    private Actor actorSample = Actor.builder()
            .actorId(1)
            .firstName("John")
            .lastName("Doe").build();
    private ActorRequest actorRequest = ActorRequest.builder()
            .firstName("John")
            .lastName("Doe")
            .build();

    @Test
    @DisplayName("Get All Actor members")
    public void testFindAll_Successful() {
        BDDMockito.when(actorRepository.findAll())
                .thenReturn(Flux.just(actorSample));
        StepVerifier.create(actorService.findAll())
                .expectSubscription()
                .expectNext(actorSample)
                .verifyComplete();

    }

    @Test
    @DisplayName("Get All Actor members returns empty")
    public void testFindAll_NoDataFound() {
        BDDMockito.when(actorRepository.findAll())
                .thenReturn(Flux.empty());
        StepVerifier.create(actorService.findAll())
                .expectSubscription()
                .expectError(DataNotFoundException.class)
                .verify();

    }

    @Test
    @DisplayName("Get by Id when there is data")
    public void testGetById_Successful() {
        BDDMockito.when(actorRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(actorSample));
        StepVerifier.create(actorService.findById(1L))
                .expectSubscription()
                .expectNext(actorSample)
                .verifyComplete();

    }

    @Test
    @DisplayName("Get by Id when there is no data")
    public void testGetById_WhenEmpty() {
        BDDMockito.when(actorRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(actorService.findById(1L))
                .expectSubscription()
                .expectError(DataNotFoundException.class)
                .verify();

    }

    @Test
    @DisplayName("Create new actor resource")
    public void testCreateActor_Successful() {
        BDDMockito.when(actorRepository.findFirstByFirstNameAndLastName(ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()))
                .thenReturn(Mono.empty());
        BDDMockito.when(genericMapper.actorRequestToActor(ArgumentMatchers.any(ActorRequest.class)))
                .thenReturn(actorSample);
        BDDMockito.when(actorRepository.save(ArgumentMatchers.any(Actor.class)))
                .thenReturn(Mono.just(actorSample));

        StepVerifier.create(actorService.save(actorRequest))
                .expectSubscription()
                .expectNext(Pair.of(false, actorSample))
                .verifyComplete();

    }

    @Test
    @DisplayName("Create returns existing actor resource")
    public void testCreateActor_SuccessfulAlreadyExists() {
        BDDMockito.when(actorRepository.findFirstByFirstNameAndLastName(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(actorSample));
        BDDMockito.when(genericMapper.actorRequestToActor(ArgumentMatchers.any(ActorRequest.class)))
                .thenReturn(actorSample);
        BDDMockito.verify(actorRepository, BDDMockito.times(0)).save(ArgumentMatchers.any(Actor.class));

        StepVerifier.create(actorService.save(actorRequest))
                .expectSubscription()
                .expectNext(Pair.of(true, actorSample))
                .verifyComplete();
    }

    @Test
    @DisplayName("Delete by Id when there is data")
    public void testDeleteById_Successful() {
        BDDMockito.when(actorRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.just(actorSample));
        BDDMockito.when(actorRepository.delete(ArgumentMatchers.any(Actor.class)))
                .thenReturn(Mono.empty());
        StepVerifier.create(actorService.delete(1L))
                .expectSubscription()
                .expectNext(Boolean.TRUE)
                .verifyComplete();

    }

    @Test
    @DisplayName("Delete by Id when there is no data")
    public void testDeleteById_WhenEmpty() {
        BDDMockito.when(actorRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Mono.empty());
        StepVerifier.create(actorService.delete(1L))
                .expectSubscription()
                .expectNext(Boolean.FALSE)
                .verifyComplete();
        BDDMockito.verify(actorRepository, BDDMockito.times(0)).delete(ArgumentMatchers.any(Actor.class));
    }
}