package com.mz.dmq.usecase.reading;

import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class NormalizeTitleName implements Function<String, String> {
    @Override
    public String apply(String name) {
        return Optional.ofNullable(name)
                .map(String::toLowerCase)
                .map(String::strip)
                .map(s -> s.replaceAll(" +", " "))
                .orElseThrow(() -> new IllegalArgumentException("Nombre de titulo no puede ser nulo"));
    }
}
