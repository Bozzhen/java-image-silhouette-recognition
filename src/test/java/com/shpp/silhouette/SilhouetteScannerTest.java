package com.shpp.silhouette;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SilhouetteScannerTest {
    private static final String SOURCE = "src/main/resources/assets/";

    @ParameterizedTest
    @MethodSource("provideSilhouettes")
    void testSilhouetteCounting(String fileName, int expectedCount) {
        SilhouetteScanner scanner = new SilhouetteScanner();

        assertEquals(expectedCount, scanner.program(SOURCE + fileName));
    }

    private static Stream<Arguments> provideSilhouettes() {
        return Stream.of(
                Arguments.of("test1.png", 5),
                Arguments.of("test2.png", 2),
                Arguments.of("test3.png", 1),
                Arguments.of("test4.png", 3),
                Arguments.of("test5.png", 3),
                Arguments.of("test6.png", 4),
                Arguments.of("test7.png", 3),
                Arguments.of("test8.png", 1),
                Arguments.of("test9.png", 3),
                Arguments.of("test10.png", 1),
                Arguments.of("test11.png", 6),
                Arguments.of("test12.png", 20),
                Arguments.of("test13.png", 9),
                Arguments.of("test14.png", 10),
                Arguments.of("test15.png", 3),
                Arguments.of("test16.png", 1),
                Arguments.of("test17.png", 3),
                Arguments.of("test18.png", 3),
                Arguments.of("test19.png", 2),
                Arguments.of("test20.png", 2),
                Arguments.of("test21.png", 2),
                Arguments.of("test22.png", 2),
                Arguments.of("test23.png", 8),
                Arguments.of("test24.png", 2),
                Arguments.of("test25.png", 2),
                Arguments.of("test26.png", 6));
    }
}
