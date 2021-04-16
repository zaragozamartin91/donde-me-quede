package com.mz.dmq.usecase.impl;

import com.mz.dmq.model.reading.Title;
import com.mz.dmq.repository.reading.TitleRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component(value = "getTitleByName")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetTitleByNameFromDb implements Function<String, Optional<Title>> {
    TitleRepository repository;

    @Override
    public Optional<Title> apply(String name) {
        return repository.findByName(name);
    }
}
