package com.mz.dmq.model.reading;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Title {
    @Id UUID id;
    String name;
    Instant createDate;

    @PrePersist
    private void prePersist() {
        createDate = ZonedDateTime.now().toInstant();
    }
}
