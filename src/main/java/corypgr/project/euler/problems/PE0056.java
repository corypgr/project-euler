package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;

/**
 * Problem 56
 *
 * https://projecteuler.net/problem=56
 *
 * For all combinations of a, b < 100 compute the exponent, then add up all digits. Track the max as we go.
 *
 * Will make heavy use of BigInteger to do the exponents.
 */
public class PE0056 implements Problem {
    private static final int MAX_ITERATION = 100;
    private static final BigInteger MAX_ITERATION_BIG_INT = BigInteger.valueOf(MAX_ITERATION);

    @Override
    public ProblemSolution solve() {
        int bestSumOfDigits = 0;
        for (BigInteger a = BigInteger.ONE; a.compareTo(MAX_ITERATION_BIG_INT) < 0; a = a.add(BigInteger.ONE)) {
            for (int b = 1; b < MAX_ITERATION; b++) {
                BigInteger exponent = a.pow(b);
                int sumOfDigits = getSumOfDigits(exponent);
                if (sumOfDigits > bestSumOfDigits) {
                    bestSumOfDigits = sumOfDigits;
                }
            }
        }

        return ProblemSolution.builder()
                .solution(bestSumOfDigits)
                .descriptiveSolution("Largest sum of digits of exponents: " + bestSumOfDigits)
                .build();
    }

    private int getSumOfDigits(BigInteger val) {
        return val.toString().chars()
                .map(Character::getNumericValue)
                .sum();
    }
}
