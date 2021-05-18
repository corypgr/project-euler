package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Problem 77
 *
 * https://projecteuler.net/problem=77
 *
 * Similar again to Problems 31 and 76. We can model this a bit closer to Problem 31 this time, since we're only dealing
 * with prime numbers. This time around, the max "denomination" can count by itself.
 *
 * It's tempting to split off something that computes these types of combinations, but this one is different. Here, we
 * don't know the end solution, so we're just computing until we hit 5,000 different ways.
 */
public class PE0077 implements Problem {
    private static final int TARGET_COMBINATIONS = 5000;
    // Sort of guessing on this one.
    private static final long MAX_PRIME = 100;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Integer> primes = primeGenerator.generatePrimesList(MAX_PRIME).stream()
                .map(Long::intValue)
                .collect(Collectors.toList());
        int lastPrime = primes.get(primes.size() - 1);
        Map<Integer, Integer> zeroAmountMap = primes.stream()
                .collect(Collectors.toMap(Function.identity(), v -> 1));

        // Top map key is the amount you're trying to find combinations for.
        // Inner map key is the prime values to the number of combinations for that prime value.
        // The largest key maps to the total combinations in all cases.
        Map<Integer, Map<Integer, Integer>> amountToNumCombinationsByPrime = new HashMap<>();
        amountToNumCombinationsByPrime.put(0, zeroAmountMap);

        int i;
        for (i = 1; amountToNumCombinationsByPrime.get(i - 1).get(lastPrime) < TARGET_COMBINATIONS && i < lastPrime; i++) {
            Map<Integer, Integer> numCombinationsByPrime = getNumCombinationsByPrime(
                    amountToNumCombinationsByPrime, i, primes);
            amountToNumCombinationsByPrime.put(i, numCombinationsByPrime);
        }

        int solution = i - 1; // Subtract 1 since we would have ended after incrementing past the target.
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("First val with more than 5000 prime number summations: " + solution)
                .build();
    }

    private Map<Integer, Integer> getNumCombinationsByPrime(Map<Integer, Map<Integer, Integer>> amountToNumCombinationsByPrime,
                                                               int amount, List<Integer> primes) {
        int numWays = 0;
        Map<Integer, Integer> primeToNumCombinations = new HashMap<>();
        for (int prime : primes) {
            if (prime <= amount) {
                int remaining = amount - prime;
                numWays += amountToNumCombinationsByPrime.get(remaining).get(prime);
            }

            // Still add the number of combinations for primes larger than amount. This gives us an easy lookup
            // for small amounts when we're looking at larger primes.
            primeToNumCombinations.put(prime, numWays);
        }
        return primeToNumCombinations;
    }
}
