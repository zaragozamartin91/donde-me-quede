package com.mz.dmq.usecase.reading;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CompareImageFilesTest {
    private final CompareImageFiles sut =
            new CompareImageFiles(List.of("cover"), List.of("commons-logo"));

    @Test
    void compare() {
        assertTrue(sut.compare(
                "File:20030712 12 July 2003 One Piece The Going Merry side Odaiba Tokyo Japan.jpg",
                "File:Commons-logo.svg"
        ) < 0);

        assertTrue(sut.compare(
                "File:Commons-logo.svg",
                "File:One Piece, Volume 61 Cover (Japanese).jpg"
        ) > 0);

        assertTrue(sut.compare(
                "ZZZZFile:One Piece, Volume 61 Cover (Japanese).jpg",
                "File:Onepiece-welt (2).png"
        ) < 0);

        assertEquals(sut.compare(
                "File:Strawhat crew jolly roger.svg",
                "File:Onepiece-welt (2).png"
        ), "File:Strawhat crew jolly roger.svg".compareTo("File:Onepiece-welt (2).png"));

        List<String> imgNames = List.of(
                "File:20030712 12 July 2003 One Piece The Going Merry side Odaiba Tokyo Japan.jpg",
                "File:Commons-logo.svg",
                "File:One Piece, Volume 61 Cover (Japanese).jpg",
                "File:Onepiece-welt (2).png",
                "File:Strawhat crew jolly roger.svg"
        );

        List<String> sorted = imgNames.stream()
                .sorted(sut) // cover images go first
                .limit(3L) // fetch 3 images at most
                .collect(Collectors.toList());

        assertFalse(sorted.contains("File:Commons-logo.svg"));

        System.out.println(sorted);
    }
}