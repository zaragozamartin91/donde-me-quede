package com.mz.dmq.controller;

import com.mz.dmq.model.reading.ReadingSuggestion;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SuggestionsController {
    Function<String, List<ReadingSuggestion>> getSuggestions;

    @Autowired
    public SuggestionsController(
            @Qualifier("getSuggestions") Function<String, List<ReadingSuggestion>> getSuggestions) {
        this.getSuggestions = getSuggestions;
    }

    @GetMapping("/suggestions/{title}")
    public List<ReadingSuggestion> getSuggestions(@PathVariable(name = "title") String title) {
        List<ReadingSuggestion> results = getSuggestions.apply(title);

        return results.stream()
                .sorted(Comparator.comparing(ReadingSuggestion::getTitle))
                .collect(Collectors.toList());
    }
}
