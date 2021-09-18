package com.mz.dmq.model.reading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.function.Function;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class ReadingSuggestion {
    @With String title;
    long pageid;

    public ReadingSuggestion normalize(Function<String, String> titleNormalizer) {
        return titleNormalizer.andThen(this::withTitle).apply(getTitle());
    }
}
