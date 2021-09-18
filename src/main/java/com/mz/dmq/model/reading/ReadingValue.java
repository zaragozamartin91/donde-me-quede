package com.mz.dmq.model.reading;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ReadingValue {
    UUID id;
    String userEmail;
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
