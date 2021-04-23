package com.mz.dmq.controller;

import com.mz.dmq.model.reading.CreateReadingRequest;
import com.mz.dmq.model.reading.Reading;
import com.mz.dmq.model.reading.Title;
import com.mz.dmq.repository.reading.ReadingRepository;
import com.mz.dmq.repository.reading.TitleRepository;
import com.mz.dmq.usecase.reading.CreateReading;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController("/poc")
public class PocRestController {
    CreateReading createReading;
    TitleRepository titleRepository;

    @PostMapping("/reading")
    public String postReading(CreateReadingRequest createReadingRequest) {
        Title title = titleRepository.findAll().stream().findAny().orElseThrow();
        Reading newReading = createReading.apply("zaragozamartin91@gmail.com", title, createReadingRequest);
        return newReading.getId().toString();
    }
}
