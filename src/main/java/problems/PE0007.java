package problems;

import prime.PrimeGenerator;
import util.Problem;
import util.ProblemSolution;

import java.util.List;

/**
 * Problem 7
 *
 * https://projecteuler.net/problem=7
 *
 * This is a little interesting. I already have something to generate prime numbers, but it relies on knowing the max
 * prime number to generate the list. I can't think of a great way to modify that algorithm to give us a specific prime
 * number count. Instead, let's do some exponential increase until we hit the size we're looking for.
 */
public class PE0007 implements Problem {
    // Position is 10001 - 1 to account for zero indexing.
    private static final int PRIME_POSITION = 10000;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();

        // Start at something reasonable compared to the size we're looking for.
        // Every second and every third value is not prime, so try 3X to start.
        long maxVal = PRIME_POSITION * 3;
        List<Long> primes = primeGenerator.generatePrimesList(maxVal);
        while (primes.size() < PRIME_POSITION) {
            maxVal *= 2;
            primes = primeGenerator.generatePrimesList(maxVal);
        }

        return ProblemSolution.builder()
                .solution(primes.get(PRIME_POSITION))
                .descriptiveSolution("10001st prime: " + primes.get(PRIME_POSITION))
                .build();
    }
}
