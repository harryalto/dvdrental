package com.reactive.demo.dvdrental;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class Pair<F, S> {
    private F first;
    private S second;
}