package com.mz.dmq.repository.reading;

import com.mz.dmq.model.reading.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TitleRepository extends JpaRepository<Title, UUID> {
    Optional<Title> findByNameAndPageid(String name, long pageid);

    List<Title> findByNameStartsWith(String name);
}
