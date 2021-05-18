package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Problem 31
 *
 * https://projecteuler.net/problem=31
 *
 * Looks a bit like a dynamic programming problem to me. We can work up from 1, determining all the ways there are to
 * make 1p, then 2p by referencing the 1p answer, and so on. I tried to do this, but it's a bit more complicated than
 * your average dynamic solution. If the ordering of the coins mattered, then the dynamic programming solution would
 * work just fine, but without it I was double counting certain combinations like (1p, 2p) and (2p, 1p). I wasn't able
 * to resolve this issue.
 *
 * Falling back to my other idea is using combinations again. We can't generate all combinations though. That would be
 * (9 choose 200 with repeats) where 0p is included with the 8 coins, which is 75,824,205,888,366 combinations. Instead,
 * I think we can customize our combination logic from CombinationUtil a bit to the problem at hand. Here we don't need
 * to retrieve the actual combinations; we just need the count of the combinations that add up to 200. We also don't
 * need to process all combinations. If a combination is > 200, you can skip over the rest of the incrementing for the
 * digit you're working on. We'll see if these optimizations gives us the speed we want here. This solution runs in about
 * 6 seconds, which is pretty slow...
 *
 * I came back to this and figured out the dynamic programming approach. The trick there is that when you subtract off a
 * coin from the target 200 and look at the previously calculated number of combinations for the remaining amount, you
 * only look at the combinations made by using the same coin or smaller coin values. Below I keep a Map for every coin
 * value to the number of combinations for every amount. The new solution cleans things up really well and executes in
 * less than 100 ms.
 */
public class PE0031 implements Problem {
    private static final int[] COINS = { 1, 2, 5, 10, 20, 50, 100, 200};
    private static final int TARGET_AMOUNT = 200;
    private static final Map<Integer, Integer> ZERO_AMOUNT_MAP = Arrays.stream(COINS)
            .boxed()
            .collect(Collectors.toMap(Function.identity(), v -> 1));

    @Override
    public ProblemSolution solve() {
        // Top map key is the amount you're trying to find combinations for.
        // Inner map key is the coin values to the number of combinations for that coin value.
        // The 200 COIN key maps to the total combinations in all cases.
        Map<Integer, Map<Integer, Integer>> amountToNumCombinationsByCoinType = new HashMap<>();
        amountToNumCombinationsByCoinType.put(0, ZERO_AMOUNT_MAP);

        for (int i = 1; i <= TARGET_AMOUNT; i++) {
            Map<Integer, Integer> numCombinationsByCoinType = getNumCombinationsByCoinType(
                    amountToNumCombinationsByCoinType, i);
            amountToNumCombinationsByCoinType.put(i, numCombinationsByCoinType);
        }

        int solution = amountToNumCombinationsByCoinType.get(TARGET_AMOUNT).get(200);
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Number of coin combinations to get to 200: " + solution)
                .build();
    }

    private Map<Integer, Integer> getNumCombinationsByCoinType(Map<Integer, Map<Integer, Integer>> amountToNumCombinationsByCoinType,
                                                               int amount) {
        int numWays = 0;
        Map<Integer, Integer> coinToNumCombinations = new HashMap<>();
        for (int coin : COINS) {
            if (coin <= amount) {
                int remaining = amount - coin;
                numWays += amountToNumCombinationsByCoinType.get(remaining).get(coin);
            }

            // Still add the number of combinations for coins larger than amount. This gives us an easy lookup
            // for small amounts when we're looking at larger coins.
            coinToNumCombinations.put(coin, numWays);
        }
        return coinToNumCombinations;
    }

}
