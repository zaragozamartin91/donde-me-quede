package com.mz.dmq.gateway.wiki;

import com.mz.dmq.model.reading.ReadingSuggestion;
import com.mz.dmq.model.wiki.WikiImage;
import com.mz.dmq.model.wiki.WikiSearch;

import java.util.List;
import java.util.Optional;

public interface WikiGateway {
    List<WikiSearch> searchByTitle(String title, int searchLimit);

    Optional<WikiSearch> findByPageid(long pageid);

    List<WikiImage> getImageUrls(List<String> imgNames);

    List<ReadingSuggestion> getSuggestions(String title);
}
