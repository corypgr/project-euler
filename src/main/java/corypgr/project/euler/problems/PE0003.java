package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Set;

/**
 * Problem 3
 *
 * https://projecteuler.net/problem=3
 *
 * So we need to find the Prime factors of this number, which means we need to know what numbers are Prime. After
 * knowing what numbers are prime, the rest is pretty straightforward.
 * ---------
 * At some point I wrote a general utility for finding prime divisors. Swapping that in here. My old solution here is
 * actually not quite correct. It only checks for prime numbers <= sqrt(TARGET). In the general case you can have a
 * prime divisor that is larger than sqrt(TARGET).
 */
public class PE0003 implements Problem {
    private static final long TARGET = 600851475143L;

    @Override
    public ProblemSolution solve() {
        DivisorsUtil divisorsUtil = new DivisorsUtil();
        Set<Long> primeDivisors = divisorsUtil.getPrimeDivisors(TARGET);
        long largestPrimeDivisor = primeDivisors.stream()
                .mapToLong(Long::longValue)
                .max()
                .getAsLong();

        return ProblemSolution.builder()
                .solution(largestPrimeDivisor)
                .descriptiveSolution("Largest Prime Factor: " + largestPrimeDivisor)
                .build();
    }
}
