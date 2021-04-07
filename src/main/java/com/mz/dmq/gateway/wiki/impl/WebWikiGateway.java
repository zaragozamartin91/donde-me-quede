package com.mz.dmq.gateway.wiki.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mz.dmq.gateway.wiki.WikiGateway;
import com.mz.dmq.model.wiki.WikiPage;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

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
        /* Building the uri string this way to avoid "double url escape / expansion" */
        String uriString = String.format(
                "https://en.wikipedia.org/w/api.php?" +
                        "action=%s&generator=%s&gsrsearch=%s&gsrlimit=%d&prop=%s&exchars=%d&exintro&explaintext&format=%s&origin=%s",
                "query", "search", title, searchLimit, "images|extracts|categories", extractChars, "json", "*");

        /*
         * https://en.wikipedia.org/w/api.php?action=query&generator=search&gsrsearch=one_piece&gsrlimit=2&prop=images|extracts|categories&exchars=64&exintro&explaintext&exlimit=max&format=json&origin=*
         * */

//        String uriString = UriComponentsBuilder.fromHttpUrl("https://en.wikipedia.org/w/api.php")
//                .queryParam("action", "query")
//                .queryParam("generator", "search")
//                .queryParam("gsrsearch", title)
//                .queryParam("gsrlimit", searchLimit)
//                .queryParam("prop", "images|extracts|categories")
//                .queryParam("exchars", extractChars)
//                .queryParam("exintro", true)
//                .queryParam("explaintext", true)
//                .queryParam("format", "json")
//                .queryParam("origin", "*")
//                .build(false)
//                .toUri()
//                .toString();

        log.info("Raw uri string is {}", uriString);

        WikiQueryResult result = new RestTemplate().getForObject(uriString, WikiQueryResult.class);
        log.info("Result is {}", result);

        return List.of();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    static class WikiQueryResult {
        WikiQueryInnerResult query;


        @JsonIgnoreProperties(ignoreUnknown = true)
        @FieldDefaults(level = AccessLevel.PRIVATE)
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @ToString
        static class WikiQueryInnerResult {
            Map<String, WikiPage> pages;
        }
    }

    public static void main(String[] args) {
        WebWikiGateway webWikiGateway = new WebWikiGateway(2, 64);
        webWikiGateway.searchByTitle("One piece");
    }
}
