package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Problem 63
 *
 * https://projecteuler.net/problem=63
 *
 * Seems pretty straightforward. We can just generate powers until the desired number of digits is hit, and count the
 * powers with that many digits.
 *
 * When do we stop? It should be safe to stop when the smallest value (2 here) to the nth power produces a number with
 * more digits than n. The problem with that strategy is that for 2, the number of digits is always <= n for 2 ^ n.
 * Looking at this another way, we know that 10 ^ n always has n + 1 digits. That makes 9 the largest possible base.
 * When 9 ^ n produces a values less than n, no larger n values should produce an n digit number. We'll stop when we
 * hit that point.
 */
public class PE0063 implements Problem {
    private static final BigInteger NINE = BigInteger.valueOf(9L);

    @Override
    public ProblemSolution solve() {
        int count = IntStream.iterate(1, i -> !hasHitMaxN(i), i -> i + 1)
                .map(this::getCountOfNDigitNumbersForNthPower)
                .sum();

        return ProblemSolution.builder()
                .solution(count)
                .descriptiveSolution("Number of N digit numbers that are also Nth powers: " + count)
                .build();
    }

    private boolean hasHitMaxN(int n) {
        return NINE.pow(n).toString().length() < n;
    }

    private int getCountOfNDigitNumbersForNthPower(int n) {
        return (int) Stream.iterate(BigInteger.ONE, v -> v.compareTo(NINE) <= 0, v -> v.add(BigInteger.ONE))
                .map(v -> v.pow(n))
                .filter(v -> v.toString().length() == n)
                .count();
    }
}
