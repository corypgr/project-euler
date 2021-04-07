package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;
import java.util.stream.Stream;

/**
 * Problem 48
 *
 * https://projecteuler.net/problem=48
 *
 * Looks like another problem for BigInteger.
 */
public class PE0048 implements Problem {
    private static final long TEN_DIGIT_DIVISOR = 10_000_000_000L;

    @Override
    public ProblemSolution solve() {
        BigInteger seriesSum = Stream.iterate(1, v -> v <= 1000, v -> v + 1)
                .map(this::getSelfPower)
                .reduce(BigInteger.ZERO, BigInteger::add);

        BigInteger modValue = seriesSum.mod(BigInteger.valueOf(TEN_DIGIT_DIVISOR));
        long solution = modValue.longValueExact();

        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Last 10 digits of self power series sum: " + solution)
                .build();
    }

    private BigInteger getSelfPower(int num) {
        return BigInteger.valueOf(num).pow(num);
    }
}
