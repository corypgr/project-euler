package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Problem 76
 *
 * https://projecteuler.net/problem=76
 *
 * Seems pretty similar to Problem 31, except we don't have a small set of values any more. Looking back at that,
 * I think we can apply the same ideas here for a dynamic programming solution. The main difference is that we need at
 * least 2 positive numbers for each combination. That excludes using v == 100 all by itself, so we do not need to
 * calculate any combinations for val 100.
 * ----------
 * Note that this problem could also use the Recurrence relation used in Problem 78. However, I also like this solution
 * and both are blazing fast, so I don't see a strong reason to update it.
 */
public class PE0076 implements Problem {
    private static final int TARGET_AMOUNT = 100;
    private static final Map<Integer, Integer> ZERO_AMOUNT_MAP = IntStream.rangeClosed(1, TARGET_AMOUNT - 1)
            .boxed()
            .collect(Collectors.toMap(Function.identity(), v -> 1));

    @Override
    public ProblemSolution solve() {
        // Top map key is the amount you're trying to find combinations for.
        // Inner map key is the int values to the number of combinations for that int value.
        // The 100 int key maps to the total combinations in all cases.
        Map<Integer, Map<Integer, Integer>> amountToNumCombinationsByIntVal = new HashMap<>();
        amountToNumCombinationsByIntVal.put(0, ZERO_AMOUNT_MAP);

        for (int i = 1; i <= TARGET_AMOUNT; i++) {
            Map<Integer, Integer> numCombinationsByIntVal = getNumCombinationsByIntVal(
                    amountToNumCombinationsByIntVal, i);
            amountToNumCombinationsByIntVal.put(i, numCombinationsByIntVal);
        }

        // Unlike with the coins, we need at least 2 numbers in our solution. That makes 99 the largest value to use,
        // and the largest we calculated for.
        int solution = amountToNumCombinationsByIntVal.get(TARGET_AMOUNT).get(TARGET_AMOUNT - 1);
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Number of combinations to get to 100: " + solution)
                .build();
    }

    private Map<Integer, Integer> getNumCombinationsByIntVal(Map<Integer, Map<Integer, Integer>> amountToNumCombinationsByIntVal,
                                                               int amount) {
        int numWays = 0;
        Map<Integer, Integer> intValToNumCombinations = new HashMap<>();
        for (int i = 1; i < TARGET_AMOUNT; i++) {
            if (i <= amount) {
                int remaining = amount - i;
                numWays += amountToNumCombinationsByIntVal.get(remaining).get(i);
            }

            // Still add the number of combinations for vals larger than amount. This gives us an easy lookup
            // for small amounts when we're looking at larger coins.
            intValToNumCombinations.put(i, numWays);
        }
        return intValToNumCombinations;
    }
}
