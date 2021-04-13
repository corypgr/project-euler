package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Iterator;
import java.util.Set;

/**
 * Problem 58
 *
 * https://projecteuler.net/problem=58
 *
 * This uses the same concept as problem 28. Though this time around I noticed a pattern for determining the values on
 * the diagonal. Starting with ring 0 (the center) the diagonals (or "corners") of a ring can be calculated by adding
 * ring * 2 to the previously calculated diagonal. Looking at the first 3 rings, we have:
 *  * ring 0 just has 1. Start there: 1
 *  * ring 1 will add 2 * 1 = 2 to the previous vals: 3, 5, 7, 9
 *  * ring 2 will add 2 * 2 = 4 to the previous vals: 13, 17, 21, 25
 *
 * Using this, we can calculate the corners of each ring until our prime number ratio drops below a certain point.
 *
 * Again, not too sure what a good max prime would be for our prime number generation. In this case, the numbers we're
 * dealing with are pretty sparse though. We can generate primes up to a point, then rely on regular division checks for
 * anything beyond that point (up to x * x).
 */
public class PE0058 implements Problem {
    private static final long MAX_PRIME_GENERATED = 100_000;
    private static final double PRIME_RATIO_TARGET = 0.1;
    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet(MAX_PRIME_GENERATED);

        // Start at ring 2, with other values already counted.
        // If we start at ring 1, with only 1 (the center) counted, that looks like 0% and the loop ends.
        int diagonalCount = 5; // 1, 3, 5, 7, 9
        int primeCount = 3; // 3, 5, 7

        long curNum = 9L;
        int ring = 1;
        while((double) primeCount / diagonalCount > PRIME_RATIO_TARGET) {
            ring++;

            int amountToAddForRing = ring * 2;
            for (int corner = 0; corner < 4; corner++) {
                curNum += amountToAddForRing;
                diagonalCount++;
                if (isPrime(curNum, primes)) {
                    primeCount++;
                }
            }
        }

        int sideLength = ring * 2 + 1;
        return ProblemSolution.builder()
                .solution(sideLength)
                .descriptiveSolution("Side length of the spiral with diagonals less than 10% prime: " + sideLength)
                .build();
    }

    private boolean isPrime(long num, Set<Long> generatedPrimes) {
        if (num <= MAX_PRIME_GENERATED) {
            return generatedPrimes.contains(num);
        }

        int maxToCheck = (int) Math.sqrt(num);
        Iterator<Long> primeIter = generatedPrimes.iterator();
        for (long prime = primeIter.next(); primeIter.hasNext() && prime <= maxToCheck; prime = primeIter.next()) {
            if (num % prime == 0) {
                return false;
            }
        }
        return true;
    }
}
