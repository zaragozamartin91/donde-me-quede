package com.mz.dmq.model.wiki;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class WikiImageInfo {
    String url;
    String descriptionurl;
    String descriptionshorturl;
}
