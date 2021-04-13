package com.mz.dmq.usecase.impl;

import com.mz.dmq.gateway.wiki.WikiGateway;
import com.mz.dmq.model.wiki.WikiPage;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@Component("getSuggestions")
public class GetSuggestionsFromWikipedia implements Function<String, List<String>> {
    WikiGateway gateway;

    @Override
    @Cacheable(value = "suggestions", key = "#title.strip()")
    public List<String> apply(String title) {
        log.info("{} not in cache... searching in wikipedia", title);
        return gateway.getSuggestions(title).stream().map(WikiPage::getTitle).collect(Collectors.toList());
    }
}
