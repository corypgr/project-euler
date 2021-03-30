package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Iterator;
import java.util.Set;

/**
 * Problem 27
 *
 * https://projecteuler.net/problem=27
 *
 * First thought is to brute force by checking all values of a and b. We can generate all primes up front though to
 * limit the number of computations there. To figure out what the max prime number would be we can choose the max values
 * of a, b, and n. The max of a and b is 1000, and the max of n would also be 1000 by the same logic used in the problem
 * description. So. 1000^2 + 1000*1000 + 1000 = 2 * (1000^2) + 1000 = 2001000.
 *
 * Note that I'm only considering the positive integer values as prime. This follows the usual number conventions.
 *
 * The brute force solution works, but it is a little slow. As an optimization, we can see that b must be prime in order
 * for the formula to spit out a prime when n == 0. When n = 0, the formula solution is b (0*0 + 0*a + b = b). If we
 * only look at primes for b, then we gain a few hundred milliseconds. Unfortunately, most of our time is spent
 * generating the prime numbers.
 */
public class PE0027 implements Problem {
    private static final int MAX_PRIME = (2 * (1000 * 1000)) + 1000;
    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet(MAX_PRIME);

        int largestN = 0;
        long bestA = -999;
        long bestB = -1000;
        for (long a = -999; a < 1000; a++) {
            Iterator<Long> primeIter = primes.iterator(); // We know this is in order
            for (long b = primeIter.next(); b <= 1000; b = primeIter.next()) {
                int n = 0;
                while (primes.contains(computeQuadratic(a, b, n))) {
                    n++;
                }

                if (n > largestN) {
                    largestN = n;
                    bestA = a;
                    bestB = b;
                }
            }
        }

        return ProblemSolution.builder()
                .solution(bestA * bestB)
                .descriptiveSolution("a: " + bestA + " and b: " + bestB + " produce primes up to n: " + largestN +
                        ". Product of a and b: " + bestA * bestB)
                .build();
    }

    private long computeQuadratic(long a, long b, int n) {
        return (n * n) + (a * n) + b;
    }
}
