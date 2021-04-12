package com.mz.dmq.gateway.wiki.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mz.dmq.gateway.wiki.WikiGateway;
import com.mz.dmq.model.wiki.WikiImage;
import com.mz.dmq.model.wiki.WikiPage;
import com.mz.dmq.model.wiki.WikiPageFragment;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebWikiGateway implements WikiGateway {
    private final long searchLimit;
    private final long extractChars;

    public WebWikiGateway(
            @Value("${dmq.wiki.search.limit}") long searchLimit,
            @Value("${dmq.wiki.search.extractChars}") long extractChars) {
        this.searchLimit = searchLimit;
        this.extractChars = extractChars;
    }

    public List<WikiPage> searchByTitle(String title) {
        log.info("Searching wiki pages for {}", title);

        /* Building the uri string this way to avoid "double url escape / expansion" */
        String uriString = String.format("https://en.wikipedia.org/w/api.php?" +
                        "action=%s&generator=%s&gsrsearch=%s&gsrlimit=%d&prop=%s&exchars=%d&exintro&explaintext&format=%s&origin=%s",
                "query", "search", title, searchLimit, "images|extracts|categories", extractChars, "json", "*");

        WikiPageQueryResult result = new RestTemplate().getForObject(uriString, WikiPageQueryResult.class);
        log.info("Result is {}", result);

        return Optional.ofNullable(result)
                .map(WikiPageQueryResult::getQuery)
                .map(WikiPageQueryResult.WikiPageQueryInnerResult::getPages)
                .map(Map::values)
                .map(ArrayList::new)
                .orElse(new ArrayList<>());
    }

    public Optional<WikiImage> getImage(String name) {
        log.info("Fetching image {}", name);

        // https://en.wikipedia.org/w/api.php?action=query&titles=Image:Onepiece-welt (2).png&prop=imageinfo&iiprop=url&format=json
        String uriString = String.format(
                "https://en.wikipedia.org/w/api.php?action=query&titles=%s&prop=imageinfo&iiprop=url&format=json", name);

        WikiImageQueryResult result = new RestTemplate().getForObject(uriString, WikiImageQueryResult.class);
        log.info("Result is {}", result);

        return Optional.ofNullable(result)
                .map(WikiImageQueryResult::getQuery)
                .map(WikiImageQueryResult.WikiImageQueryInnerResult::getPages)
                .map(Map::values)
                .flatMap(v -> v.stream().findFirst());
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    static class WikiPageQueryResult {
        private WikiPageQueryInnerResult query;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        static class WikiPageQueryInnerResult {
            private Map<String, WikiPage> pages;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    static class WikiImageQueryResult {
        private WikiImageQueryInnerResult query;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        static class WikiImageQueryInnerResult {
            private Map<String, WikiImage> pages;
        }
    }

    public static void main(String[] args) {
        WebWikiGateway webWikiGateway = new WebWikiGateway(2, 64);
        List<WikiPage> wikiPages = webWikiGateway.searchByTitle("kliasd");
        System.out.println(wikiPages);

        List<String> imageNames = wikiPages.stream()
                .flatMap(w -> Optional.ofNullable(w.getImages()).stream())
                .flatMap(Collection::stream)
                .map(WikiPageFragment::getTitle)
                .collect(Collectors.toList());

        System.out.println(imageNames);

        WikiImage wikiImage = imageNames.stream().findFirst().flatMap(webWikiGateway::getImage).orElseThrow();
        System.out.println(wikiImage);
    }
}
