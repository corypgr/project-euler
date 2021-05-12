package corypgr.project.euler.problems.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Stream;

class TotientFunctionUtilTest {
    private TotientFunctionUtil util;

    @BeforeEach
    void setup() {
        util = new TotientFunctionUtil();
    }

    @ParameterizedTest
    @ValueSource(longs = { -10, -1, 0 })
    void calculateTotientFunction_oneArg_invalidInput(long input) {
        assertThrows(IllegalArgumentException.class, () -> util.calculateTotientFunction(input));
    }

    @ParameterizedTest
    @MethodSource("expectedResultsForOneArgMethod")
    void calculateTotientFunction_oneArg_expectedResult(long input, long expectedResult) {
        assertEquals(expectedResult, util.calculateTotientFunction(input));
    }

    static Stream<Arguments> expectedResultsForOneArgMethod() {
        return Stream.of(
                arguments(1, 1),
                arguments(2, 1),
                arguments(3, 2),
                arguments(4, 2),
                arguments(5, 4),
                arguments(6, 2),
                arguments(7, 6),
                arguments(8, 4),
                arguments(9, 6),
                arguments(10, 4));
    }

    @ParameterizedTest
    @MethodSource("invalidInputsWithDivisors")
    void calculateTotientFunction_withDivisors_invalidInput(long value, Set<Long> primeDivisors) {
        assertThrows(IllegalArgumentException.class, () -> util.calculateTotientFunction(value, primeDivisors));
    }

    static Stream<Arguments> invalidInputsWithDivisors() {
        return Stream.of(
                arguments(-10, Set.of(2L)),
                arguments(-1, Set.of(2L)),
                arguments(0, Set.of(2L)),
                arguments(2, null),
                arguments(2, Collections.emptySet()),
                arguments(-1, null));
    }

    @ParameterizedTest
    @MethodSource("expectedResultsWithDivisors")
    void calculateTotientFunction_oneArg_expectedResult(long value, Set<Long> primeDivisors, long expectedResult) {
        assertEquals(expectedResult, util.calculateTotientFunction(value, primeDivisors));
    }

    static Stream<Arguments> expectedResultsWithDivisors() {
        return Stream.of(
                arguments(1, Collections.emptySet(), 1),
                arguments(2, Set.of(2L), 1),
                arguments(3, Set.of(3L), 2),
                arguments(4, Set.of(2L), 2),
                arguments(5, Set.of(5L), 4),
                arguments(6, Set.of(2L, 3L), 2),
                arguments(7, Set.of(7L), 6),
                arguments(8, Set.of(2L), 4),
                arguments(9, Set.of(3L), 6),
                arguments(10, Set.of(2L, 5L), 4));
    }
}
