package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Problem 37
 *
 * https://projecteuler.net/problem=37
 *
 * On it's face this doesn't seem to be that difficult. For a set of prime numbers we can just check if all of their
 * truncations are prime. The problem is that I don't know what the max prime number should be to initiate my search. We
 * do know that there are only 11 such primes, and we know that 3797 is one of those numbers. 3797 is a fairly small
 * prime number, but it is a start at a maximum. We can start at something reasonably large, say 100 * 3797 as our max
 * prime and do some exponential increase if we need to increase from there.
 *
 * This brings up a question of whether it would be a good idea to write another prime number generation method. That one
 * wouldn't be as efficient as our current one where we set a max prime number, but it would allow us to do a simple
 * iteration to get the "next" prime number. For this problem it probably isn't needed, but will revisit in the future.
 */
public class PE0037 implements Problem {
    private static final long STARTING_MAX_PRIME = 3797 * 100;
    private static final int NUM_EXPECTED_RESULTS = 11;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();

        long maxVal = STARTING_MAX_PRIME;
        List<Long> truncatablePrimes = new LinkedList<>();
        do {
            Set<Long> primes = primeGenerator.generatePrimesSet(maxVal);
            truncatablePrimes = getTruncatablePrimes(primes, truncatablePrimes);
            maxVal *= 2;
        } while (truncatablePrimes.size() < NUM_EXPECTED_RESULTS);

        long sum = truncatablePrimes.stream()
                .mapToLong(Long::longValue)
                .sum();

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of the truncatable primes: " + sum)
                .build();
    }

    private List<Long> getTruncatablePrimes(Set<Long> primes, List<Long> truncatablePrimesSoFar) {
        long maxSoFar = truncatablePrimesSoFar.isEmpty() ? 0L :
                truncatablePrimesSoFar.get(truncatablePrimesSoFar.size() - 1);
        primes.stream()
                .dropWhile(prime -> prime <= maxSoFar)
                .filter(prime -> isTruncatablePrime(primes, prime))
                .forEach(truncatablePrimesSoFar::add);

        return truncatablePrimesSoFar;
    }

    private boolean isTruncatablePrime(Set<Long> primes, long prime) {
        // Small primes are not "truncatable"
        if (prime < 10) {
            return false;
        }

        // Right to left truncation
        for (long primeTruncatedOnTheRight = prime / 10; primeTruncatedOnTheRight > 0; primeTruncatedOnTheRight /= 10) {
            if (!primes.contains(primeTruncatedOnTheRight)) {
                return false;
            }
        }

        // Left to right truncation
        // The first digit is some prime % (multiplier of 10)
        long firstDigitMultiplier = (long) Math.pow(10, String.valueOf(prime).length() - 1);
        for (long primeTruncatedOnTheLeft = prime % firstDigitMultiplier; primeTruncatedOnTheLeft > 0;
                firstDigitMultiplier /= 10, primeTruncatedOnTheLeft %= firstDigitMultiplier) {
            if (!primes.contains(primeTruncatedOnTheLeft)) {
                return false;
            }
        }
        return true;
    }
}
