package com.mz.dmq.usecase.impl;

import com.mz.dmq.gateway.wiki.WikiGateway;
import com.mz.dmq.model.wiki.WikiPage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component("getSuggestions")
public class GetSuggestionsFromWikipedia implements Function<String, List<String>> {
    WikiGateway gateway;

    @Override
    public List<String> apply(String title) {
        return gateway.getSuggestions(title).stream().map(WikiPage::getTitle).collect(Collectors.toList());
    }
}
