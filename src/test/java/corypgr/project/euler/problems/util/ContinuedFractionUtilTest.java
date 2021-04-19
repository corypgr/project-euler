package corypgr.project.euler.problems.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import corypgr.project.euler.problems.util.ContinuedFractionUtil.ContinuedFraction;
import corypgr.project.euler.problems.util.ContinuedFractionUtil.RationalFraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

class ContinuedFractionUtilTest {
    private ContinuedFractionUtil util;

    @BeforeEach
    void setup() {
        util = new ContinuedFractionUtil();
    }

    @ParameterizedTest
    @ValueSource(ints = { -10, -4, -1})
    void getContinuedFractionOfSquareRoot_negativeNumbers_throws(int n) {
        assertThrows(IllegalArgumentException.class, () -> util.getContinuedFractionOfSquareRoot(n));
    }

    @ParameterizedTest
    @MethodSource("continuedFractionInputAndExpectedOutput")
    void getContinuedFractionOfSquareRoot_returnsExpected(int n, ContinuedFraction expectedOutput) {
        assertEquals(expectedOutput, util.getContinuedFractionOfSquareRoot(n));
    }

    static Stream<Arguments> continuedFractionInputAndExpectedOutput() {
        return Stream.of(
                arguments(0, new ContinuedFraction(0, Collections.emptyList())),
                arguments(1, new ContinuedFraction(1, Collections.emptyList())),
                arguments(2, new ContinuedFraction(1, List.of(2))),
                arguments(3, new ContinuedFraction(1, List.of(1, 2))),
                arguments(4, new ContinuedFraction(2, Collections.emptyList())),
                arguments(5, new ContinuedFraction(2, List.of(4))),
                arguments(6, new ContinuedFraction(2, List.of(2, 4))),
                arguments(7, new ContinuedFraction(2, List.of(1, 1, 1, 4))),
                arguments(13, new ContinuedFraction(3, List.of(1, 1, 1, 1, 6))),
                arguments(23, new ContinuedFraction(4, List.of(1, 3, 1, 8))));
    }

    @ParameterizedTest
    @ValueSource(ints = { -10, -4, -1})
    void getConvergentsOfSquareRoot_negativeNumbers_throws(int n) {
        assertThrows(IllegalArgumentException.class, () -> util.getConvergentsOfSquareRoot(n));
    }

    @ParameterizedTest
    @MethodSource("convergentsSquareInputsAndExpectedOutput")
    void getConvergentsOfSquareRoot_squareInputs_returnsExpected(int n, RationalFraction onlyResult) {
        Iterator<RationalFraction> iterator = util.getConvergentsOfSquareRoot(n);

        assertTrue(iterator.hasNext());
        assertEquals(onlyResult, iterator.next());
        // After the only convergent is removed, the iterator should be empty.
        assertFalse(iterator.hasNext());
    }

    static Stream<Arguments> convergentsSquareInputsAndExpectedOutput() {
        return Stream.of(
                arguments(0, new RationalFraction(BigInteger.ZERO, BigInteger.ONE)),
                arguments(1, new RationalFraction(BigInteger.ONE, BigInteger.ONE)),
                arguments(4, new RationalFraction(BigInteger.TWO, BigInteger.ONE)));
    }

    @ParameterizedTest
    @MethodSource("convergentsIntegerInputsAndExpectedOutput")
    void getConvergentsOfSquareRoot_returnsExpected(int n, List<RationalFraction> first4Results) {
        Iterator<RationalFraction> iterator = util.getConvergentsOfSquareRoot(n);
        for (RationalFraction expected : first4Results) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
        assertTrue(iterator.hasNext());
    }

    static Stream<Arguments> convergentsIntegerInputsAndExpectedOutput() {
        return Stream.of(
                arguments(2, List.of(
                        new RationalFraction(BigInteger.ONE, BigInteger.ONE),
                        new RationalFraction(BigInteger.valueOf(3), BigInteger.TWO),
                        new RationalFraction(BigInteger.valueOf(7), BigInteger.valueOf(5)),
                        new RationalFraction(BigInteger.valueOf(17), BigInteger.valueOf(12)))),
                arguments(3, List.of(
                        new RationalFraction(BigInteger.ONE, BigInteger.ONE),
                        new RationalFraction(BigInteger.TWO, BigInteger.ONE),
                        new RationalFraction(BigInteger.valueOf(5), BigInteger.valueOf(3)),
                        new RationalFraction(BigInteger.valueOf(7), BigInteger.valueOf(4)))),
                arguments(7, List.of(
                        new RationalFraction(BigInteger.TWO, BigInteger.ONE),
                        new RationalFraction(BigInteger.valueOf(3), BigInteger.ONE),
                        new RationalFraction(BigInteger.valueOf(5), BigInteger.TWO),
                        new RationalFraction(BigInteger.valueOf(8), BigInteger.valueOf(3)))));
    }

    @Test
    void getConvergentsUsingContinuedFractionVals_testEExample() {
        Iterator<Integer> eContinuedFractionIterator = Stream.of(1, 2, 1, 1, 4, 1, 1, 6, 1).iterator();

        List<RationalFraction> expectedFirst10Convergents = List.of(
                new RationalFraction(BigInteger.TWO, BigInteger.ONE),
                new RationalFraction(BigInteger.valueOf(3), BigInteger.ONE),
                new RationalFraction(BigInteger.valueOf(8), BigInteger.valueOf(3)),
                new RationalFraction(BigInteger.valueOf(11), BigInteger.valueOf(4)),
                new RationalFraction(BigInteger.valueOf(19), BigInteger.valueOf(7)),
                new RationalFraction(BigInteger.valueOf(87), BigInteger.valueOf(32)),
                new RationalFraction(BigInteger.valueOf(106), BigInteger.valueOf(39)),
                new RationalFraction(BigInteger.valueOf(193), BigInteger.valueOf(71)),
                new RationalFraction(BigInteger.valueOf(1264), BigInteger.valueOf(465)),
                new RationalFraction(BigInteger.valueOf(1457), BigInteger.valueOf(536)));

        Iterator<RationalFraction> eConvergentIterator =
                util.getConvergentsUsingContinuedFractionVals(2, eContinuedFractionIterator);

        for (RationalFraction expected : expectedFirst10Convergents) {
            assertTrue(eConvergentIterator.hasNext());
            assertEquals(expected, eConvergentIterator.next());
        }

        // e has an infinite number of convergents, but since we provided a limited number of continued fraction values
        // our iterator should stop here.
        assertFalse(eConvergentIterator.hasNext());
    }
}
