package com.mz.dmq.model.wiki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class WikiPage {
    /* {
        "pageid": 530440,
        "ns": 0,
        "title": "One piece",
        "contentmodel": "wikitext",
        "pagelanguage": "en",
        "pagelanguagehtmlcode": "en",
        "pagelanguagedir": "ltr",
        "touched": "2021-04-11T09:59:45Z",
        "lastrevid": 944645104,
        "length": 92,
        "redirect": ""
      }
      */

    long pageid;
    long ns;
    String title;
    String contentmodel;
    String pagelanguage;
    Instant touched;
    long length;
}
