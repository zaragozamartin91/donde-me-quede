package com.mz.dmq.usecase.reading;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompareImageFiles implements Comparator<String> {
    List<String> promotedWords;
    List<String> demotedWords;

    public CompareImageFiles(
            @Value("${dmq.images.promoted}") List<String> promotedWords,
            @Value("${dmq.images.demoted}") List<String> demotedWords) {
        this.promotedWords = promotedWords.stream().map(this::normalize).collect(Collectors.toList());
        this.demotedWords = demotedWords.stream().map(this::normalize).collect(Collectors.toList()); ;
    }

    @Override
    public int compare(String title1, String title2) {
        // if title1 is cover -> prioritize
        // if title1 is Commons-logo -> deprioritize

        String t1 = normalize(title1);
        String t2 = normalize(title2);

        return isPromoted(t1) || isDemoted(t2) ? -1 :
                isDemoted(t1) || isPromoted(t2) ? +1 :
                        title1.compareTo(title2);
    }

    public CompareImageFiles withPromotedWord(String pw) {
        List<String> npw = Stream.concat(Stream.of(pw), promotedWords.stream()).collect(Collectors.toList());
        return new CompareImageFiles(npw, demotedWords);
    }

    private String normalize(String s) {
        return s.strip().toLowerCase();
    }

    private boolean isPromoted(String title) {
        return promotedWords.stream().anyMatch(title::contains);
    }

    private boolean isDemoted(String title) {
        return demotedWords.stream().anyMatch(title::contains);
    }
}
