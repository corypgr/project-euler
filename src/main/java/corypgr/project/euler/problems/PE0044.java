package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem 44
 *
 * https://projecteuler.net/problem=44
 *
 * Starting off, I don't see a clear way to reduce the problem. First thought is to generate all Pentagonal numbers up
 * to a point, then check all of their combinations of sums and differences to see what the pentagonal pair is.
 *
 * I'm not sure what a good max would be in this case. Since the sequence will grow exponentially, we can probably set
 * a fairly high number as the max and still execute quickly. Cheated a little here. I tried at 1 million and didn't
 * find anything. Tried again at 10 million and found the answer.
 */
public class PE0044 implements Problem {
    private static final int MAX_NUM = 10_000_000;

    @Override
    public ProblemSolution solve() {
        List<Integer> pentagonNumbers = Stream.iterate(1, i -> i + 1)
                .map(this::getPentagonalNumber)
                .takeWhile(number -> number < MAX_NUM)
                .collect(Collectors.toList());

        Pair bestPair = getPairWithSmallestDifference(pentagonNumbers);
        int bestDifference = bestPair.getB() - bestPair.getA();

        return ProblemSolution.builder()
                .solution(bestDifference)
                .descriptiveSolution("The pentagon pair whose sum and difference are pentagon numbers, and has the " +
                        "smallest difference is: " + bestPair + " with difference: " + bestDifference)
                .build();
    }

    private Pair getPairWithSmallestDifference(List<Integer> pentagonNumbers) {
        Set<Integer> pentagonNumbersSet = new HashSet<>(pentagonNumbers);

        int bestDifference = Integer.MAX_VALUE;
        Pair bestPair = null;

        // Only go up to half of the max for i. We can't check if sums are pentagon numbers beyond that point.
        int iValue = 0;
        int halfOfMax = MAX_NUM / 2;
        for (int i = 0; iValue <= halfOfMax; i++) {
            iValue = pentagonNumbers.get(i);

            // Only check sums up to the max since that is the largest we can check against.
            int lastSum = 0;
            for (int j = i + 1; j < pentagonNumbers.size() && lastSum < MAX_NUM; j++) {
                int jValue = pentagonNumbers.get(j);
                lastSum = iValue + jValue;
                int difference = jValue - iValue;
                if (pentagonNumbersSet.contains(lastSum) && pentagonNumbersSet.contains(difference) &&
                        difference < bestDifference) {
                    bestDifference = difference;
                    bestPair = new Pair(iValue, jValue);
                }
            }
        }
        return bestPair;
    }

    private int getPentagonalNumber(int index) {
        // Guaranteed to be a whole number, so integer division is fine.
        return (index * (3 * index - 1)) / 2;
    }

    @Value
    private static class Pair {
        private final int a;
        private final int b;
    }
}
