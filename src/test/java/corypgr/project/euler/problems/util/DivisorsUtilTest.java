package corypgr.project.euler.problems.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

class DivisorsUtilTest {
    private DivisorsUtil util;

    @BeforeEach
    void setup() {
        util = new DivisorsUtil();
    }

    @ParameterizedTest
    @MethodSource("lessThan1Inputs")
    void getDivisors_invalidInputs(long input) {
        assertThrows(IllegalArgumentException.class, () -> util.getDivisors(input));
    }

    @ParameterizedTest
    @MethodSource("getDivisorsExpectedResults")
    void getDivisors_expectedResult(long input, Set<Long> expectedResult) {
        assertThat(util.getDivisors(input), containsInAnyOrder(expectedResult.toArray()));
    }

    static Stream<Arguments> getDivisorsExpectedResults() {
        return Stream.of(
                arguments(1, Set.of(1L)),
                arguments(2, Set.of(1L, 2L)),
                arguments(3, Set.of(1L, 3L)),
                arguments(4, Set.of(1L, 2L, 4L)),
                arguments(5, Set.of(1L, 5L)),
                arguments(6, Set.of(1L, 2L, 3L, 6L)),
                arguments(7, Set.of(1L, 7L)),
                arguments(8, Set.of(1L, 2L, 4L, 8L)),
                arguments(9, Set.of(1L, 3L, 9L)),
                arguments(10, Set.of(1L, 2L, 5L, 10L)));
    }

    @ParameterizedTest
    @MethodSource("lessThan1Inputs")
    void getProperDivisors_invalidInputs(long input) {
        assertThrows(IllegalArgumentException.class, () -> util.getProperDivisors(input));
    }

    @ParameterizedTest
    @MethodSource("getProperDivisorsExpectedResults")
    void getProperDivisors_expectedResult(long input, Set<Long> expectedResult) {
        assertThat(util.getProperDivisors(input), containsInAnyOrder(expectedResult.toArray()));
    }

    static Stream<Arguments> getProperDivisorsExpectedResults() {
        return Stream.of(
                arguments(1, Collections.emptySet()),
                arguments(2, Set.of(1L)),
                arguments(3, Set.of(1L)),
                arguments(4, Set.of(1L, 2L)),
                arguments(5, Set.of(1L)),
                arguments(6, Set.of(1L, 2L, 3L)),
                arguments(7, Set.of(1L)),
                arguments(8, Set.of(1L, 2L, 4L)),
                arguments(9, Set.of(1L, 3L)),
                arguments(10, Set.of(1L, 2L, 5L)));
    }

    @ParameterizedTest
    @MethodSource("lessThan1Inputs")
    void getPrimeDivisors_oneArg_invalidInputs(long input) {
        assertThrows(IllegalArgumentException.class, () -> util.getPrimeDivisors(input));
    }

    @ParameterizedTest
    @MethodSource("getPrimeDivisorsExpectedResults")
    void getPrimeDivisors_oneArg_expectedResult(long input, Set<Long> expectedResult) {
        assertThat(util.getPrimeDivisors(input), containsInAnyOrder(expectedResult.toArray()));
    }

    static Stream<Arguments> getPrimeDivisorsExpectedResults() {
        return Stream.of(
                arguments(1, Collections.emptySet()),
                arguments(2, Set.of(2L)),
                arguments(3, Set.of(3L)),
                arguments(4, Set.of(2L)),
                arguments(5, Set.of(5L)),
                arguments(6, Set.of(2L, 3L)),
                arguments(7, Set.of(7L)),
                arguments(8, Set.of(2L)),
                arguments(9, Set.of(3L)),
                arguments(10, Set.of(2L, 5L)));
    }

    @ParameterizedTest
    @MethodSource("lessThan1Inputs")
    void getPrimeDivisors_withPrimes_invalidInputs(long input) {
        assertThrows(IllegalArgumentException.class, () -> util.getPrimeDivisors(input, List.of(2L)));
    }

    @ParameterizedTest
    @MethodSource("getPrimeDivisorsWithPrimesExpectedResults")
    void getPrimeDivisors_withPrimes_expectedResult(long input, List<Long> primes, Set<Long> expectedResult) {
        assertThat(util.getPrimeDivisors(input, primes), containsInAnyOrder(expectedResult.toArray()));
    }

    /**
     * Most common scenario here is we pass in the primes up to sqrt(value). This might be an empty list, but the code
     * will still determine the single prime divisor above sqrt(value). This is demonstrated in these inputs.
     */
    static Stream<Arguments> getPrimeDivisorsWithPrimesExpectedResults() {
        return Stream.of(
                arguments(1, Collections.emptyList(), Collections.emptySet()),
                arguments(2, Collections.emptyList(), Set.of(2L)),
                arguments(3, Collections.emptyList(), Set.of(3L)),
                arguments(4, List.of(2L), Set.of(2L)),
                arguments(5, List.of(2L), Set.of(5L)),
                arguments(6, List.of(2L), Set.of(2L, 3L)),
                arguments(7, List.of(2L, 3L), Set.of(7L)),
                arguments(8, List.of(2L, 3L), Set.of(2L)),
                arguments(9, List.of(2L, 3L), Set.of(3L)),
                arguments(10, List.of(2L, 3L), Set.of(2L, 5L)));
    }

    @ParameterizedTest
    @MethodSource("lessThan1Inputs")
    void getProperPrimeDivisors_oneArg_invalidInputs(long input) {
        assertThrows(IllegalArgumentException.class, () -> util.getProperPrimeDivisors(input));
    }

    @ParameterizedTest
    @MethodSource("getProperPrimeDivisorsExpectedResults")
    void getProperPrimeDivisors_oneArg_expectedResult(long input, Set<Long> expectedResult) {
        assertThat(util.getProperPrimeDivisors(input), containsInAnyOrder(expectedResult.toArray()));
    }

    static Stream<Arguments> getProperPrimeDivisorsExpectedResults() {
        return Stream.of(
                arguments(1, Collections.emptySet()),
                arguments(2, Collections.emptySet()),
                arguments(3, Collections.emptySet()),
                arguments(4, Set.of(2L)),
                arguments(5, Collections.emptySet()),
                arguments(6, Set.of(2L, 3L)),
                arguments(7, Collections.emptySet()),
                arguments(8, Set.of(2L)),
                arguments(9, Set.of(3L)),
                arguments(10, Set.of(2L, 5L)));
    }

    @ParameterizedTest
    @MethodSource("lessThan1Inputs")
    void getProperPrimeDivisors_withPrimes_invalidInputs(long input) {
        assertThrows(IllegalArgumentException.class, () -> util.getProperPrimeDivisors(input, List.of(2L)));
    }

    @ParameterizedTest
    @MethodSource("getProperPrimeDivisorsWithPrimesExpectedResults")
    void getProperPrimeDivisors_withPrimes_expectedResult(long input, List<Long> primes, Set<Long> expectedResult) {
        assertThat(util.getProperPrimeDivisors(input, primes), containsInAnyOrder(expectedResult.toArray()));
    }

    /**
     * Most common scenario here is we pass in the primes up to sqrt(value). This might be an empty list, but the code
     * will still determine the single prime divisor above sqrt(value). This is demonstrated in these inputs.
     */
    static Stream<Arguments> getProperPrimeDivisorsWithPrimesExpectedResults() {
        return Stream.of(
                arguments(1, Collections.emptyList(), Collections.emptySet()),
                arguments(2, Collections.emptyList(), Collections.emptySet()),
                arguments(3, Collections.emptyList(), Collections.emptySet()),
                arguments(4, List.of(2L), Set.of(2L)),
                arguments(5, List.of(2L), Collections.emptySet()),
                arguments(6, List.of(2L), Set.of(2L, 3L)),
                arguments(7, List.of(2L, 3L), Collections.emptySet()),
                arguments(8, List.of(2L, 3L), Set.of(2L)),
                arguments(9, List.of(2L, 3L), Set.of(3L)),
                arguments(10, List.of(2L, 3L), Set.of(2L, 5L)));
    }

    @ParameterizedTest
    @MethodSource("lessThan1Inputs")
    void getNumDivisors_invalidInputs(long input) {
        assertThrows(IllegalArgumentException.class, () -> util.getNumDivisors(input));
    }

    @ParameterizedTest
    @MethodSource("getNumDivisorsExpectedResults")
    void getNumDivisors_expectedResult(long input, long expectedResult) {
        assertEquals(expectedResult, util.getNumDivisors(input));
    }

    static Stream<Arguments> getNumDivisorsExpectedResults() {
        return Stream.of(
                arguments(1, 1),
                arguments(2, 2),
                arguments(3, 2),
                arguments(4, 3),
                arguments(5, 2),
                arguments(6, 4),
                arguments(7, 2),
                arguments(8, 4),
                arguments(9, 3),
                arguments(10, 4));
    }

    static Stream<Long> lessThan1Inputs() {
        return Stream.of(-10L, -1L, 0L);
    }
}
