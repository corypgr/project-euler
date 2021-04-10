package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Problem 53
 *
 * https://projecteuler.net/problem=53
 *
 * So, we have a formula. We can use it to calculate the number of combinations. The trick here is that the formula,
 * when run directly, is pretty expensive. 100! is a huge number after all. There are several shortcuts we can take to
 * make this easier on ourselves:
 *  * We don't need to calculate the factorial directly. We can determine all of the values which will be multiplied
 *    together in both the numerator and denominator, and cancel out the shared values. Then we just multiply what is
 *    left.
 *  * We don't need to look at all combinations of r and n. (n + 1 choose r) > (n choose r). This is intuitively
 *    true, but we can also prove it. (n choose r) = (n!)/(r!(n-r)!). (n + 1 choose r) = (n+1!)/(r!(n+1-r)!).
 *    Dividing the former out of the latter, we see that (n + 1 choose r) = (n choose r) * (n+1)/(n+1-r) where r <= n.
 *    With this in mind, when we find a combination of (n choose r) > 1 million, we can just count the rest of the the
 *    n values up to 100.
 *  * Similarly, we don't need to look at all r values. Let's look closer at the denominator of the equation. If r = a,
 *    then the denominator is a!(n - a)!. If r = n - a, then the denominator is (n - a)!(n - (n - a))! = (n - a)!a!.
 *    They're equal! The problem here is that this assumes a static n. I'm not sure how I can use this info effectively,
 *    so I'm leaving it off.
 */
public class PE0053 implements Problem {
    private static final int MAX_VAL = 100;
    private static final long TARGET_VAL = 1_000_000;
    @Override
    public ProblemSolution solve() {
        int count = IntStream.rangeClosed(1, MAX_VAL)
                .map(this::getCountOfCombinationsOverTargetFor)
                .sum();

        return ProblemSolution.builder()
                .solution(count)
                .descriptiveSolution("Count of combinations over 1 million: " + count)
                .build();
    }

    private int getCountOfCombinationsOverTargetFor(int r) {
        for (int n = r; n <= MAX_VAL; n++) {
            long combinationCount = calculateCombinationCount(n, r);
            if (combinationCount > TARGET_VAL) {
                return MAX_VAL - n + 1;
            }
        }
        return 0;
    }

    private long calculateCombinationCount(int n, int r) {
        int nMinusR = n - r;
        int maxDenominatorProduct = Math.max(r, nMinusR);
        int minDenominatorProduct= Math.min(r, nMinusR);

        // Still use BigInteger in case the numbers get really big.
        BigInteger numerator = IntStream.rangeClosed(maxDenominatorProduct + 1, n)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);
        BigInteger denominator = IntStream.rangeClosed(1, minDenominatorProduct)
                .mapToObj(BigInteger::valueOf)
                .reduce(BigInteger.ONE, BigInteger::multiply);
        return numerator.divide(denominator).longValueExact();
    }
}
