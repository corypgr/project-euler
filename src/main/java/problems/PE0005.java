package problems;

import prime.PrimeGenerator;
import util.CountMap;
import util.MinimalFactorSet;
import util.Problem;
import util.ProblemSolution;

import java.util.Map;
import java.util.Set;

/**
 * Problem 5
 *
 * https://projecteuler.net/problem=5
 *
 * This is an interesting problem. Thinking a little bit about the problem, we can get the answer by finding the prime
 * factors of each number from 1 to 20, taking a minimum set of those factors to produce any number from 1 to 20, then
 * multiply all the factors in the minimum set together. We can actually do this with a calculator:
 *
 * 1 = 1 (ignore)
 * 2 = 2
 * 3 = 3
 * 4 = 2 * 2
 * 5 = 5
 * 6 = 2 * 3
 * 7 = 7
 * 8 = 2 * 2 * 2
 * 9 = 3 * 3
 * 10 = 2 * 5
 * 11 = 11
 * 12 = 2 * 2 * 3
 * 13 = 13
 * 14 = 2 * 7
 * 15 = 3 * 5
 * 16 = 2 * 2 * 2 * 2
 * 17 = 17
 * 18 = 2 * 3 * 3
 * 19 = 19
 * 20 = 2 * 2 * 5
 *
 * minimum set = 2 * 2 * 2 * 2 * 3 * 3 * 5 * 7 * 11 * 13 * 17 * 19 = (answer)
 *
 * This works, but I didn't write any code :) I'll apply basically the same algorithm as code.
 */
public class PE0005 implements Problem {
    private static final Long MAX_VAL = 20L;

    @Override
    public ProblemSolution solve() {
        MinimalFactorSet minimalFactorUtil = new MinimalFactorSet();
        CountMap<Long> minimalFactorSet = minimalFactorUtil.getMinimalFactorSetForNumbersUpTo(MAX_VAL);

        long product = minimalFactorSet.entrySet().stream()
                .mapToLong(e -> (long) Math.pow(e.getKey(), e.getValue())) // shortcut for repeated multiplication
                .reduce(1, (a, b) -> a * b);

        return ProblemSolution.builder()
                .solution(product)
                .descriptiveSolution("Product: " + product)
                .build();
    }
}
