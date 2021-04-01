package com.mz.dmq.usecase.impl;

import com.mz.dmq.model.reading.Title;
import com.mz.dmq.repository.TitleRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.Function;

@Component(value = "storeTitle")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StoreTitle2db implements Function<String, Title> {
    TitleRepository repository;

    @Override
    public Title apply(String name) {
        Title title = Title.builder().id(UUID.randomUUID()).name(name).build();
        return repository.save(title);
    }
}
