package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.TotientFunctionUtil;

import java.util.List;
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
 *
 * Above worked, but is slow. Takes around 8-9 seconds to run. Reading the Euler forum thread for this, I realize that
 * we're seeing the same thing as Euler's Totient function again. q(d) = phi(d) because it is looking for the relatively
 * prime values of d. With this, I can optimize the solution significantly.
 */
public class PE0072 implements Problem {
    private static final int MAX_D = 1_000_000;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList((long) Math.sqrt(MAX_D));

        DivisorsUtil divisorsUtil = new DivisorsUtil();
        TotientFunctionUtil totientFunctionUtil = new TotientFunctionUtil();

        long uniqueCount = 0;
        for (long d = 2; d <= MAX_D; d++) {
            Set<Long> primeDivisors = divisorsUtil.getPrimeDivisors(d, primes);
            uniqueCount += totientFunctionUtil.calculateTotientFunction(d, primeDivisors);
        }

        return ProblemSolution.builder()
                .solution(uniqueCount)
                .descriptiveSolution("Number of reduced proper fractions for d <= 1,000,000: " + uniqueCount)
                .build();
    }
}
