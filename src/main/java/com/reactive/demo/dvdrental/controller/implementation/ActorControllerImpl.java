package com.reactive.demo.dvdrental.controller.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.ActorModel;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import com.reactive.demo.dvdrental.controller.ActorController;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.service.ActorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class ActorControllerImpl implements ActorController {

    private ActorService actorService;

    public ActorControllerImpl(final ActorService actorService) {

        this.actorService = actorService;
    }

    @Override
    public Flux<ActorModel> findAll() {
        return actorService.findAll().map(data -> {
            ActorModel actorModel = GenericMapper.INSTANCE.actorToActorModel(data);
            System.out.println(actorModel.toString());
            return actorModel;
        });
    }

    //@ApiOperation("Find Staff by its id")
    @Override
    public Mono<ActorModel> findById(final Long id) {
        return actorService.findById(id).
                map(data -> {
                    ActorModel actorModel = GenericMapper.INSTANCE.actorToActorModel(data);
                    System.out.println(actorModel.toString());
                    return actorModel;
                });

    }

    @Override
    public Mono<ResponseEntity<ActorModel>> create(ActorRequest actorRequest) {
        return actorService.save(actorRequest)
                .map(data -> {
                    ActorModel ActorModel = GenericMapper.INSTANCE.actorToActorModel(data.getSecond());
                    System.out.println(ActorModel.toString());
                    return Pair.of(data.getFirst(), ActorModel);
                })
                .map(pairData -> {
                    if (pairData.getFirst()) {
                        return new ResponseEntity<>(pairData.getSecond(), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(pairData.getSecond(), HttpStatus.CREATED);
                    }
                });
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteById(Long id) {
        return actorService.delete(id).flatMap(result -> {
            if (result.booleanValue() == Boolean.TRUE) {
                return Mono.just(ResponseEntity.noContent().build());
            } else
                return Mono.just(ResponseEntity.notFound().build());
        });
    }
}
