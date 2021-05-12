package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Problem 69
 *
 * https://projecteuler.net/problem=69
 *
 * Interesting. My first thought at a solution is to generate all Prime factors for all numbers under 1 million. Then
 * determining if a number is relatively prime to another number is simply taking the intersection between the two sets
 * of factors. If the intersection's size > 0, we know they are not relatively prime to each other.
 *
 * That solution is much too slow. Rethinking this, what we're really trying to do is minimize phi(n) and maximize n. We
 * get a small phi(n) when there are many proper prime divisors, and we see the smallest phi(n) when those divisors are
 * small. Take for example a number which has 2 and 3 as prime divisors. Every 2nd and 3rd numbers are not relatively
 * prime to our number.
 *
 * With this in mind, I rewrite the solution to look for the n with the largest number of proper prime divisors. When
 * there are ties, check for the divisors with the smaller values. This logic gives us the best answer quickly. There's
 * an unanswered question here though. If two n's had the same prime divisor sets, would a larger n be better? That isn't
 * clear since the phi(n) result will change roughly proportional to the size of n. Luckily, we didn't need to worry
 * about that here.
 *
 * -----
 * After seeing Problem 70 I did a little research on efficiently computing the Totient function. It can be computed
 * using n * (the product of all prime divisors in the formula (1 - 1/p)). See https://en.wikipedia.org/wiki/Euler%27s_totient_function
 * Given this formula, we can see n/phi(n) == m/phi(m) when n and m have the same prime divisors. So, size of n doesn't
 * actually matter.
 */
public class PE0069 implements Problem {
    private static final int MAX_N = 1_000_000;

    @Override
    public ProblemSolution solve() {
        // Only need primes up to sqrt(MAX_N) because DivisorsUtil only looks that far when determining prime factors.
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList((long) Math.sqrt(MAX_N));
        DivisorsUtil divisorsUtil = new DivisorsUtil();

        Set<Long> bestPrimeDivisorSet = Collections.emptySet();
        int bestN = 0;
        for (int n = 1; n <= MAX_N; n++) {
            Set<Long> properPrimeDivisors = divisorsUtil.getProperPrimeDivisors(n, primes);
            if (isNewPhiResultBetter(bestPrimeDivisorSet, properPrimeDivisors)) {
                bestPrimeDivisorSet = properPrimeDivisors;
                bestN = n;
            }
        }

        return ProblemSolution.builder()
                .solution(bestN)
                .descriptiveSolution("N value under 1 million which maximizes n/phi(n): " + bestN)
                .build();
    }

    private boolean isNewPhiResultBetter(Set<Long> oldPrimeDivisors, Set<Long> newPrimeDivisors) {
        if (oldPrimeDivisors.size() != newPrimeDivisors.size()) {
            return newPrimeDivisors.size() > oldPrimeDivisors.size();
        }

        // Check if there are smaller values in the new divisors set.
        Iterator<Long> oldDivisorsIter = oldPrimeDivisors.stream().sorted().iterator();
        Iterator<Long> newDivisorsIter = newPrimeDivisors.stream().sorted().iterator();
        while (oldDivisorsIter.hasNext() && newDivisorsIter.hasNext()) {
            long oldDivisor = oldDivisorsIter.next();
            long newDivisor = newDivisorsIter.next();

            if (oldDivisor != newDivisor) {
                return newDivisor < oldDivisor;
            }
        }

        // Divisors are equal. n/phi(n) is equal between the two.
        return false;
    }
}
