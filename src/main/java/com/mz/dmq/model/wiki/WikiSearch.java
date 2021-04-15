package com.mz.dmq.model.wiki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class WikiSearch {
    long pageid;
    long ns;
    String title;
    long index;
    List<WikiSearchFragment> images;
    String extract;
    List<WikiSearchFragment> categories;
}
