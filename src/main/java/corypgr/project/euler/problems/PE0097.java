package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 97
 *
 * https://projecteuler.net/problem=97
 *
 * For this it looks like I just need to calculate 28433 × 2^7830457 + 1. We could do the multiplication in a loop,
 * keeping only the last 10 digits (the relevant bits) during the multiplications. However, I think Java's BigInteger
 * can solve this one.
 * -----------
 * The above works, but it's pretty slow. It runs in around 4 seconds. I'll try my looping strategy outlined above and
 * see if that's a faster solution. It runs in less than 100 ms with this solution.
 */
public class PE0097 implements Problem {
    private static final long MOD_VAL = 10_000_000_000L;

    @Override
    public ProblemSolution solve() {
        long powerOf2 = 1;
        for (int i = 1; i <= 7830457; i++) {
            powerOf2 *= 2;
            powerOf2 %= MOD_VAL;
        }

        long solution = 28433 * powerOf2 + 1;
        solution %= MOD_VAL;

        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Last 10 digits of the non-Marsenne prime: " + solution)
                .build();
    }
}
