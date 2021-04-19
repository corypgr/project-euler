package corypgr.project.euler.problems.util;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility for generating the repeating continued fractions of squared roots, as well as getting the convergents of
 * a continued fraction.
 */
public class ContinuedFractionUtil {
    public ContinuedFraction getContinuedFractionOfSquareRoot(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Only works on positive square root values.");
        }

        CustomFraction fraction = new CustomFraction(n);
        int wholePart = fraction.getIntValue();
        if (wholePart * wholePart == n) {
            // Not irrational
            return new ContinuedFraction(wholePart, Collections.emptyList());
        }

        List<Integer> continuedList = new LinkedList<>();
        int intVal = wholePart;
        int maxIntVal = wholePart * 2;
        while (intVal != maxIntVal) {
            // At each step the next fraction in the continued fraction is calculated by first subtracting the last
            // intVal and then taking the reciprocal.
            fraction = fraction.subtract(intVal).reciprocal();

            intVal = fraction.getIntValue();
            continuedList.add(intVal);
        }
        return new ContinuedFraction(wholePart, continuedList);
    }

    /**
     * Returns an Iterator because the series of convergents is infinite. The Iterator will be infinite unless n is a
     * square.
     */
    public Iterator<RationalFraction> getConvergentsOfSquareRoot(int n) {
        ContinuedFraction continuedFraction = getContinuedFractionOfSquareRoot(n);
        if (continuedFraction.getRepeatedContinuedPart().isEmpty()) {
            return getConvergentsUsingContinuedFractionVals(continuedFraction.getWholePart(), Collections.emptyIterator());
        }

        Iterator<Integer> continuedFractionIterator = new Iterator<>() {
            private Iterator<Integer> continuedFractionIterator = continuedFraction.getRepeatedContinuedPart().iterator();

            @Override
            public boolean hasNext() {
                return true; // Cyclic. Always true.
            }

            @Override
            public Integer next() {
                if (!continuedFractionIterator.hasNext()) {
                    continuedFractionIterator = continuedFraction.getRepeatedContinuedPart().iterator();
                }

                return continuedFractionIterator.next();
            }
        };

        return getConvergentsUsingContinuedFractionVals(continuedFraction.getWholePart(), continuedFractionIterator);
    }

    /**
     * Returns an Iterator because the series of convergents could be infinite. The Iterator will be return so long as
     * the continuedFractionIterator returns.
     */
    public Iterator<RationalFraction> getConvergentsUsingContinuedFractionVals(int wholePart,
            Iterator<Integer> continuedFractionIterator) {

        return new Iterator<>() {
            private BigInteger prevNumerator = BigInteger.ONE; // Satisfies the equation.
            private BigInteger prevDenominator = BigInteger.ZERO; // Satisfies the equation.
            private BigInteger numerator = BigInteger.valueOf(wholePart);
            private BigInteger denominator = BigInteger.ONE;

            @Override
            public boolean hasNext() {
                return numerator != null;
            }

            @Override
            public RationalFraction next() {
                RationalFraction returnVal = new RationalFraction(numerator, denominator);

                if (!continuedFractionIterator.hasNext()) {
                    this.numerator = null;
                    this.denominator = null;
                    return returnVal;
                }

                BigInteger newPrevNumerator = numerator;
                BigInteger newPrevDenominator = denominator;

                BigInteger continuedFractionVal = BigInteger.valueOf(continuedFractionIterator.next());

                this.numerator = continuedFractionVal.multiply(numerator).add(prevNumerator);
                this.denominator = continuedFractionVal.multiply(denominator).add(prevDenominator);
                this.prevNumerator = newPrevNumerator;
                this.prevDenominator = newPrevDenominator;

                return returnVal;
            }
        };
    }

    @Value
    public static class RationalFraction {
        private final BigInteger numerator;
        private final BigInteger denominator;
    }

    @Value
    public static class ContinuedFraction {
        private final int wholePart;
        private final List<Integer> repeatedContinuedPart;
    }

    /**
     * A CustomFraction when computed is (sqrt(numeratorSquareRoot) + numeratorAddNum) / denominatorNum;
     */
    @Value
    @AllArgsConstructor
    private static class CustomFraction {
        private final long numeratorSquareRoot;
        private final long numeratorAddNum;
        private final long denominatorNum;

        /**
         * Sort of our base. We start with just the square root number. The other numbers are filled in so our
         * computation = sqrt(numeratorSquareRoot).
         */
        public CustomFraction(long numeratorSquareRoot) {
            this.numeratorSquareRoot = numeratorSquareRoot;
            this.numeratorAddNum = 0;
            this.denominatorNum = 1;
        }

        /**
         * Subtracting in our CustomFraction is just subtracting the (denominator * val) from the numerator.
         */
        public CustomFraction subtract(long val) {
            long newNumeratorAddNum = getNumeratorAddNum() - (denominatorNum * val);
            return new CustomFraction(getNumeratorSquareRoot(), newNumeratorAddNum, getDenominatorNum());
        }

        /**
         * Returns 1 / (existing fraction) where the square root stays in the numerator in order to keep the equation
         * simple. Normally, the reciprocal is just swapping the numerator and denominator. To put the square root back
         * in the numerator, we multiply the numerator and denominator by the conjugate of the denominator.
         *
         * For the denominator the square root irrational is eliminated, and we're left with
         * sqrt(numeratorSquareRoot)^2 - numeratorAddNum^2 (we always subtract since we multiplied a positive and a
         * negative).
         *
         * The numerator is just denominatorNum * (sqrt(numeratorSquareRoot) - numeratorAddNum). There's an interesting
         * pattern here though. The value in the new denominator is always divisible by the old denominatorNum (the new
         * multiplier in the numerator). This means we can divide out the old denominatar from the new ond and ignore
         * the multiplier in the numerator.
         */
        public CustomFraction reciprocal() {
            long newDenominatorNum = (getNumeratorSquareRoot() - (getNumeratorAddNum() * getNumeratorAddNum()))
                    / getDenominatorNum();

            // Square root val stays the same. We just flip the sign of our addNum.
            long newNumeratorAddNum = -1 * getNumeratorAddNum();
            return new CustomFraction(getNumeratorSquareRoot(), newNumeratorAddNum, newDenominatorNum);
        }

        /**
         * Returns the integer portion of the computed fraction.
         *
         * This actually calculates the fraction value, using only 3 calculations so there is unlikely to be too many
         * double rounding issues.
         */
        public int getIntValue() {
            return (int) ((Math.sqrt(getNumeratorSquareRoot()) + getNumeratorAddNum()) / getDenominatorNum());
        }
    }
}
