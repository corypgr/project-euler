package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Problem 87
 *
 * https://projecteuler.net/problem=87
 *
 * Seems pretty straightforward. We can generate all primes up to sqrt(50 million), then generate all squares, cubes,
 * and fourth powers of each of those primes up to 50 million. Then we're simply testing the combinations of the sums
 * of those values to see if they're under 50 million.
 *
 * We'll need to keep track of what values we've already calculated so that we do not double count anything. I thought
 * this may result in too many numbers to keep in a normal HashSet, but I didn't run into any problems.
 */
public class PE0087 implements Problem {
    private static final long MAX_SUM = 50_000_000;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList((long) Math.sqrt(MAX_SUM));
        List<Long> squares = getPowerNumberList(primes, 2);
        List<Long> cubes = getPowerNumberList(primes, 3);
        List<Long> fourths = getPowerNumberList(primes, 4);

        Set<Long> sums = new HashSet<>();

        // Normally I'm not a fan of break statements in for loops. I can't think of a cleaner way to write this though.
        for (long square : squares) {
            for (long cube : cubes) {
                long squarePlusCube = square + cube;
                if (squarePlusCube >= MAX_SUM) {
                    break;
                }

                for (long fourth : fourths) {
                    long sum = fourth + squarePlusCube;
                    if (sum >= MAX_SUM) {
                        break;
                    }

                    sums.add(sum);
                }
            }
        }

        return ProblemSolution.builder()
                .solution(sums.size())
                .descriptiveSolution("Number of sums below 50 million: " + sums.size())
                .build();
    }

    private List<Long> getPowerNumberList(List<Long> primes, int power) {
        return primes.stream()
                .map(prime -> (long) Math.pow(prime, power))
                .takeWhile(num -> num < MAX_SUM)
                .collect(Collectors.toList());
    }
}
