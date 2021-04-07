package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.PermutationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.List;

/**
 * Problem 43
 *
 * https://projecteuler.net/problem=43
 *
 * Seems kind of interesting. My first thought is to generate all 0 to 9 pandigital numbers using our PermutationUtil.
 * Then go through each checking the condition this is asking about.
 *
 * This runs a little slow, but I think most of it is due to how long it takes to generate the permutations. I added some
 * small optimizations, like the digitsToNumMap lookup table, but it still takes around 2 seconds to run.
 */
public class PE0043 implements Problem {
    private static final List<Integer> DIGITS = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

    // Listed in order of least likely to be divisible. Adjusted to be zero-indexed.
    // Every other number is divisible by 2. But only every 17th number is divisible by 17.
    private static final List<DigitDivisionInfo> DIGIT_DIVISION_INFOS = List.of(
            new DigitDivisionInfo(7, 8, 9, 17),
            new DigitDivisionInfo(6, 7, 8, 13),
            new DigitDivisionInfo(5, 6, 7, 11),
            new DigitDivisionInfo(4, 5, 6, 7),
            new DigitDivisionInfo(3, 4, 5, 5),
            new DigitDivisionInfo(2, 3, 4, 3),
            new DigitDivisionInfo(1, 2, 3, 2));

    @Override
    public ProblemSolution solve() {
        PermutationUtil<Integer> permutationUtil = new PermutationUtil<>();
        List<List<Integer>> permutations = permutationUtil.getAllPermutations(DIGITS);

        int[][][] digitsToNumMap = getDigitsToNumMap();

        long sumOfMatchingValues = permutations.stream()
                .filter(permutation -> permutation.get(0) != 0) // Zero start isn't a pandigital number
                .filter(permutation -> matchesCondition(permutation, digitsToNumMap))
                .mapToLong(this::listToLong)
                .sum();

        return ProblemSolution.builder()
                .solution(sumOfMatchingValues)
                .descriptiveSolution("Sum of pandigital numbers matching divisible condition: " + sumOfMatchingValues)
                .build();
    }

    private boolean matchesCondition(List<Integer> permutation, int[][][] digitsToNumMap) {
        for (DigitDivisionInfo divisionInfo : DIGIT_DIVISION_INFOS) {
            int a = permutation.get(divisionInfo.getAIndex());
            int b = permutation.get(divisionInfo.getBIndex());
            int c = permutation.get(divisionInfo.getCIndex());
            long digitSequenceVal = digitsToNumMap[a][b][c];
            if (digitSequenceVal % divisionInfo.getDivisor() != 0) {
                return false;
            }
        }
        return true;
    }

    private long listToLong(List<Integer> permutation) {
        long result = 0;
        int digitShifter = 10;
        for (int digit : permutation) {
            result *= digitShifter;
            result += digit;
        }
        return result;
    }

    /**
     * Generate all of these sequences up front. We can references them instead of calculating them over and over again.
     * Only 1000 values.
     */
    private int[][][] getDigitsToNumMap() {
        int[][][] digitsToNumMap = new int[10][10][10];
        for (int a = 0; a < 10; a++) {
            int aValToAdd = a * 100;
            for (int b = 0; b < 10; b++) {
                int bValToAdd = b * 10;
                int num = aValToAdd + bValToAdd;
                for (int c = 0; c < 10; c++, num++) {
                    digitsToNumMap[a][b][c] = num;
                }
            }
        }
        return digitsToNumMap;
    }

    @Value
    private static class DigitDivisionInfo {
        private final int aIndex;
        private final int bIndex;
        private final int cIndex;
        private final int divisor;
    }
}
