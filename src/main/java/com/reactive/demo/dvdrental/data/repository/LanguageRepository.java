package com.reactive.demo.dvdrental.data.repository;

import com.reactive.demo.dvdrental.data.entity.Language;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends ReactiveCrudRepository<Language, Long> {
}
