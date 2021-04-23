package com.mz.dmq.repository.reading;

import com.mz.dmq.model.reading.Reading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReadingRepository extends JpaRepository<Reading, UUID> {
}
