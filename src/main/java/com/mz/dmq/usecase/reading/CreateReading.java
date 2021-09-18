package com.mz.dmq.usecase.reading;

import com.mz.dmq.model.reading.CreateReadingRequest;
import com.mz.dmq.model.reading.Reading;
import com.mz.dmq.model.reading.Title;
import com.mz.dmq.repository.reading.ReadingRepository;
import com.mz.dmq.util.TriFunction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CreateReading implements TriFunction<String, Title, CreateReadingRequest, Reading> {
    ReadingRepository readingRepository;

    @Override
    public Reading apply(String readerId, Title title, CreateReadingRequest rr) {
        Reading reading = Reading.builder()
                .chapter(rr.getChapter())
                .id(UUID.randomUUID())
                .briefing(rr.getBriefing())
                .comment(rr.getComment())
                .link(rr.getLink())
                .page(rr.getPage())
                .time(rr.getTime())
                .title(title)
                .readerId(readerId)
                .build();
        return readingRepository.save(reading);
    }
}
