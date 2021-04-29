package com.mz.dmq.usecase.reading;

import com.mz.dmq.gateway.wiki.WikiGateway;
import com.mz.dmq.model.reading.Title;
import com.mz.dmq.model.wiki.WikiImageInfo;
import com.mz.dmq.model.wiki.WikiSearch;
import com.mz.dmq.model.wiki.WikiSearchFragment;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class GetTitleImages implements Function<Title, List<String>> {
    WikiGateway wikiGateway;
    Comparator<String> compareImageFiles;

    @Autowired
    public GetTitleImages(WikiGateway wikiGateway,
                          @Qualifier("compareImageFiles") Comparator<String> compareImageFiles) {
        this.wikiGateway = wikiGateway;
        this.compareImageFiles = compareImageFiles;
    }

    @Override
    @Cacheable(value = "suggestions", key = "#title.getPageid()")
    public List<String> apply(Title title) {
        return Optional.of(title)
                .map(Title::getPageid)
                .filter(pid -> pid > 0)
                .map(this::fetchImages)
                .orElseGet(List::of);
    }

    private List<String> fetchImages(long pageid) {
        List<WikiSearchFragment> wikiImages = wikiGateway.findByPageid(pageid)
                .map(WikiSearch::getImages)
                .orElse(List.of());

        /* Comparing image names no prioritize covers */
        List<String> imgFileNames = wikiImages.stream()
                .map(WikiSearchFragment::getTitle)
                .sorted(compareImageFiles) // cover images go first
                .limit(3L) // fetch 3 images at most
                .collect(Collectors.toList());

        return wikiGateway.getImageUrls(imgFileNames).stream()
                .flatMap(w -> w.getImageinfo().stream().findFirst().stream())
                .map(WikiImageInfo::getUrl)
                .sorted(compareImageFiles) // sorting urls again to show cover images first
                .collect(Collectors.toList());
    }
}
