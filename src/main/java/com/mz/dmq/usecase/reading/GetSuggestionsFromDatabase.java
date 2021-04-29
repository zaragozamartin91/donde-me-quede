package com.mz.dmq.usecase.reading;

import com.mz.dmq.model.reading.ReadingSuggestion;
import com.mz.dmq.model.reading.Title;
import com.mz.dmq.repository.reading.TitleRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GetSuggestionsFromDatabase implements Function<String, List<ReadingSuggestion>> {
    TitleRepository titleRepository;

    @Override
    public List<ReadingSuggestion> apply(String title) {
        String normalizedTitleName = Title.normalizeTitleName(title);
        return titleRepository.findByNameStartsWith(normalizedTitleName).stream()
                .map(this::fromTitle)
                .map(s -> s.normalize(Title::normalizeTitleName))
                .collect(Collectors.toList());
    }

    private ReadingSuggestion fromTitle(Title title) {
        return new ReadingSuggestion(title.getName(), title.getPageid());
    }
}
