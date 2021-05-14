package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.LongStream;

/**
 * Problem 74
 *
 * https://projecteuler.net/problem=74
 *
 * This is interesting. My first thought is we can write a sort of dynamic programming solution that memoizes how many
 * links are in a chain after the current point. The problem with that is the chain will loop, but we don't know where
 * exactly...
 *
 * Next idea, create linked lists for each chain, with a mapping from the value to the node in the chain. When we see a
 * specific value that we've already processed, we can simply drop into the chain we've already computed and walk the
 * nodes until we see repeats. This would at least save us recomputing the chain values.
 *
 * The above works, running in around 2-3 seconds. Reading the problem more closely, I see that I was incorrect in my
 * earlier musing. We do know when the chain loops. The problem statement tells us the only 3 non-self loops. Given
 * that information, and a check for self-loops, we do a recursive solution that memoizes the length of the chain. This
 * brings us well under a 1 second runtime.
 */
public class PE0074 implements Problem {
    private static final long MAX_NUM = 1_000_000;
    private static final long TARGET_CHAIN_LENGTH = 60;

    private static final long[] DIGIT_FACTORIALS;
    static {
        DIGIT_FACTORIALS = new long[10];
        DIGIT_FACTORIALS[0] = 1;
        for (int i = 1; i < 10; i++) {
            DIGIT_FACTORIALS[i] = DIGIT_FACTORIALS[i - 1] * i;
        }
    }

    @Override
    public ProblemSolution solve() {
        Map<Long, Integer> valToChainLength = new HashMap<>();
        // 169 -> 363601 -> 1454 -> 169 loop
        valToChainLength.put(169L, 3);
        valToChainLength.put(363601L, 3);
        valToChainLength.put(1454L, 3);
        // 871 -> 45361 -> 871 loop
        valToChainLength.put(871L, 2);
        valToChainLength.put(45361L, 2);
        // 872 -> 45362 -> 872 loop
        valToChainLength.put(872L, 2);
        valToChainLength.put(45362L, 2);

        long countOfChainsAtTargetLength = LongStream.range(1, MAX_NUM)
                .map(v -> getNonRepeatingLength(v, valToChainLength))
                .filter(v -> v == TARGET_CHAIN_LENGTH)
                .count();
        return ProblemSolution.builder()
                .solution(countOfChainsAtTargetLength)
                .descriptiveSolution("Number of chains of 60 length: " + countOfChainsAtTargetLength)
                .build();
    }

    private int getNonRepeatingLength(long value, Map<Long, Integer> valToChainLength) {
        int memoizedChainLength = valToChainLength.getOrDefault(value, -1);
        if (memoizedChainLength > -1) {
            return memoizedChainLength;
        }

        long nextValue = getNextValue(value);
        if (nextValue == value) {
            valToChainLength.put(value, 1);
            return 1;
        }

        int lengthResult = getNonRepeatingLength(nextValue, valToChainLength) + 1;
        valToChainLength.put(value, lengthResult);
        return lengthResult;
    }

    private long getNextValue(long value) {
        long result = 0;

        long remaining = value;
        while (remaining > 0) {
            result += DIGIT_FACTORIALS[(int) remaining % 10];
            remaining /= 10;
        }
        return result;
    }
}
