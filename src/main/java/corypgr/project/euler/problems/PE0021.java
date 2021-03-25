package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.DivisorsUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Problem 21
 *
 * https://projecteuler.net/problem=21
 *
 * Interesting problem. Another problem where we're trying to figure out what the divisors of a number are, though those
 * divisors don't have to be prime. We can do a similar solution as problem 12 for determining what the divisors are,
 * though here we actually care what the divisors are. Since we have something of an upper bound we can also generate
 * the prime numbers under that upper bound and use that as a quick test to see if a number has other proper divisors. A
 * minor optimization.
 */
public class PE0021 implements Problem {
    private static final int MAX_NUM = 10000;
    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primesUnderMax = primeGenerator.generatePrimesSet(MAX_NUM);

        DivisorsUtil divisorsUtil = new DivisorsUtil();
        Set<Long> amicableNumbers = new HashSet<>();

        for (long i = 1; i < MAX_NUM; i++) {
            if (!amicableNumbers.contains(i)) {  // Skip if we've already processed it.
                long divisorSum = getSumOfDivisors(i, primesUnderMax, divisorsUtil);
                if (divisorSum > i) { // If less than i, then we've already run that number and we know these aren't an
                                      // amicable pair. If it equals i, that means they aren't amicable by definition.
                    long pairedDivisorSum = getSumOfDivisors(divisorSum, primesUnderMax, divisorsUtil);
                    if (pairedDivisorSum == i) {
                        amicableNumbers.add(i);
                        amicableNumbers.add(divisorSum);
                    }
                }
            }
        }

        long sumUnderMax = amicableNumbers.stream()
                .mapToLong(Long::longValue)
                .filter(v -> v < MAX_NUM)
                .sum();
        return ProblemSolution.builder()
                .solution(sumUnderMax)
                .descriptiveSolution("Sum of amicable numbers under " + MAX_NUM + ": " + sumUnderMax)
                .build();
    }

    private long getSumOfDivisors(long num, Set<Long> primes, DivisorsUtil divisorUtil) {
        if (primes.contains(num)) {
            return 1; // prime numbers only have 2 divisors. And we skip the number itself in this sum.
        }

        return divisorUtil.getProperDivisors(num).stream()
                .mapToLong(Long::longValue)
                .sum();
    }
}
