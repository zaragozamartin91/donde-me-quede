package com.mz.dmq.gateway.wiki;

import com.mz.dmq.model.wiki.WikiImage;
import com.mz.dmq.model.wiki.WikiPage;
import com.mz.dmq.model.wiki.WikiSearch;

import java.util.List;
import java.util.Optional;

public interface WikiGateway {
    List<WikiSearch> searchByTitle(String title, int searchLimit);

    Optional<WikiImage> getImage(String name);

    List<WikiPage> getSuggestions(String title);
}
