package com.mz.dmq.controller;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SuggestionsController {
    Function<String, List<String>> getSuggestions;

    @Autowired
    public SuggestionsController(@Qualifier("getSuggestions") Function<String, List<String>> getSuggestions) {
        this.getSuggestions = getSuggestions;
    }

    @GetMapping("/suggestions/{title}")
    public List<String> getSuggestions(@PathVariable(name = "title") String title) {
        // TODO : add suggestions from database
        return getSuggestions.apply(title);
    }
}
