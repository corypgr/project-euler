package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem 46
 *
 * https://projecteuler.net/problem=46
 *
 * I didn't know what an "odd composite number" was. Turns out it is just a non-prime (composite) number that is odd.
 * This problem looks like it can be brute forced. Roughly:
 *  * Set some max value.
 *  * Generate all primes up to the max.
 *  * Generate all odd composites up to the max, by removing the primes from the odd numbers.
 *  * Generate all values which are twice a square up to the max, storing in a set.
 *  * Loop through the odd composites, with inner loops checking if num - (a prime number) == (twice a square).
 *
 *  No idea what the max should be, so we'll test a few different ones.
 */
public class PE0046 implements Problem {
    private static final int MAX_VALUE = 10_000;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet(MAX_VALUE);
        Set<Long> twiceASquares = getTwiceASquares();

        long solution = Stream.iterate(3L, v -> v < MAX_VALUE, v -> v + 2)
                .filter(Predicate.not(primes::contains)) // Leaves only odd composites.
                .filter(v -> !isSumOfAPrimeAndTwiceASquare(v, primes, twiceASquares))
                .findFirst()
                .get();

        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("First odd composite that isn't the sum of a prime and twice a square: " + solution)
                .build();
    }

    private Set<Long> getTwiceASquares() {
        return Stream.iterate(1L, v -> v + 1)
                .map(v -> v * v * 2)
                .takeWhile(v -> v < MAX_VALUE)
                .collect(Collectors.toSet());
    }

    private boolean isSumOfAPrimeAndTwiceASquare(long val, Set<Long> primes, Set<Long> twiceASquares) {
        return primes.stream()
                .takeWhile(prime -> prime < val)
                .anyMatch(prime -> twiceASquares.contains(val - prime));
    }
}
