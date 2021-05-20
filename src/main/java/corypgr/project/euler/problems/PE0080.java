package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.stream.IntStream;

/**
 * Problem 80
 *
 * https://projecteuler.net/problem=80
 *
 * I'm guessing the challenge for this problem is in generating the square root of a number to 100 (or more) precision.
 * I'll try to use BigDecimal to do this the lazy way, and fall back on researching other techniques if that doesn't
 * work.
 *
 * BigDecimal works, though I stumbled due to the wording of the problem. It apparently means the first 100 digits of
 * the number including the integer portion.
 */
public class PE0080 implements Problem {
    private static final int MAX_NUM = 100;
    private static final int NUM_DIGITS = 100;
    // Setting to our desired precision plus some buffer so that rounding doesn't play a factor.
    private static final MathContext SQRT_CONTEXT = new MathContext(NUM_DIGITS + 5);

    @Override
    public ProblemSolution solve() {
        int sum = IntStream.rangeClosed(1, MAX_NUM)
                .mapToObj(this::sqrt)
                .filter(this::isIrrational)
                .mapToInt(this::sumOfFirstOneHundredDigits)
                .sum();

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of the first hundred digits of the square roots of the first one hundred " +
                        "natural numbers, where we only count irrational roots: " + sum)
                .build();
    }

    private BigDecimal sqrt(int val) {
        return BigDecimal.valueOf(val).sqrt(SQRT_CONTEXT);
    }

    private boolean isIrrational(BigDecimal val) {
        return val.toPlainString().contains("."); // No decimal point in a rational (integer) result.
    }

    private int sumOfFirstOneHundredDigits(BigDecimal val) {
        return val.toPlainString().chars()
                .filter(v -> v != '.')
                .limit(NUM_DIGITS)
                .map(Character::getNumericValue)
                .sum();
    }
}
