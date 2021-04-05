package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Arrays;

/**
 * Problem 40
 *
 * https://projecteuler.net/problem=40
 *
 * At first glance this looks a little too easy. We can just concatenate all integers into a string until we hit 1000000
 * characters. THen pull out the digits we care about and multiply them together.
 *
 * This works just fine.
 */
public class PE0040 implements Problem {
    private static final int[] POSITIONS = { 1, 10, 100, 1000, 10000, 100000, 1000000 };

    @Override
    public ProblemSolution solve() {
        StringBuilder concatenatedBuilder = new StringBuilder();
        int maxPosition = POSITIONS[POSITIONS.length - 1];
        for (int i = 1; concatenatedBuilder.length() < maxPosition; i++) {
            concatenatedBuilder.append(i);
        }

        String concatenated = concatenatedBuilder.toString();
        int productOfDigits = Arrays.stream(POSITIONS)
                .map(num -> num - 1) // zero indexed array. Adjust positions to match.
                .map(concatenated::charAt)
                .map(Character::getNumericValue)
                .reduce(1, (v1, v2) -> v1 * v2);

        return ProblemSolution.builder()
                .solution(productOfDigits)
                .descriptiveSolution("Product of fractional digits: " + productOfDigits)
                .build();
    }
}
