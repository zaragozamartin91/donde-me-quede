package com.mz.dmq.model.reading;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadingValue {
    UUID id;
    @Builder.Default String userEmail = "foo@bar.com";
    String titleName;
    String chapter;
    int page;
    LocalTime time;
    String link;
    String briefing;
    String comment;
    Instant createDate;
    List<String> images;
}
