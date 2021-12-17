package com.reactive.demo.dvdrental.service.implementation;

import com.reactive.demo.dvdrental.api.model.ActorModel;
import com.reactive.demo.dvdrental.api.model.FilmModel;
import com.reactive.demo.dvdrental.data.entity.Actor;
import com.reactive.demo.dvdrental.data.entity.Category;
import com.reactive.demo.dvdrental.data.entity.Film;
import com.reactive.demo.dvdrental.data.entity.Language;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.data.repository.*;
import com.reactive.demo.dvdrental.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final CategoryRepository categoryRepository;
    private final FilmActorRepository filmActorRepository;
    private final FilmCategoryRepository filmCategoryRepository;
    private final FilmRepository filmRepository;
    private final LanguageRepository languageRepository;
    private final ActorRepository actorRepository;

    public FilmServiceImpl(final CategoryRepository categoryRepository,
                           final FilmActorRepository filmActorRepository,
                           final FilmCategoryRepository filmCategoryRepository,
                           final FilmRepository filmRepository,
                           final LanguageRepository languageRepository, ActorRepository actorRepository) {
        this.categoryRepository = categoryRepository;
        this.filmCategoryRepository = filmCategoryRepository;
        this.filmActorRepository = filmActorRepository;
        this.filmRepository = filmRepository;
        this.languageRepository = languageRepository;
        this.actorRepository = actorRepository;
    }

    /**
     * One behaviour which i found is described here : https://stackoverflow.com/questions/70388853/r2dbc-need-help-understanding-why-there-are-multiple-calls-to-db
     */
    @Override
    public Mono<FilmModel> getById(Long id) {
        //return getFilmModelMono(id);
        return getFilmModelMonoIssue(id);
    }

    /**
     * @param id
     * @return
     */
    private Mono<FilmModel> getFilmModelMonoIssue(Long id) {
        Mono<Film> filmMono = filmRepository.findById(id).switchIfEmpty(Mono.error(DataFormatException::new));
        Flux<Actor> actorFlux = Flux.defer(() -> filmMono.flatMapMany(this::getByActorId));
        Mono<String> language = Mono.defer(() ->filmMono.flatMap(film -> languageRepository.findById(film.getLanguageId())).map(Language::getName));
        Mono<String> category = Mono.defer(() ->filmMono.flatMap(film -> filmCategoryRepository
                        .findFirstByFilmId(film.getFilmId()))
                .flatMap(filmCategory -> categoryRepository.findById(filmCategory.getCategoryId()))
                .map(Category::getName));

        return Mono.zip(filmMono, actorFlux.collectList(), language, category)
                .map(tuple -> {
                    FilmModel filmModel = GenericMapper.INSTANCE.filmToFilmModel(tuple.getT1());
                    List<ActorModel> actors = tuple
                            .getT2()
                            .stream()
                            .map(act -> GenericMapper.INSTANCE.actorToActorModel(act))
                            .collect(Collectors.toList());
                    filmModel.setActorModelList(actors);
                    filmModel.setLanguage(tuple.getT3());
                    filmModel.setCategory(tuple.getT4());
                    return filmModel;
                });
    }

    /**
     * @param id
     * @return
     */
    private Mono<FilmModel> getFilmModelMono(Long id) {
        return filmRepository.findById(id).switchIfEmpty(Mono.error(DataFormatException::new))
                .flatMap(film ->
                        Mono.zip(Mono.just(film),
                                Mono.just(film.getFilmId()).flatMapMany(this::getByActorIdNew).subscribeOn(Schedulers.boundedElastic()).collectList(),
                                Mono.just(film.getLanguageId()).flatMap(languageRepository::findById).map(Language::getName).subscribeOn(Schedulers.boundedElastic()),
                                Mono.just(film.getFilmId()).flatMap(filmCategoryRepository::findFirstByFilmId)
                                        .flatMap(filmCategory -> categoryRepository.findById(filmCategory.getCategoryId()))
                                        .map(Category::getName).subscribeOn(Schedulers.boundedElastic())
                        )
                )
                .map(tuple -> {
                    FilmModel filmModel = GenericMapper.INSTANCE.filmToFilmModel(tuple.getT1());
                    List<ActorModel> actors = tuple
                            .getT2()
                            .stream()
                            .map(act -> GenericMapper.INSTANCE.actorToActorModel(act))
                            .collect(Collectors.toList());
                    filmModel.setActorModelList(actors);
                    filmModel.setLanguage(tuple.getT3());
                    filmModel.setCategory(tuple.getT4());
                    return filmModel;
                });
    }

    /**
     * @param film
     * @return
     */
    private Flux<Actor> getByActorId(Film film) {
        return filmActorRepository
                .findAllByFilmId(film.getFilmId())
                .flatMap(filmActor -> actorRepository.findById(filmActor.getActorId()));
    }

    private Flux<Actor> getByActorIdNew(Long filmId) {
        return filmActorRepository
                .findAllByFilmId(filmId)
                .flatMap(filmActor -> actorRepository.findById(filmActor.getActorId()));
    }

    @Override
    public Mono<Boolean> delete(Long id) {
        return null;
    }
}
