package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.List;

/**
 * Problem 3
 *
 * https://projecteuler.net/problem=3
 *
 * So we need to find the Prime factors of this number, which means we need to know what numbers are Prime. After
 * knowing what numbers are prime, the rest is pretty straightforward.
 */
public class PE0003 implements Problem {
    private static final long TARGET = 600851475143L;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();

        // Use the square root as the max value since any prime factor must be <= the square root.
        List<Long> primes = primeGenerator.generatePrimesList((long) Math.sqrt(TARGET));

        // Check in reverse order since we're looking for the largest value.
        for (int i = primes.size() - 1; i >= 0; i--) {
            long prime = primes.get(i);
            if (TARGET % prime == 0) {
                return ProblemSolution.builder()
                        .solution(prime)
                        .descriptiveSolution("Largest Prime Factor: " + prime)
                        .build();
            }
        }
        return ProblemSolution.builder()
                .solution(TARGET)
                .descriptiveSolution("No Other Prime factors found. " + TARGET + " must be prime.")
                .build();
    }
}
