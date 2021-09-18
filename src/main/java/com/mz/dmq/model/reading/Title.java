package com.mz.dmq.model.reading;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Title {
    @With @Id UUID id;
    @With String name;
    @EqualsAndHashCode.Exclude Instant createDate;
    long pageid; // wikipedia page id

    public Title(String name, long pageid) {
        this.name = name;
        this.pageid = pageid;
    }

    @PrePersist
    private void prePersist() {
        createDate = ZonedDateTime.now().toInstant();
    }


    public boolean hasPageId() {
        return pageid == 0L;
    }

    public Title withNormalizedName() {
        return this.withName(normalizeTitleName(getName()));
    }

    public Title withRandomId() {
        return this.withId(UUID.randomUUID());
    }

    public static String normalizeTitleName(String name) {
        return Optional.ofNullable(name)
                .map(String::toLowerCase)
                .map(String::strip)
                .map(s -> s.replaceAll(" +", " "))
                .orElseThrow(() -> new IllegalArgumentException("Nombre de titulo no puede ser nulo"));
    }
}
