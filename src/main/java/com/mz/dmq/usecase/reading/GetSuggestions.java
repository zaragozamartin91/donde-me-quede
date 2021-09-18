package com.mz.dmq.usecase.reading;

import com.mz.dmq.model.reading.ReadingSuggestion;
import com.mz.dmq.model.reading.Title;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class GetSuggestions implements Function<String, List<ReadingSuggestion>> {
    Function<String, List<ReadingSuggestion>> fromWikipedia;
    Function<String, List<ReadingSuggestion>> fromDatabase;

    @Autowired
    public GetSuggestions(
            @Qualifier("getSuggestionsFromWikipedia") Function<String, List<ReadingSuggestion>> fromWikipedia,
            @Qualifier("getSuggestionsFromDatabase") Function<String, List<ReadingSuggestion>> fromDatabase) {
        this.fromWikipedia = fromWikipedia;
        this.fromDatabase = fromDatabase;
    }

    @Override
    @Cacheable(value = "suggestions", key = "#title.strip().toLowerCase()")
    public List<ReadingSuggestion> apply(String title) {
        log.info("{} not in cache... searching in wikipedia", title);

        String normalizedTitleName = Title.normalizeTitleName(title);

        CompletableFuture<List<ReadingSuggestion>> wikiSuggestionsFuture =
                CompletableFuture.supplyAsync(() -> fromWikipedia.apply(normalizedTitleName));
        CompletableFuture<List<ReadingSuggestion>> dbSuggestionsFuture =
                CompletableFuture.supplyAsync(() -> fromDatabase.apply(normalizedTitleName));

        return Stream.concat(
                dbSuggestionsFuture.join().stream(),
                wikiSuggestionsFuture.join().stream()
        ).collect(Collectors.collectingAndThen(Collectors.toSet(), List::copyOf));
    }
}
