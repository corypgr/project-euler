package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;

/**
 * Problem 57
 *
 * https://projecteuler.net/problem=57
 *
 * This looks like a real mess. Calculating all of the fractions looks complicated. Luckily, there's a pattern. Both
 * the numerator and denominator can be calculated separately with f(n) = 2 * f(n - 1) + f(n - 2). We can compute this
 * directly from the previous value.
 *
 * Need to use BigInteger again. The expansions grow very quickly.
 */
public class PE0057 implements Problem {
    @Override
    public ProblemSolution solve() {
        int countOfNumeratorLongerThanDenominator = 0;

        BigInteger prevNumerator = BigInteger.valueOf(3);
        BigInteger numerator = BigInteger.valueOf(7);
        BigInteger prevDenominator = BigInteger.valueOf(2);
        BigInteger denominator = BigInteger.valueOf(5);
        for (int i = 3; i <= 1000; i++) {
            BigInteger nextNumerator = numerator.multiply(BigInteger.TWO).add(prevNumerator);
            BigInteger nextDenominator = denominator.multiply(BigInteger.TWO).add(prevDenominator);
            if (numDigits(nextNumerator) > numDigits(nextDenominator)) {
                countOfNumeratorLongerThanDenominator++;
            }

            prevNumerator = numerator;
            numerator = nextNumerator;
            prevDenominator = denominator;
            denominator = nextDenominator;
        }

        return ProblemSolution.builder()
                .solution(countOfNumeratorLongerThanDenominator)
                .descriptiveSolution("Number of first 1000 expansions where the numerator is longer than the" +
                        " denominator: " + countOfNumeratorLongerThanDenominator)
                .build();
    }

    private int numDigits(BigInteger num) {
        return num.toString().length();
    }
}
