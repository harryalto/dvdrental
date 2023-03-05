package com.reactive.demo.dvdrental.data.config;

import com.reactive.demo.dvdrental.data.entity.Film;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.convert.EnumWriteSupport;


@WritingConverter
class RatingWritingConverter extends EnumWriteSupport<Film.MpaaRating> {
}