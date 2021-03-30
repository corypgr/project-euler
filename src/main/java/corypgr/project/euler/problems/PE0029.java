package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Problem 29
 *
 * https://projecteuler.net/problem=29
 *
 * I'm tempted to just use BigInteger again here, but there is a more interesting solution. For there to be a duplicate
 * term, then 'a' must be a perfect square, perfect cube, etc... From the example only 16 is duplicated:
 *   2^4 = 16. 4^2 = 16.
 * 4^2 is (2^2)^2 = 2^4. So, for a perfect square, cube, etc.. we can take the exponent and multiply it by the 'b' to get
 * the new exponent when using the "simplified term". We can collect all of these "simplified terms" in a Set and look at
 * the count.
 *
 * I tried to count the overlapping values and subtract it from the expected total terms (99 * 99), but I couldn't nail
 * down how to detect only 1 original value properly.
 */
public class PE0029 implements Problem {
    @Override
    public ProblemSolution solve() {
        Map<Integer, Term> valToSimplifiedTerm = new HashMap<>();
        // Generates simplified terms for all 'a' values. Over 10 it will just be Term(i, 1)
        for (int i = 2; i <= 100; i++) {
            int product = i;
            int pow = 1;

            while (product <= 100) {
                valToSimplifiedTerm.putIfAbsent(product, new Term(i, pow));

                product *= i;
                pow++;
            }
        }

        // Convert all 'a' values to simplified terms, determine the new Term using 'b', and save to the Set.
        Set<Term> distinctTerms = new HashSet<>();
        for (int a = 2; a <= 100; a++) {
            Term simplifiedA = valToSimplifiedTerm.get(a);
            for (int b = 2; b <= 100; b++) {
                distinctTerms.add(new Term(simplifiedA.getA(), simplifiedA.getB() * b));
            }
        }

        return ProblemSolution.builder()
                .solution(distinctTerms.size())
                .descriptiveSolution("Number of unique terms: " + distinctTerms.size())
                .build();
    }

    @Value
    private static class Term {
        private final int a;
        private final int b;
    }
}
