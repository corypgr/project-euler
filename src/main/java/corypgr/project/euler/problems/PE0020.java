package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;

/**
 * Problem 20
 *
 * https://projecteuler.net/problem=20
 *
 * 100! is likely larger than can fit in an int or long, so we're probably going to want to use BigInteger again. The
 * Naive solution is to just calculate the factorial directly using BigInteger. I suspect multiplication is much slower
 * when using BigInteger, but when actually executing I see it runs very quickly. Went with the naive solution.
 */
public class PE0020 implements Problem {
    @Override
    public ProblemSolution solve() {
        BigInteger product = BigInteger.ONE;
        for (int i = 2; i <= 100; i++) {
            product = product.multiply(BigInteger.valueOf(i));
        }

        int sumOfDigits = product.toString().chars()
                .map(Character::getNumericValue)
                .sum();
        return ProblemSolution.builder()
                .solution(sumOfDigits)
                .descriptiveSolution("Sum of the digits in 100!: " + sumOfDigits)
                .build();
    }
}
