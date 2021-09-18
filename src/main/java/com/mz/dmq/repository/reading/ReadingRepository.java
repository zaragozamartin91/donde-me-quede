package com.mz.dmq.repository.reading;

import com.mz.dmq.model.reading.Reading;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReadingRepository extends PagingAndSortingRepository<Reading, UUID> {
    @Query("FROM Reading as reading " +
            "JOIN FETCH reading.title " +
            "WHERE reading.readerId = :readerId " +
            "ORDER BY reading.createDate DESC ")
    List<Reading> findByReaderIdOrderByCreateDateDesc(@Param("readerId") String readerId, Pageable pageable);
}
