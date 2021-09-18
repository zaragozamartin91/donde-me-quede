package com.mz.dmq.usecase.reading;

import com.mz.dmq.model.reading.Reading;
import com.mz.dmq.repository.reading.ReadingRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class GetReadingsByUser implements BiFunction<String, Integer, List<Reading>> {
    ReadingRepository readingRepository;
    int pageSize;

    @Autowired
    public GetReadingsByUser(
            ReadingRepository readingRepository, @Value("${dmq.reading.pagesize}") int pageSize) {
        this.readingRepository = readingRepository;
        this.pageSize = pageSize;
    }

    @Override
    @Transactional
    public List<Reading> apply(String email, Integer page) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return readingRepository.findByReaderIdOrderByCreateDateDesc(email, pageable);
    }
}
