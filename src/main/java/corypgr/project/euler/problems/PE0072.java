package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Problem 72
 *
 * https://projecteuler.net/problem=72
 *
 * First idea here would be to simply track all double values we've already seen for each fraction, then return the
 * count at the end. This would be a fairly large number, and I'm guessing we'll run into some weird java arithmetic
 * issues.
 *
 * Instead, I think I've found a pattern. Define q(d) as the number of unique n / d combinations for d, where the
 * combination would not be simplified. Looking at the denominators up to 9, I see that:
 *  * If d is prime, q(d) = d - 1.
 *  * If d is divisible by a,b, and c, q(d) = d - 1 - q(a) - q(b) - q(c).
 *
 * We can determine the divisors of our d value, and use previous calculations to determine the number of new
 * combinations to add for d.
 */
public class PE0072 implements Problem {
    private static final int MAX_D = 1_000_000;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet(MAX_D);

        DivisorsUtil divisorsUtil = new DivisorsUtil();

        Map<Long, Long> dToNewUniqueVals = new HashMap<>();
        dToNewUniqueVals.put(1L, 0L); // 1 is present in all divisor sets.

        for (long d = 2; d <= MAX_D; d++) {
            //System.out.println(d);
            if (primes.contains(d)) {
                dToNewUniqueVals.put(d, d - 1);
            } else {
                Set<Long> divisors = divisorsUtil.getProperDivisors(d);
                long newUnique = d - 1;
                for (long divisor : divisors) {
                    newUnique -= dToNewUniqueVals.get(divisor);
                }
                dToNewUniqueVals.put(d, newUnique);
            }
        }

        long uniqueCount = dToNewUniqueVals.values().stream()
                .mapToLong(Long::longValue)
                .sum();
        return ProblemSolution.builder()
                .solution(uniqueCount)
                .descriptiveSolution("Number of reduced proper fractions for d <= 1,000,000: " + uniqueCount)
                .build();
    }
}
