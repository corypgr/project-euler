package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.PermutationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Problem 41
 *
 * https://projecteuler.net/problem=41
 *
 * Hmm, the solution isn't hard to write here. Generate all primes <= 987654321, the max pandigital number. Then check
 * all of them in reverse to find the first one that is pandigital.
 *
 * I expect this is going to be very slow though... Generating prime numbers isn't exactly a fast operation. Finding
 * them up through 1 billion is going to take a while.
 *
 * Cheating a little bit here. 987654321 and 87654321 both resulted in OutOfMemory errors :). When running with 7654321
 * as the max we get the result we're looking for. Still takes 5-6 seconds to run everything, but I can't get that
 * shorter without figuring out a more efficient way to generate the large prime numbers.
 *
 * --------
 * Read through some of the solutions in the Project Euler thread on this. Found some interesting ideas to speed it up.
 * First, we can see that 8+7+6+5+4+3+2+1 = 36. This is divisible by 3, so we know that the answer isn't 8 digits long.
 * Similarly with 9 digits, the sum is 45, also divisible by 3.
 *
 * Since we're trying to find the largest pandigital prime, we can calculate things in a different order. We can go back
 * from 7654321, and for every pandigital number, check if it is prime. We can generate all permutations of the 7 digits
 * and sort them to get all pandigital numbers. To check if the number is prime, we check if it is divisible by any of
 * the primes under sqrt(7654321L), which we can generate up front.
 *
 * This gets the runtime under 100 ms.
 */
public class PE0041 implements Problem {
    private static final List<Integer> DIGITS = List.of(1, 2, 3, 4, 5, 6, 7);
    private static final long MAX_PANDIGITAL = 7654321L;

    @Override
    public ProblemSolution solve() {
        PermutationUtil<Integer> permutationUtil = new PermutationUtil<>();
        List<List<Integer>> permutations = permutationUtil.getAllPermutations(DIGITS);
        List<Long> pandigitalsSorted = permutations.stream()
                .map(this::permutationToLong)
                .sorted()
                .collect(Collectors.toList());

        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet((long) Math.sqrt(MAX_PANDIGITAL));

        long largestPandigitalPrime = -1L;
        for (int i = pandigitalsSorted.size() - 1; largestPandigitalPrime < 0; i--) {
            if (isPrime(pandigitalsSorted.get(i), primes)) {
                largestPandigitalPrime = pandigitalsSorted.get(i);
            }
        }

        return ProblemSolution.builder()
                .solution(largestPandigitalPrime)
                .descriptiveSolution("Largest pandigital prime number: " + largestPandigitalPrime)
                .build();
    }

    private long permutationToLong(List<Integer> permutation) {
        long result = 0;
        for (int i = permutation.size() - 1; i >= 0; i--) {
            result *= 10;
            result += permutation.get(i);
        }
        return result;
    }

    private boolean isPrime(long val, Set<Long> primes) {
        for (long prime : primes) {
            if (val % prime == 0) {
                return false;
            }
        }
        return true;
    }
}
