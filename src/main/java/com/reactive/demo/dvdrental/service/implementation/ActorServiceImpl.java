package com.reactive.demo.dvdrental.service.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import com.reactive.demo.dvdrental.data.entity.Actor;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.data.repository.ActorRepository;
import com.reactive.demo.dvdrental.exception.DataNotFoundException;
import com.reactive.demo.dvdrental.service.ActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    public ActorServiceImpl(final ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    @Override
    public Mono<Actor> findById(final Long id) {
        return actorRepository.findById(id).
                switchIfEmpty(Mono.error(new DataNotFoundException(id.intValue())))
                .single();
    }


    @Override
    public Mono<Pair<Boolean, Actor>> save(ActorRequest actorModel) {
        Actor actor = GenericMapper.INSTANCE.actorRequestToActor(actorModel);
        // first find and if not found then insert
        return Mono.just(actor).
                flatMap(input ->
                        actorRepository.findFirstByFirstNameAndLastName(
                                        input.getFirstName(),
                                        input.getLastName())
                                .map(existingActor -> Pair.of(true, existingActor))
                                .defaultIfEmpty(Pair.of(false, actor))
                ).
                flatMap(data ->
                        {
                            if (data.getFirst().booleanValue() == false) {
                                return actorRepository.save(actor).map(savedData -> Pair.of(false, savedData));
                            } else
                                return Mono.just(data);
                        }
                );
    }

    @Override
    public Flux<Actor> findAll() {
        return actorRepository.findAll()
                .switchIfEmpty(Mono.error(new DataNotFoundException(0)));
    }

    @Override
    public Mono<Boolean> delete(Long id) {
        return getBooleanMono(id);
    }

    private Mono<Boolean> getBooleanMono(Long id) {
        return Mono.just(id)
                .flatMap(inputId ->
                        actorRepository.findById(inputId)
                                .map(existingActor -> Pair.of(true, existingActor))
                                .defaultIfEmpty(Pair.of(false, null)))
                .flatMap(pair -> {
                    if (pair.getFirst()) {
                        return actorRepository.delete(pair.getSecond()).then(Mono.just(Boolean.TRUE));
                    } else
                        return Mono.just(Boolean.FALSE);
                });
    }

}
