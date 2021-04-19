package com.mz.dmq.model.reading;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Reading {
    @Id UUID id;
    @Column(name = "user_email") String userEmail;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "title_id")
    Title title;
    String chapter;
    int page;
    LocalTime time;
    String link;
    String briefing;
    String comment;
}
