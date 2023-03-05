package com.reactive.demo.dvdrental.controller.implementation;

import com.reactive.demo.dvdrental.api.model.FilmModel;
import com.reactive.demo.dvdrental.api.request.FilmRequest;
import com.reactive.demo.dvdrental.controller.FilmController;
import com.reactive.demo.dvdrental.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class FilmControllerImpl implements FilmController {
    private FilmService filmService;

    public FilmControllerImpl(final FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    public Flux<FilmModel> findAll(String releaseYear) {
        return null;
    }

    @Override
    public Mono<FilmModel> getById(Long id) {
        return filmService.getById(id);
    }

    @Override
    public Mono<ResponseEntity<FilmModel>> create(FilmRequest filmRequest) {

        return filmService.create(filmRequest).map(pairData -> {
            if (pairData.getFirst()) {
                return new ResponseEntity<>(pairData.getSecond(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(pairData.getSecond(), HttpStatus.CREATED);
            }
        });
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteById(Long id) {
        return filmService.delete(id).flatMap(result -> {
            if (result.booleanValue() == Boolean.TRUE) {
                return Mono.just(ResponseEntity.noContent().build());
            } else
                return Mono.just(ResponseEntity.notFound().build());
        });
    }
}
