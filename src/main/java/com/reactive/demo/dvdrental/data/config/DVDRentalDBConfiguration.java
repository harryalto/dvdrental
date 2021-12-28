package com.reactive.demo.dvdrental.data.config;


import com.reactive.demo.dvdrental.data.entity.Film;
import com.reactive.demo.dvdrental.data.mapper.EnumCodecSpecial;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.List;


@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories
@EnableR2dbcAuditing
public class DVDRentalDBConfiguration extends AbstractR2dbcConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        System.out.println("Initializing postgreSQL connection factory");
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host("localhost")
                        .database("dvdrental")
                        .username("postgres")
                        .password("postgres")
                        .codecRegistrar(EnumCodecSpecial.builder().withEnum("mpaa_rating", Film.MpaaRating.class).build())
                        .build()
        );
    }

    @Override
    protected List<Object> getCustomConverters() {
        return List.of(new FilmReadingConverter(), new RatingWritingConverter());
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        System.out.println("Initializing postgreSQL connection factory");
        return new R2dbcTransactionManager(connectionFactory);
    }
}
