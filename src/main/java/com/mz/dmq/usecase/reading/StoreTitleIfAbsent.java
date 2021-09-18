package com.mz.dmq.usecase.reading;

import com.mz.dmq.model.reading.Title;
import com.mz.dmq.repository.reading.TitleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StoreTitleIfAbsent implements Function<Title, Title> {
    TitleRepository titleRepository;


    @Autowired
    public StoreTitleIfAbsent(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Override
    public Title apply(Title title) {
        Title newTitle = title.withNormalizedName();
        return titleRepository
                .findByNameAndPageid(newTitle.getName(), newTitle.getPageid())
                .orElseGet(() -> titleRepository.save(newTitle.withRandomId()));
    }
}
