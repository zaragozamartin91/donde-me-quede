package com.mz.dmq.repository;

import com.mz.dmq.model.reading.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TitleRepository extends JpaRepository<Title, UUID> {
    Optional<Title> findByName(String name);
}
