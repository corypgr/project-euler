package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Map;
import java.util.stream.IntStream;

/**
 * Problem 31
 *
 * https://projecteuler.net/problem=31
 *
 * Looks a bit like a dynamic programming problem to me. We can work up from 1, determining all the ways there are to
 * make 1p, then 2p by referencing the 1p answer, and so on. I tried to do this, but it's a bit more compicated than
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
 */
public class PE0031 implements Problem {
    private static final int[] COINS = { 0, 1, 2, 5, 10, 20, 50, 100, 200};
    private static final int[] COIN_VAL_TO_INDEX;
    static {
        COIN_VAL_TO_INDEX = new int[201];
        COIN_VAL_TO_INDEX[0] = 0;
        COIN_VAL_TO_INDEX[1] = 1;
        COIN_VAL_TO_INDEX[2] = 2;
        COIN_VAL_TO_INDEX[5] = 3;
        COIN_VAL_TO_INDEX[10] = 4;
        COIN_VAL_TO_INDEX[20] = 5;
        COIN_VAL_TO_INDEX[50] = 6;
        COIN_VAL_TO_INDEX[100] = 7;
        COIN_VAL_TO_INDEX[200] = 8;
    }
    private static final int MAX_COIN_COUNT = 200;
    private static final int TARGET_AMOUNT = 200;
    private static final int LAST_INDEX = 199;

    @Override
    public ProblemSolution solve() {
        int[] currentIndices = new int[MAX_COIN_COUNT];
        currentIndices[LAST_INDEX] = COINS.length - 1; // Seed with first valid state adding to 200 to simplify
                                                               // initial computations.

        int countOfTargetCombinations = 0;
        while (currentIndices[0] == 0) { // When the first index flips to 1, that is the last valid combination.
            countOfTargetCombinations++;
            currentIndices = getNextTargetCombination(currentIndices);
        }
        countOfTargetCombinations++; // Account for last valid combination not being counted.

        return ProblemSolution.builder()
                .solution(countOfTargetCombinations)
                .descriptiveSolution("Number of coin combinations to get to 200: " + countOfTargetCombinations)
                .build();
    }

    private int[] getNextTargetCombination(int[] indices) {
        // Start by finding max index, backtrack to index before that (k), then set all indexes from k - n to k + 1.
        int maxValIndex = indices[LAST_INDEX];
        boolean flipComplete = false;
        for (int i = LAST_INDEX - 1; i >= 0 && !flipComplete; i--) {
            if (indices[i] < maxValIndex) {
                int newValToFill = indices[i] + 1;
                if (COINS[newValToFill] * (LAST_INDEX - i) <= TARGET_AMOUNT) {
                    for (int j = i; j < indices.length; j++) {
                        indices[j] = newValToFill;
                    }
                    flipComplete = true;
                }
            }
        }

        // Check validity of the resulting values.
        int curAmount = IntStream.of(indices)
                .map(i -> COINS[i])
                .sum();
        if (curAmount == TARGET_AMOUNT) {
            return indices;
        } else if (curAmount > TARGET_AMOUNT) {
            // We went over the target. Repeat above to roll over to the next value again.
            return getNextTargetCombination(indices);
        }

        int lastIndexCoin = COINS[indices[LAST_INDEX]];
        curAmount -= lastIndexCoin;
        int nextCoinNeeded = TARGET_AMOUNT - curAmount;
        if (COIN_VAL_TO_INDEX[nextCoinNeeded] > 0) {
            indices[LAST_INDEX] = COIN_VAL_TO_INDEX[nextCoinNeeded];
            return indices;
        } else {
            indices[LAST_INDEX]++;
            return getNextTargetCombination(indices);
        }
    }


}
