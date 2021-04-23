package com.mz.dmq.usecase.reading;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NormalizeTitleNameTest {
    private final NormalizeTitleName usecase = new NormalizeTitleName();

    @Test
    void apply() {
        assertEquals("one piece", usecase.apply(" One piece   "));
        assertEquals("one piece", usecase.apply("oNe     pIeCe    "));
    }
}