package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Problem 50
 *
 * https://projecteuler.net/problem=50
 *
 * At first, I thought this looked pretty straightforward. Generate the primes up to 1 million, then cycle through each
 * checking if the sum of the first n primes adds up to that number. Not super efficient, but it would run pretty
 * quickly. That actually doesn't work. The problem doesn't say that the consecutive primes start with the first prime.
 * This is reinforced by the 953 example. The first 21 primes adds to 712, so the proper sequence of primes to get to
 * 953 is the primes from 7 - 89 (calculated by hand).
 *
 * What's the next thing we can do? We can generate all consecutive sums of prime numbers and check if they're prime.
 * This can be done a few ways, but I think a reasonably efficient solution would be to do a sort of sliding window
 * approach, similar to our SlidingWindowProduct class. It's easier with sums though. We can start with the largest
 * possible sequence length and work backwards, ending as soon as we find a consecutive sum that is prime.
 */
public class PE0050 implements Problem {
    private static final long MAX_PRIME = 1_000_000;

    @Override
    public ProblemSolution solve() {
        long solution = getPrimeWithLongestSequenceSum();

        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Prime number under 1 million which can be represented as the sum of the longest" +
                        " sequence of prime numbers: " + solution)
                .build();
    }

    private long getPrimeWithLongestSequenceSum() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList(MAX_PRIME);
        Set<Long> primesAsSet = new HashSet<>(primes);

        for (int length = getMaxPossibleSequenceLength(primes); length > 0; length--) {
            // Get initial sum of first n elements
            long sum = primes.stream()
                    .limit(length)
                    .mapToLong(Long::longValue)
                    .sum();

            // Sliding window summation of the remaining elements.
            // Return when any sum is a prime number.
            for (int i = length; i < primes.size() && sum < MAX_PRIME; i++) {
                sum += primes.get(i);
                sum -= primes.get(i - length);

                if (primesAsSet.contains(sum)) {
                    return sum;
                }
            }
        }

        throw new IllegalStateException("Should have found a prime with some consecutive primes.");
    }

    /**
     * The max length is the number of primes, starting at the beginning, which adds up to larger than our MAX_PRIME
     * minus 1. Since the primes are in increasing order, the length cannot be any larger without going over MAX_PRIME.
     */
    private int getMaxPossibleSequenceLength(List<Long> primes) {
        int length = 0;
        int sum = 0;
        while (sum < MAX_PRIME) {
            sum += primes.get(length);
            length++;
        }
        return length - 1;
    }
}
