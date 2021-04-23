package com.mz.dmq.model.wiki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class WikiImage {
    long ns;
    String title;
    String imagerepository;
    @JsonProperty("imageinfo") List<WikiImageInfo> imageinfo;

    public List<String> getImageUrls() {
        return getImageinfo().stream().map(WikiImageInfo::getUrl).collect(Collectors.toList());
    }
}
