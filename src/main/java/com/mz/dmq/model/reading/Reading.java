package com.mz.dmq.model.reading;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Reading {
    @Id UUID id;
    @Column(name = "reader_id") String readerId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title_id")
    Title title;
    String chapter;
    int page;
    LocalTime time;
    String link;
    String briefing;
    String comment;
    Instant createDate;

    @PrePersist
    private void prePersist() {
        createDate = ZonedDateTime.now().toInstant();
    }

    public String getTitleName() {
        return getTitle().getName();
    }
}
