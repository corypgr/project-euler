package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Problem 73
 *
 * https://projecteuler.net/problem=73
 *
 * An interesting twist building off of problems 71 and 72. Since we're looking for a narrow range, we can't use the
 * totient function information this time around. In this case our range is much smaller though. With d <= 12,000 we
 * can come up with a quick test of whether something is relatively prime to another number.
 *
 * My plan is to find the prime divisors of all numbers up to 12,000 and store them in a map. Then, for each d:
 *  * Find the starting numerator of the 1/3 to 1/2 range for that d. (d / 3 + 1).
 *  * Find the ending numerator of the 1/3 to 1/2 range for that d. (d / 2, then subtract 1 if exactly 1/2).
 *  * For each numerator n in range, check if the intersection of primeDivisors(d) and primeDivisors(n) is empty.
 *    * If empty, add 1 to our count.
 *    * If not empty, then the fraction could be reduced, and is covered by another fraction already counted. Skip.
 */
public class PE0073 implements Problem {
    private static final long MAX_D = 12_000;
    private static final int LOWER_RANGE_DIVISOR = 3;
    private static final int UPPER_RANGE_DIVISOR = 2;

    @Override
    public ProblemSolution solve() {
        Map<Long, List<Long>> valToPrimeDivisors = getPrimeDivisorsMap();

        long count = LongStream.rangeClosed(2, MAX_D) // start at d = 2 since there are no fractions with d = 1
                .mapToObj(this::getFractionRange)
                .mapToLong(range -> getRelativelyPrimeCountInRange(range, valToPrimeDivisors))
                .sum();

        return ProblemSolution.builder()
                .solution(count)
                .descriptiveSolution("Number of reduced fractions between 1/3 and 1/2: " + count)
                .build();
    }

    private Map<Long, List<Long>> getPrimeDivisorsMap() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList((long) Math.sqrt(MAX_D));
        DivisorsUtil divisorsUtil = new DivisorsUtil();

        return LongStream.rangeClosed(1, MAX_D)
                .boxed()
                .collect(Collectors.toMap(Function.identity(), d -> getSortedPrimeDivisors(d, divisorsUtil, primes)));
    }

    private List<Long> getSortedPrimeDivisors(long d, DivisorsUtil divisorsUtil, List<Long> primes) {
        return divisorsUtil.getPrimeDivisors(d, primes).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private Range getFractionRange(long d) {
        long startN = d / LOWER_RANGE_DIVISOR + 1;
        long endN = d / UPPER_RANGE_DIVISOR;
        if (endN / (double) d == 0.5) {
            endN--;
        }

        return Range.builder()
                .d(d)
                .startN(startN)
                .endD(endN)
                .build();
    }

    private long getRelativelyPrimeCountInRange(Range range, Map<Long, List<Long>> valToPrimeDivisors) {
        List<Long> dPrimeDivisors = valToPrimeDivisors.get(range.getD());

        return LongStream.rangeClosed(range.getStartN(), range.getEndD())
                .filter(n -> isIntersectionEmpty(dPrimeDivisors, valToPrimeDivisors.get(n)))
                .count();
    }

    /**
     * Lists are sorted, so we can iterate through until we find a match or run out of elements.
     */
    private boolean isIntersectionEmpty(List<Long> a, List<Long> b) {
        int aIndex = 0;
        int bIndex = 0;
        while (aIndex < a.size() && bIndex < b.size()) {
            long aVal = a.get(aIndex);
            long bVal = b.get(bIndex);

            if (aVal == bVal) {
                return false;
            } else if (aVal < bVal) {
                aIndex++;
            } else {
                bIndex++;
            }
        }

        // no matches found.
        return true;
    }

    @Value
    @Builder
    private static final class Range {
        private final long d;
        private final long startN;
        private final long endD;
    }
}
