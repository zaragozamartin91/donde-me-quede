package com.mz.dmq.usecase.wiki;

import com.mz.dmq.gateway.wiki.WikiGateway;
import com.mz.dmq.model.reading.ReadingSuggestion;
import com.mz.dmq.model.reading.Title;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class GetSuggestionsFromWikipedia implements Function<String, List<ReadingSuggestion>> {
    WikiGateway gateway;

    @Override
    public List<ReadingSuggestion> apply(String title) {
        String normalizedTitleName = Title.normalizeTitleName(title);
        return gateway.getSuggestions(normalizedTitleName).stream()
                .map(s -> s.normalize(Title::normalizeTitleName))
                .collect(Collectors.toList());
    }
}
