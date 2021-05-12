package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.CountMap;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.TotientFunctionUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem 70
 *
 * https://projecteuler.net/problem=70
 *
 * Seems similar to number 69, we're just looking for the opposite condition. Looking at examples of phi(n) results,
 * phi(n) < n in all cases. This means that we're trying to get as close to 1 as possible. If n == 1 were an option,
 * that would be the correct choice. To get as close to n as possible, we need to maximize phi(n) relative to n. The
 * largest phi(n) we'll see is when n is prime, where phi(n) = n - 1. However, the digits of n - 1 won't ever be a
 * permutation of n. Instead, we'll want to look at other cases where we can minimize the number of not relatively prime
 * values relative to n.
 *
 * First examples that come to mind are n values which are the square of a prime number (p). These numbers only have p
 * and 1 as a proper factor. The only values which are not relatively prime to n would be multiples of p, which there
 * aren't that many of. I tried these, and didn't come up with a solution. Same results for cubes and other powers
 * of prime numbers.
 *
 * I did a little research and found an efficient calculation: https://en.wikipedia.org/wiki/Euler%27s_totient_function
 * Rolling a solution that checks all of the n values under 10 million, it runs in ~30 seconds. It works, but is far too
 * slow for me.
 *
 * Going back to my old ideas, the next best thing to check after powers of a single prime is multiplication of 2 primes.
 * I'm cheating a little here, since I know what the prime numbers need to be by this point, but logically we know that
 * the prime numbers we're looking for have to be fairly large. If they're small, then phi(n) will be small. We can set
 * a range of primes from (sqrt(10,000,000) - 50%, sqrt(10,000,000) + 50%) to try to find our number, testing all
 * combinations of 2 of these. Since we're only looking at the products of 2 prime numbers, there can't be any other
 * prime divisors.
 */
public class PE0070 implements Problem {
    private static final int MAX_N = 10_000_000;
    private static final double DIFFERENCE_FROM_SQRT_MAX_N = .5;

    @Override
    public ProblemSolution solve() {
        TotientFunctionUtil totientFunctionUtil = new TotientFunctionUtil();
        List<Long> primes = getPrimesInRange();

        double smallestNDividedByPhi = Double.MAX_VALUE;
        long bestN = 0;
        for (int i = 0; i < primes.size(); i++) {
            long iPrimeDivisor = primes.get(i);

            boolean pastMaxN = false;
            for (int j = i; j < primes.size() && !pastMaxN; j++) {
                long jPrimeDivisor = primes.get(j);
                long n = iPrimeDivisor * jPrimeDivisor;

                Set<Long> primeDivisors = Stream.of(iPrimeDivisor, jPrimeDivisor).collect(Collectors.toSet());
                long phi = totientFunctionUtil.calculateTotientFunction(n, primeDivisors);
                double nDividedByPhi = n / (double) phi;

                if (n > MAX_N) {
                    pastMaxN = true;
                } else if (nDividedByPhi < smallestNDividedByPhi && areDigitsPermutations(n, phi)) {
                    smallestNDividedByPhi = nDividedByPhi;
                    bestN = n;
                }
            }
        }

        return ProblemSolution.builder()
                .solution(bestN)
                .descriptiveSolution("n value with phi(n) as a permutation of n and smallest n/phi(n) ratio: " + bestN)
                .build();
    }

    private List<Long> getPrimesInRange() {
        int sqrtOfMaxN = (int) Math.sqrt(MAX_N);
        int minPrime = (int) (sqrtOfMaxN * DIFFERENCE_FROM_SQRT_MAX_N);
        int maxPrime = sqrtOfMaxN + minPrime;

        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList(maxPrime);

        for (int i = 0; i < primes.size(); i++) {
            if (primes.get(i) >= minPrime) {
                return primes.subList(i, primes.size());
            }
        }

        throw new IllegalStateException("Didn't find any primes >= " + minPrime);
    }

    private boolean areDigitsPermutations(long a, long b) {
        return getCountMapFromLong(a).equals(getCountMapFromLong(b));
    }

    private CountMap<Integer> getCountMapFromLong(long val) {
        CountMap<Integer> map = new CountMap<>();
        long remaining = val;
        while (remaining > 0) {
            map.addCount((int) remaining % 10);
            remaining /= 10;
        }
        return map;
    }
}
