package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.List;
import java.util.Set;

/**
 * Problem 47
 *
 * https://projecteuler.net/problem=47
 *
 * This is interesting. First, we need a way to find prime factors. I'll update DivisorsUtil with something for that.
 * Then we can loop through numbers until we see 4 in a row with 4 prime divisors.
 *
 * The max prime number is unknown here... We probably don't need something huge, but we'll try a few different ones.
 */
public class PE0047 implements Problem {
    private static final long MAX_PRIME = 1000;
    private static final long MAX_NUM = MAX_PRIME * MAX_PRIME;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList(MAX_PRIME);
        DivisorsUtil divisorsUtil = new DivisorsUtil();

        int consecutive = 0;
        long num = 0;
        while (consecutive < 4 && num < MAX_NUM) {
            num++;

            Set<Long> primeDivisors = divisorsUtil.getProperPrimeDivisors(num, primes);
            if (primeDivisors.size() == 4) {
                consecutive++;
            } else {
                consecutive = 0;
            }
        }
        long firstConsecutiveNumber = num - 3;

        return ProblemSolution.builder()
                .solution(firstConsecutiveNumber)
                .descriptiveSolution("First of the consecutive numbers: " + firstConsecutiveNumber)
                .build();
    }
}
