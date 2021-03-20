package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.List;

/**
 * Problem 10
 *
 * https://projecteuler.net/problem=10
 *
 * With our PrimeGenerator this problem is pretty easy.
 */
public class PE0010 implements Problem {
    private static final long MAX_PRIME = 2_000_000;

    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();

        List<Long> primes = primeGenerator.generatePrimesList(MAX_PRIME);
        long sum = primes.stream()
                .mapToLong(Long::longValue)
                .sum();

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Prime Sum: " + sum)
                .build();
    }
}
