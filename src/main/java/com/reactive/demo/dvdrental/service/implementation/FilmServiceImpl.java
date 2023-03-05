package com.reactive.demo.dvdrental.service.implementation;

import com.reactive.demo.dvdrental.Pair;
import com.reactive.demo.dvdrental.api.model.ActorModel;
import com.reactive.demo.dvdrental.api.model.FilmModel;
import com.reactive.demo.dvdrental.api.request.ActorRequest;
import com.reactive.demo.dvdrental.api.request.FilmRequest;
import com.reactive.demo.dvdrental.data.entity.*;
import com.reactive.demo.dvdrental.data.mapper.GenericMapper;
import com.reactive.demo.dvdrental.data.repository.*;
import com.reactive.demo.dvdrental.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
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
    private final TransactionalOperator operator;


    public FilmServiceImpl(final CategoryRepository categoryRepository,
                           final FilmActorRepository filmActorRepository,
                           final FilmCategoryRepository filmCategoryRepository,
                           final FilmRepository filmRepository,
                           final LanguageRepository languageRepository,
                           final ActorRepository actorRepository,
                           final TransactionalOperator operator) {
        this.categoryRepository = categoryRepository;
        this.filmCategoryRepository = filmCategoryRepository;
        this.filmActorRepository = filmActorRepository;
        this.filmRepository = filmRepository;
        this.languageRepository = languageRepository;
        this.actorRepository = actorRepository;
        this.operator = operator;
    }

    /**
     * One behaviour which I found is described here : https://stackoverflow.com/questions/70388853/r2dbc-need-help-understanding-why-there-are-multiple-calls-to-db
     */
    @Override
    public Mono<FilmModel> getById(Long id) {
        if (id == 1L) {
            log.info("Calling getFilmModelMonoIssue");
            return getFilmModelMonoIssue(id);
        } else if (2L == id) {
            log.info("Calling getFilmModelMonoUsingCache");
            return getFilmModelMonoUsingCache(id);
        } else {
            log.info("Calling getFilmModelMono");
            return getFilmModelMono(id);
        }

    }

    /**
     * @param id FilmId
     * @return Watch how threads are scheduled in each scenario
     * Experiment 1: Try with Mono.cache()
     * Experiment 2: Try with Mono.cache() + subscribeOn/publishOn
     * Experiment 3: Remove defer() operator and try
     */
    private Mono<FilmModel> getFilmModelMonoIssue(Long id) {
        Mono<Film> filmMono = filmRepository.findById(id).switchIfEmpty(Mono.error(DataFormatException::new));
        Flux<Actor> actorFlux = filmMono.flatMapMany(this::getByActorId).publishOn(Schedulers.boundedElastic());
        Mono<String> language = filmMono.flatMap(film -> languageRepository.findById(film.getLanguageId())).map(Language::getName);
        Mono<String> category = filmMono.flatMap(film -> filmCategoryRepository
                .findFirstByFilmId(film.getFilmId())
                .flatMap(filmCategory -> categoryRepository.findById(filmCategory.getCategoryId()))
                .map(Category::getName));

        return Mono.zip(filmMono, actorFlux.collectList(), language, category)
                .map(tuple -> {
                    FilmModel filmModel = GenericMapper.INSTANCE.filmToFilmModel(tuple.getT1());
                    List<ActorModel> actors = tuple
                            .getT2()
                            .stream()
                            .map(GenericMapper.INSTANCE::actorToActorModel)
                            .collect(Collectors.toList());
                    filmModel.setActors(actors);
                    filmModel.setLanguage(tuple.getT3());
                    filmModel.setCategory(tuple.getT4());
                    return filmModel;
                });
    }

    /**
     * This method uses cache() and the threading model is completely different.
     *
     * @param id Film Id
     * @return Mono<FilmModel>
     */
    private Mono<FilmModel> getFilmModelMonoUsingCache(Long id) {
        Mono<Film> filmMono = filmRepository.findById(id).switchIfEmpty(Mono.error(DataFormatException::new)).cache().subscribeOn(Schedulers.boundedElastic());
        Flux<Actor> actorFlux = filmMono.flatMapMany(this::getByActorId).publishOn(Schedulers.boundedElastic());
        Mono<String> language = filmMono.flatMap(film -> languageRepository.findById(film.getLanguageId())).map(Language::getName);
        Mono<String> category = filmMono.flatMap(film -> filmCategoryRepository
                .findFirstByFilmId(film.getFilmId())
                .flatMap(filmCategory -> categoryRepository.findById(filmCategory.getCategoryId()))
                .map(Category::getName));

        return Mono.zip(filmMono, actorFlux.collectList(), language, category)
                .map(tuple -> {
                    FilmModel filmModel = GenericMapper.INSTANCE.filmToFilmModel(tuple.getT1());
                    List<ActorModel> actors = tuple
                            .getT2()
                            .stream()
                            .map(GenericMapper.INSTANCE::actorToActorModel)
                            .collect(Collectors.toList());
                    filmModel.setActors(actors);
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
                            .map(GenericMapper.INSTANCE::actorToActorModel)
                            .collect(Collectors.toList());
                    filmModel.setActors(actors);
                    filmModel.setLanguage(tuple.getT3());
                    filmModel.setCategory(tuple.getT4());
                    return filmModel;
                });
    }

    private Mono<FilmModel> getFilmModelMonoUsingDefer(Long id) {
        Mono<Film> filmMono = Mono.defer(() -> filmRepository.findById(id).switchIfEmpty(Mono.error(DataFormatException::new)));
        Flux<Actor> actorFlux = filmMono.flatMapMany(this::getByActorId);
        Mono<String> language = filmMono.flatMap(film -> languageRepository.findById(film.getLanguageId())).map(Language::getName);
        Mono<String> category = filmMono.flatMap(film -> filmCategoryRepository
                .findFirstByFilmId(film.getFilmId())
                .flatMap(filmCategory -> categoryRepository.findById(filmCategory.getCategoryId()))
                .map(Category::getName));

        return Mono.zip(filmMono, actorFlux.collectList(), language, category)
                .map(tuple -> {
                    FilmModel filmModel = GenericMapper.INSTANCE.filmToFilmModel(tuple.getT1());
                    List<ActorModel> actors = tuple
                            .getT2()
                            .stream()
                            .map(GenericMapper.INSTANCE::actorToActorModel)
                            .collect(Collectors.toList());
                    filmModel.setActors(actors);
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

    /**
     * Logic :
     * Transaction start
     * -- find Language or insert
     * -- find by Title and release year and languageId or insert
     * -- foreach( actor)
     * ---- find or create actor
     * ---- find or create film_actor ( film id, actor)
     * -- end
     * -- for category
     * ---- find category or insert
     * ---- find film_category or insert
     * -- end
     * -- Map data
     * Transaction end
     *
     * @param filmRequest
     * @return Mono<Pair < Boolean, FilmModel>>
     */
    @Override
    public Mono<Pair<Boolean, FilmModel>> create(FilmRequest filmRequest) {

        return Mono.just(filmRequest)
                .flatMap(this::getOrCreateLanguage)
                .flatMap(languageInput -> getOrCreateFilm(filmRequest, languageInput.getLanguageId()))
                .flatMap(filmInput ->
                        Mono.zip(Mono.just(filmInput),
                                Flux.fromIterable(filmRequest.getActors())
                                        .flatMap(this::getOrCreateActor)
                                        .flatMap(actorInput -> Mono.zip(Mono.just(actorInput), getOrCreateFilmActor(filmInput.getSecond().getFilmId(), actorInput.getActorId())))
                                        .collectList(),
                                Mono.just(filmRequest.getFilmCategory())
                                        .flatMap(this::getOrCreateCategory)
                                        .flatMap(category -> getOrCreateFilmCategory(filmInput.getSecond().getFilmId(), category.getCategoryId()))
                        ))
                .map(tuple -> {
                    FilmModel filmModel = GenericMapper.INSTANCE.filmToFilmModel(tuple.getT1().getSecond());
                    List<ActorModel> actorModelList = tuple.getT2().stream().map(it -> it.getT1()).map(GenericMapper.INSTANCE::actorToActorModel).collect(Collectors.toList());
                    filmModel.setActors(actorModelList);
                    return Pair.of(tuple.getT1().getFirst(), filmModel);
                }).as(operator::transactional);
    }

    public Mono<Actor> getOrCreateActor(final ActorRequest actorRequest) {
        Actor actor = GenericMapper.INSTANCE.actorRequestToActor(actorRequest);
        // first find and if not found then insert
        return Mono.just(actor).
                flatMap(input ->
                        actorRepository.findFirstByFirstNameAndLastName(
                                        input.getFirstName(),
                                        input.getLastName())
                                .switchIfEmpty(Mono.defer(() -> actorRepository.save(actor)))
                );
    }

    private Mono<Language> getOrCreateLanguage(final FilmRequest filmRequest) {
        return Mono.just(filmRequest)
                .flatMap(input -> languageRepository.findFirstByName(input.getLanguage()))
                .switchIfEmpty(Mono.defer(() ->
                        languageRepository
                                .save(Language.builder().name(filmRequest.getLanguage()).build())
                ));

    }

    private Mono<Pair<Boolean, Film>> getOrCreateFilm(final FilmRequest filmRequest, final Long languageId) {
        Film filmTemp = GenericMapper.INSTANCE.filmRequestToFilm(filmRequest);
        final Film film = filmTemp.toBuilder().languageId(languageId).build();
        return Mono.
                just(film)
                .flatMap(input ->
                        filmRepository
                                .findFirstByTitleAndReleaseYearAndLanguageId(filmRequest.getTitle(),
                                        Integer.valueOf(filmRequest.getReleaseYear()),
                                        languageId)
                                .map(existingFilm -> Pair.of(true, existingFilm))
                                .defaultIfEmpty(Pair.of(false, film))
                ).flatMap(tuple -> {
                    if (!tuple.getFirst()) {
                        return filmRepository.save(tuple.getSecond()).map(savedData -> Pair.of(false, savedData));
                    } else
                        return Mono.just(tuple);

                });
    }

    private Mono<FilmActor> getOrCreateFilmActor(final Long filmId, final Long actorId) {
        return Mono.just(Pair.of(filmId, actorId))
                .flatMap(tuple ->
                        filmActorRepository.findByFilmIdAndActorId(tuple.getFirst(), tuple.getSecond()))
                .switchIfEmpty(Mono.defer(() -> filmActorRepository
                        .save(FilmActor
                                .builder()
                                .actorId(actorId)
                                .filmId(filmId)
                                .build())));
    }

    private Mono<Category> getOrCreateCategory(final String category) {
        return Mono.just(category)
                .flatMap(input -> categoryRepository.findByName(category))
                .switchIfEmpty(Mono.defer(() -> categoryRepository.save(Category.builder().name(category).build())));
    }

    private Mono<FilmCategory> getOrCreateFilmCategory(final Long filmId, final Long categoryId) {
        return Mono.just(Pair.of(filmId, categoryId))
                .flatMap(tuple -> filmCategoryRepository.findFirstByCategoryIdAndFilmId(categoryId, filmId))
                .switchIfEmpty(Mono.defer(() ->
                        filmCategoryRepository
                                .save(FilmCategory
                                        .builder()
                                        .categoryId(categoryId).filmId(filmId)
                                        .build())));
    }

}
