package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.HashMap;
import java.util.Map;

/**
 * Problem 14
 *
 * https://projecteuler.net/problem=14
 *
 * Looks like a dynamic programming problem to me. Since we're getting a count, and the numbers jump all over the place,
 * I don't think we'll be able to pre-generate everything. I'll go with recursion and memoization.
 */
public class PE0014 implements Problem {
    private static final long MAX_START = 1_000_000L;
    @Override
    public ProblemSolution solve() {
        Map<Long, Long> numberToSequenceCount = new HashMap<>();
        numberToSequenceCount.put(1L,1L);

        long bestStartVal = 0L;
        long bestSequenceCount = 0L;
        for (long i = 1; i < MAX_START; i++) {
            long sequenceCount = getSequenceCount(i, numberToSequenceCount);
            if (sequenceCount > bestSequenceCount) {
                bestSequenceCount = sequenceCount;
                bestStartVal = i;
            }
        }

        return ProblemSolution.builder()
                .solution(bestStartVal)
                .descriptiveSolution("Best starting number: " + bestStartVal)
                .build();
    }

    private long getSequenceCount(long start, Map<Long, Long> numberToSequenceCount) {
        long memoizedCount = numberToSequenceCount.getOrDefault(start, -1L);
        if (memoizedCount > 0) {
            return memoizedCount;
        }

        if (start % 2 == 0) {
            long count = 1 + getSequenceCount(start / 2, numberToSequenceCount);
            numberToSequenceCount.put(start, count);
            return count;
        } else {
            long count = 1 + getSequenceCount((3 * start) + 1, numberToSequenceCount);
            numberToSequenceCount.put(start, count);
            return count;
        }
    }
}
