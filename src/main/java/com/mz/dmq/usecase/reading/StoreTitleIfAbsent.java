package com.mz.dmq.usecase.reading;

import com.mz.dmq.model.reading.Title;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class StoreTitleIfAbsent implements Function<String, Title> {
    Function<String, String> normalizeTitleName;
    Function<String, Optional<Title>> getTitleByName;
    Function<String, Title> storeTitle;

    @Autowired
    public StoreTitleIfAbsent(
            @Qualifier("normalizeTitleName") Function<String, String> normalizeTitleName,
            @Qualifier("getTitleByName") Function<String, Optional<Title>> getTitleByName,
            @Qualifier("storeTitle") Function<String, Title> storeTitle) {
        this.normalizeTitleName = normalizeTitleName;
        this.getTitleByName = getTitleByName;
        this.storeTitle = storeTitle;
    }

    @Override
    public Title apply(String name) {
        String titleName = normalizeTitleName.apply(name);
        return getTitleByName.apply(titleName).orElseGet(() -> storeTitle.apply(titleName));
    }
}
