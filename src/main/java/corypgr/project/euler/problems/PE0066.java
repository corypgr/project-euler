package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.ContinuedFractionUtil;
import corypgr.project.euler.problems.util.ContinuedFractionUtil.RationalFraction;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Problem 66
 *
 * https://projecteuler.net/problem=66
 *
 * Doesn't look that hard. It looks like for each D value, we can check each x starting at 0 to see if any y satisfies
 * the equation. The check for a valid y will be calculating if sqrt((1 - x^2) / D) is an integer. I tried this, and
 * the numbers grow far too large to effectively check using this naive method. I tried flipping it around, testing
 * y values until a valid x is found. That was a bit faster but it still took ~5 minutes to get through d values up to
 * 300.
 *
 * After a little bit of research, I found out our equation is a special form of Diophantine equations called Pell's
 * equation: https://en.wikipedia.org/wiki/Pell%27s_equation There's an optimized way to solve this using the
 * convergents of the continued fraction for the square root of a number! Problems 64, 65, and 66 are all linked! The
 * solution to Pell's equation is one of the convergents (numerator / denominator) where x = numerator and y = denominator.
 *
 * I created a utility for generalizing the continued fraction and convergent generation, and swapped that in here to
 * come up with the current solution.
 */
public class PE0066 implements Problem {
    private static final int MAX_D = 1000;

    @Override
    public ProblemSolution solve() {
        ContinuedFractionUtil continuedFractionUtil = new ContinuedFractionUtil();

        BigInteger largestMinimalX = BigInteger.ZERO;
        int bestD = 1;
        for (int d = 1; d <= MAX_D; d++) {
            BigInteger minimalX = getMinimalX(d, continuedFractionUtil);
            if (minimalX != null && minimalX.compareTo(largestMinimalX) > 0) {
                largestMinimalX = minimalX;
                bestD = d;
            }
        }

        return ProblemSolution.builder()
                .solution(bestD)
                .descriptiveSolution("Largest minimal x value for Pell's equations with D <= 1000: " + bestD)
                .build();
    }

    private BigInteger getMinimalX(int d, ContinuedFractionUtil continuedFractionUtil) {
        // Skip square d values.
        int dSqrt = (int) Math.sqrt(d);
        if (dSqrt * dSqrt == d) {
            return null;
        }

        BigInteger dAsBigInt = BigInteger.valueOf(d);

        Iterator<RationalFraction> convergentIterator = continuedFractionUtil.getConvergentsOfSquareRoot(d);
        return Stream.generate(convergentIterator::next)
                .filter(fraction -> solvesPellsEquation(fraction.getNumerator(), fraction.getDenominator(), dAsBigInt))
                .map(RationalFraction::getNumerator)
                .findFirst()
                .get();
    }

    /**
     * Checks if the lhs of x^2 - d * y^2 == 1.
     */
    private boolean solvesPellsEquation(BigInteger x, BigInteger y, BigInteger d) {
        BigInteger xSquared = x.multiply(x);
        BigInteger ySquared = y.multiply(y);
        return xSquared.subtract(d.multiply(ySquared)).equals(BigInteger.ONE);
    }
}
