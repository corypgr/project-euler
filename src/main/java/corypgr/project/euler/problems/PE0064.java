package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Problem 64
 *
 * https://projecteuler.net/problem=64
 *
 * This problem was pretty challenging. I can see why I skipped it on my first pass when working on these years ago. To
 * start, I had a hard time following the explanation of the series for this problem. I also don't see an obvious pattern
 * for determining the periods. Found some threads at https://math.stackexchange.com/questions/265690 and
 * https://math.stackexchange.com/questions/980470 that gave me ideas for calculating the period.
 *
 * While I didn't see a pattern for generating the continued fraction, I did see that the end of each continued fraction
 * is 2 * the integer (non-repeating) portion. That gave me a stopping point.
 *
 * Initially, I tried to calculate the double value using basic Java math. At each step subtract the int val and take
 * the reciprocal, storing into a double as you go. You pull out the int portion of the double to generate the list of
 * the repeating fraction. This works ok for smaller numbers, but when we get to long lists of repeating fractions the
 * java math errors stack up and our division starts to give incorrect results. I tried a number of things to get around
 * that, like using BigDecimal with long scales. The error margins didn't improve well enough.
 *
 * Next I thought about what we were doing. We're generating a "fraction" that has the square root and some other
 * numbers. If we hold off on calculating the square root until we need to determine the int portion, we can simplify
 * the fraction well enough to only have a few calculations. Much lower error bar. I looked around for a while to find
 * Java libraries that do this for me... and didn't really find one. There may be something, but the few things I did
 * find (like apache commons Math library) can do rational numbers (ints a,b in the form a / b) but they don't deal with
 * irrational numbers (our square roots) at all.
 *
 * I thought it would be a lot of work to write the fraction math logic myself, but after looking at the patterns that
 * our fractions take, it isn't that hard. I rolled my own solution specifically for this scenario. We subtract the int
 * val from the fraction and then take the reciprocol, only doing our "real" math when we need to pull the int val. This
 * works, and runs very quickly as well.
 */
public class PE0064 implements Problem {
    @Override
    public ProblemSolution solve() {
        long count = IntStream.rangeClosed(1, 10000)
                .mapToObj(this::getSquareRootAsContinuedFraction)
                .filter(v -> v.getContinuedPart().size() % 2 == 1)
                .count();

        return ProblemSolution.builder()
                .solution(count)
                .descriptiveSolution("Number of square roots with odd continued parts: " + count)
                .build();
    }

    private ContinuedFraction getSquareRootAsContinuedFraction(int n) {
        CustomFraction fraction = new CustomFraction(n);
        int wholePart = fraction.getIntValue();
        if (wholePart * wholePart == n) {
            // Not irrational
            return new ContinuedFraction(wholePart, Collections.emptyList());
        }

        List<Integer> continuedList = new ArrayList<>();
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

    @Value
    private static class ContinuedFraction {
        private final int wholePart;
        private final List<Integer> continuedPart;
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
