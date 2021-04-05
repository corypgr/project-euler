package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Set;

/**
 * Problem 35
 *
 * https://projecteuler.net/problem=35
 *
 * Not too difficult. Generate all primes below one million, then for each of those numbers
 * check if all of their rotations are also prime. Count the number of cases.
 */
public class PE0035 implements Problem {
    private static final long MAX_PRIME = 1_000_000;
    private static final int[] SIZE_TO_FIRST_DIGIT_MULTIPLIERS;
    static {
        SIZE_TO_FIRST_DIGIT_MULTIPLIERS = new int[8];
        SIZE_TO_FIRST_DIGIT_MULTIPLIERS[0] = 0; // Won't be used.
        SIZE_TO_FIRST_DIGIT_MULTIPLIERS[1] = 1; // Won't be used.
        for (int i = 2; i < SIZE_TO_FIRST_DIGIT_MULTIPLIERS.length; i++) {
            SIZE_TO_FIRST_DIGIT_MULTIPLIERS[i] = SIZE_TO_FIRST_DIGIT_MULTIPLIERS[i - 1] * 10;
        }
    }

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet(MAX_PRIME);

        long countOfCircularPrimes = primes.stream()
                .filter(prime -> isCircularPrime(prime, primes))
                .count();

        return ProblemSolution.builder()
                .solution(countOfCircularPrimes)
                .descriptiveSolution("Number of circular primes below 1 million: " + countOfCircularPrimes)
                .build();
    }

    private boolean isCircularPrime(long originalPrime, Set<Long> primes) {
        int numLength = String.valueOf(originalPrime).length();
        if (numLength == 1) {
            // Only 1 way to represent the number.
            return true;
        }

        int firstDigitMultiplier = SIZE_TO_FIRST_DIGIT_MULTIPLIERS[numLength];

        long rotatedPrime = originalPrime;
        do {
            long firstDigit = rotatedPrime / firstDigitMultiplier;
            long primeWithoutFirstDigit = rotatedPrime % firstDigitMultiplier;
            rotatedPrime = (primeWithoutFirstDigit * 10) + firstDigit;
            if (!primes.contains(rotatedPrime)) {
                return false;
            }
        } while(rotatedPrime != originalPrime);

        return true;
    }
}
