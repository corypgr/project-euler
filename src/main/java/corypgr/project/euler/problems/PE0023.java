package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Problem 23
 *
 * https://projecteuler.net/problem=23
 *
 * Seems that we only really care about the abundant numbers here. I think we can do something similar to the sieve of
 * eratosphenes here. We can generate all abundant numbers up to the limit provided (the hard way... by finding all
 * proper divisors). Then using those abundant numbers generate all numbers which are the sum of 2 abundant numbers up
 * to the same limit. Remove those summed values from the set of all numbers, giving us the values that can't be summed,
 * and sum those values.
 *
 * Can use the same prime number trick as we did in problem 21 as well to simplify divisor generation.
 */
public class PE0023 implements Problem {
    private static final int MAX_NON_SUMMED_ABUNDANT_NUMBER = 28123;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primesUnderMax = primeGenerator.generatePrimesSet(MAX_NON_SUMMED_ABUNDANT_NUMBER);

        DivisorsUtil divisorsUtil = new DivisorsUtil();
        List<Long> abundantNumbers = LongStream.range(1, MAX_NON_SUMMED_ABUNDANT_NUMBER)
                .filter(num -> isAbundantNumber(num, primesUnderMax, divisorsUtil))
                .mapToObj(num -> num)
                .collect(Collectors.toList());

        // using HashSet explicitly so we know what we're removing from later. Fill with all numbers to start.
        Set<Long> nonSummedAbundantNumbers = new HashSet<>();
        LongStream.range(1, MAX_NON_SUMMED_ABUNDANT_NUMBER)
                .forEach(nonSummedAbundantNumbers::add);

        // Find sums the lazy way. Remove them all, leaving only the values we care about.
        for (int i = 0; i < abundantNumbers.size(); i++) {
            for (int j = i; j < abundantNumbers.size(); j++) {
                nonSummedAbundantNumbers.remove(abundantNumbers.get(i) + abundantNumbers.get(j));
            }
        }

        long sumOfNonSummedAbundantNumbers = nonSummedAbundantNumbers.stream()
                .mapToLong(Long::longValue)
                .sum();

        return ProblemSolution.builder()
                .solution(sumOfNonSummedAbundantNumbers)
                .descriptiveSolution("Sum of numbers which cannot be written as the sum of two abundant numbers: " +
                        sumOfNonSummedAbundantNumbers)
                .build();
    }

    private boolean isAbundantNumber(long num, Set<Long> primes, DivisorsUtil divisorUtil) {
        if (primes.contains(num)) {
            return false; // prime numbers only have 1 and itself as divisors. 1 isn't prime, so no prime numbers are abundant.
        }

        long sumOfDivisors = divisorUtil.getProperDivisors(num).stream()
                .mapToLong(Long::longValue)
                .sum();
        return sumOfDivisors > num;
    }
}
