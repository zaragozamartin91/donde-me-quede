package com.mz.dmq.gateway.wiki.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mz.dmq.gateway.wiki.WikiGateway;
import com.mz.dmq.model.reading.ReadingSuggestion;
import com.mz.dmq.model.wiki.WikiImage;
import com.mz.dmq.model.wiki.WikiSearch;
import com.mz.dmq.model.wiki.WikiSearchFragment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class WebWikiGateway implements WikiGateway {
    private final long extractChars;

    public WebWikiGateway(
            @Value("${dmq.wiki.search.limit}") long searchLimit,
            @Value("${dmq.wiki.search.extractChars}") long extractChars) {
        this.extractChars = extractChars;
    }

    @Override
    public List<WikiSearch> searchByTitle(String title, int searchLimit) {
        log.info("Searching wiki pages for {}", title);

        /* Building the uri string this way to avoid "double url escape / expansion" */
        String uriString = String.format("https://en.wikipedia.org/w/api.php?" +
                        "action=%s&generator=%s&gsrsearch=%s&gsrlimit=%d&prop=%s&exchars=%d&exintro&explaintext&format=%s&origin=%s",
                "query", "search", title, searchLimit, "images|extracts|categories", extractChars, "json", "*");

        WikiSearchQueryResult result = new RestTemplate().getForObject(uriString, WikiSearchQueryResult.class);
        log.info("Result is {}", result);

        return Optional.ofNullable(result)
                .map(WikiSearchQueryResult::getQuery)
                .map(WikiSearchQueryResult.WikiPageQueryInnerResult::getPages)
                .map(Map::values)
                .map(ArrayList::new)
                .orElse(new ArrayList<>());
    }

    @Override
    public Optional<WikiSearch> findByPageid(long pageid) {
        // https://en.wikipedia.org//w/api.php?action=query&pageids=360759&prop=extracts|images&exchars=175&format=json&explaintext&origin=*

        log.info("Searching for page {}", pageid);

        String uriString = String.format("https://en.wikipedia.org/w/api.php?" +
                        "action=query&pageids=%d&prop=extracts|images&exchars=%d&format=json&explaintext&origin=*",
                pageid, extractChars);
//24653
        WikiSearchQueryResult result = new RestTemplate().getForObject(uriString, WikiSearchQueryResult.class);
        log.info("Result is {}", result);

        return Optional.ofNullable(result)
                .map(WikiSearchQueryResult::getQuery)
                .map(WikiSearchQueryResult.WikiPageQueryInnerResult::getPages)
                .map(p -> p.get(String.valueOf(pageid)));
    }

    @Override
    public List<WikiImage> getImageUrls(List<String> imgFileNames) {
        log.info("Fetching images for {}", imgFileNames);

        String imgNameParam = String.join("|", imgFileNames);

        // https://en.wikipedia.org/w/api.php?action=query&titles=Image:Onepiece-welt (2).png&prop=imageinfo&iiprop=url&format=json
        String uriString = String.format(
                "https://en.wikipedia.org/w/api.php?action=query&titles=%s&prop=imageinfo&iiprop=url&format=json",
                imgNameParam);

        WikiImageQueryResult result = new RestTemplate().getForObject(uriString, WikiImageQueryResult.class);
        log.info("Result is {}", result);

        return Optional.ofNullable(result)
                .map(WikiImageQueryResult::getQuery)
                .map(WikiImageQueryResult.WikiImageQueryInnerResult::getPages)
                .map(Map::values)
                .map(List::copyOf)
                .orElse(List.of());
    }

    // todo: create custom class for results
    @Override
    public List<ReadingSuggestion> getSuggestions(String title) {
        log.info("Fetching suggestions for {}", title);

        // https://en.wikipedia.org/w/api.php?action=query&list=prefixsearch&format=json&origin=*&pssearch=One%20pi

        String uriString = String.format(
                "https://en.wikipedia.org/w/api.php?action=query&list=prefixsearch&format=json&origin=*&pssearch=%s",
                title);

/*  "query": {
        "prefixsearch": [
          {
            "ns": 0,
            "title": "One Piece",
            "pageid": 360759
          },
... */


        WikiPrefixQueryResult result = new RestTemplate().getForObject(uriString, WikiPrefixQueryResult.class);
        log.info("Result is {}", result);

        return Optional.ofNullable(result)
                .map(WikiPrefixQueryResult::getQuery)
                .map(WikiPrefixQueryResult.WikiPrefixQueryInnerResult::getPrefixsearch)
                .orElse(new ArrayList<>());
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    static class WikiSearchQueryResult {
        private WikiPageQueryInnerResult query;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        static class WikiPageQueryInnerResult {
            private Map<String, WikiSearch> pages;
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

    /*  "query": {
        "prefixsearch": [
          {
            "ns": 0,
            "title": "One Piece",
            "pageid": 360759
          },
... */

    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    static class WikiPrefixQueryResult {
        private WikiPrefixQueryInnerResult query;

        @JsonIgnoreProperties(ignoreUnknown = true)
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        static class WikiPrefixQueryInnerResult {
            private List<ReadingSuggestion> prefixsearch;
        }
    }

    public static void main(String[] args) {
        WebWikiGateway webWikiGateway = new WebWikiGateway(2, 64);

//        List<ReadingSuggestion> suggestions = webWikiGateway.getSuggestions("one pie");
//        System.out.println("Suggestions:");
//        System.out.println(suggestions);

        System.out.println("------------------------------------------------------------");
        Optional<WikiSearch> searchByPageId = webWikiGateway.findByPageid(360759);
        System.out.println(searchByPageId);
    }

    private static WebWikiGateway oldMain(WebWikiGateway webWikiGateway) {
        String title = "One piece";
        List<WikiSearch> wikiSearches = webWikiGateway.searchByTitle(title, 1);
        System.out.println("Searches for " + title);
        System.out.println(wikiSearches);

        List<String> imageNames = wikiSearches.stream()
                .flatMap(w -> Optional.ofNullable(w.getImages()).stream())
                .flatMap(Collection::stream)
                .map(WikiSearchFragment::getTitle)
                .collect(Collectors.toList());

        System.out.println("Images for " + title);
        System.out.println(imageNames);

        List<WikiImage> wikiImages = webWikiGateway.getImageUrls(imageNames);
        System.out.println("Wiki image urls for " + title);
        System.out.println(wikiImages);

        List<ReadingSuggestion> suggestions = webWikiGateway.getSuggestions("One pie");
        System.out.println(suggestions);
        return webWikiGateway;
    }
}
