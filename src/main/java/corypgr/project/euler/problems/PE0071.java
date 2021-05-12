package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 71
 *
 * https://projecteuler.net/problem=71
 *
 * This is interesting. Clearly we don't actually need to list out all of the different fractions. We can do some math
 * to get a good range of values to check and just look at the division result of the fractions to determine what is
 * the closest "immediately left" value of 3/7.
 *
 * For each d, we'll do:
 *  * d / 7 = q (the number of times 7 divides into d).
 *  * (q * 3) - 1 = n lower bound for that d.
 *  * Increment n until n / d > 3 / 7, tracking the largest n / d value under 3/7 for all d.
 */
public class PE0071 implements Problem {
    private static final int MAX_D = 1_000_000;
    private static final int N_TARGET = 3;
    private static final int D_TARGET = 7;
    private static final double FRACTION_TARGET = N_TARGET / (double) D_TARGET;

    @Override
    public ProblemSolution solve() {
        double bestFraction = 0.0;
        int bestN = 0;
        for (int d = 1; d <= MAX_D; d++) {
            int dTargetInD = d / D_TARGET;

            int n = (N_TARGET * dTargetInD) - 1;
            double nDividedByD = n / (double) d;
            while(nDividedByD < FRACTION_TARGET) {
                if (nDividedByD > bestFraction) {
                    bestFraction = nDividedByD;
                    bestN = n;
                }

                n++;
                nDividedByD = n / (double) d;
            }
        }

        return ProblemSolution.builder()
                .solution(bestN)
                .descriptiveSolution("Numerator of the fraction immediately left of 3/7: " + bestN)
                .build();
    }
}
