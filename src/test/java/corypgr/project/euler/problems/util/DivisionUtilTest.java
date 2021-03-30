package corypgr.project.euler.problems.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class DivisionUtilTest {
    private DivisionUtil util;

    @BeforeEach
    void setup() {
        util = new DivisionUtil();
    }

    @ParameterizedTest
    @MethodSource("inputAndResults")
    void valuesWithExpectedResults(int numerator, int denominator, String expectedResult) {
        assertEquals(expectedResult, util.divideWithRepeatingDecimalRepresentation(numerator, denominator));
    }

    static Stream<Arguments> inputAndResults() {
        return Stream.of(
                arguments(1, 1, "1"),
                arguments(1, 2, "0.5"),
                arguments(1, 3, "0.(3)"),
                arguments(1, 4, "0.25"),
                arguments(1, 5, "0.2"),
                arguments(1, 6, "0.1(6)"),
                arguments(1, 7, "0.(142857)"),
                arguments(2, 3, "0.(6)"),
                arguments(5, 5, "1"),
                arguments(10, 5, "2"),
                arguments(5, 4, "1.25"),
                arguments(7, 6, "1.1(6)"),
                arguments(8, 7, "1.(142857)"));
    }

    @Test
    void zeroDenominatorThrows() {
        assertThrows(IllegalArgumentException.class, () -> util.divideWithRepeatingDecimalRepresentation(10, 0));
    }
}
