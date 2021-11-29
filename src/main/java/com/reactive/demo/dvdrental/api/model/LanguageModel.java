package com.reactive.demo.dvdrental.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(title = "Language Model")
@Data
public class LanguageModel {
    private long languageId;
    private String name;
}
