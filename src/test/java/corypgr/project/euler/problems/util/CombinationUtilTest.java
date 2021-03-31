package corypgr.project.euler.problems.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class CombinationUtilTest {
    private CombinationUtil<Long> util;

    @BeforeEach
    void setup() {
        util = new CombinationUtil<>();
    }

    @ParameterizedTest
    @MethodSource("inputsAndOutputs")
    void testInputsAndOutputs(List<Long> inputValues, int numElements, List<List<Long>> expectedOutput) {
        assertEquals(expectedOutput, util.getAllCombinationsWithRepeats(inputValues, numElements));
    }

    static Stream<Arguments> inputsAndOutputs() {
        return Stream.of(
                // empty cases
                arguments(null, 3, Collections.emptyList()),
                arguments(Collections.emptyList(), 3, Collections.emptyList()),
                arguments(List.of(1L, 2L), 0, Collections.emptyList()),
                arguments(List.of(1L, 2L), -1, Collections.emptyList()),

                // normal cases
                arguments(List.of(1L, 2L), 1, List.of(
                        List.of(1L),
                        List.of(2L))),
                arguments(List.of(1L), 2, List.of(
                        List.of(1L, 1L))),
                arguments(List.of(1L, 2L), 2, List.of(
                        List.of(1L, 1L),
                        List.of(1L, 2L),
                        List.of(2L, 2L))),
                arguments(List.of(1L, 2L), 3, List.of(
                        List.of(1L, 1L, 1L),
                        List.of(1L, 1L, 2L),
                        List.of(1L, 2L, 2L),
                        List.of(2L, 2L, 2L))));
    }
}
