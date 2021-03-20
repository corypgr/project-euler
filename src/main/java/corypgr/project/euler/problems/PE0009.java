package corypgr.project.euler.problems;

import lombok.Value;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 9
 *
 * https://projecteuler.net/problem=9
 *
 * We can do a little math so that we're not checking too many values, but other than that this is a brute-force
 * strategy.
 */
public class PE0009 implements Problem {
    private static final long GOAL = 1000L;

    public ProblemSolution solve() {
        Triplet triplet = findTriplet();

        long product = triplet.getA() * triplet.getB() * triplet.getC();
        return ProblemSolution.builder()
                .solution(product)
                .descriptiveSolution("Triplet: " + triplet + " Product: " + product)
                .build();
    }

    private static Triplet findTriplet() {
        for (long a = 1; a < GOAL; a++) {
            for (long b = a + 1; b < GOAL; b++) {
                long c = GOAL - a - b;
                if ((a * a) + (b * b) == c * c) {
                    return new Triplet(a, b, c);
                }
            }
        }
        throw new IllegalStateException("No valid triplet found");
    }

    @Value
    private static class Triplet {
        private final long a;
        private final long b;
        private final long c;
    }
}
