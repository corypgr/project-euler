package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.CombinationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Problem 34
 *
 * https://projecteuler.net/problem=34
 *
 * Similar to problem 30. We can generate the different combinations of numbers, and check if their factorial sum equals
 * a permutation of the combination.
 *
 * The lower limit is 10, since the single digit numbers don't create sums. Finding an upper limit might be annoying
 * here. 9! = 362880, which is 6 digits long. If we consider a value of all 9s, 9! * 6 = 2177280, which is 7 digits long.
 * Bumping up to 7, 7 * 9! = 2540160, and 8 * 9! is still only 7 digits long, so we know that 2540160 is our start at a
 * max. Since the first digit can't be bigger than 2, we can recalculate using 2! + 6 * 9! = 2177282 as an upper limit.
 * We can probably do better, but this is a good place to start.
 *
 * As an optimization, we can calculate all of factorials up front and reference those when doing the sums. With that,
 * it actually won't be very expensive to check the various numbers combinations.
 */
public class PE0034 implements Problem {
    private static final List<Integer> DIGITS = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    private static final int[] NUM_TO_FACTORIAL;
    private static final String[] ZERO_NUM_TO_PADDING;
    static {
        NUM_TO_FACTORIAL = new int[DIGITS.size()];
        NUM_TO_FACTORIAL[0] = 1;
        for (int i = 1; i < DIGITS.size(); i++) {
            NUM_TO_FACTORIAL[i] = NUM_TO_FACTORIAL[i - 1] * i;
        }

        ZERO_NUM_TO_PADDING = new String[DIGITS.size()];
        ZERO_NUM_TO_PADDING[0] = "";
        for (int i = 1; i < DIGITS.size(); i++) {
            ZERO_NUM_TO_PADDING[i] = ZERO_NUM_TO_PADDING[i - 1] + "0";
        }
    }

    private static final Integer MAX_NUM_DIGITS = 7;
    private static final Integer MAX_FIRST_DIGIT = 2;

    @Override
    public ProblemSolution solve() {
        CombinationUtil<Integer> combinationUtil = new CombinationUtil<>();
        List<List<Integer>> combinations = combinationUtil.getAllCombinationsWithRepeats(DIGITS, MAX_NUM_DIGITS);

        int sum = combinations.stream()
                .skip(10) // First ten combinations are 0 - 9. These don't result in "sums", so skip them.
                .filter(combination -> combination.get(0) <= MAX_FIRST_DIGIT) // If the first digit is > 2, then the
                                                                              // combination is larger than the upper limit.
                .map(this::getNumberIfIsSumOfFactorials)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .sum();

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of numbers that can be written as the sum of the factorials of their " +
                        "digits: " + sum)
                .build();
    }

    private Integer getNumberIfIsSumOfFactorials(List<Integer> combination) {
        int sum = combination.stream()
            .mapToInt(digit -> NUM_TO_FACTORIAL[digit])
            .sum();

        String sumAsString = String.valueOf(sum);
        if (sumAsString.length() < combination.size()) {
            // Remove some number of zeros from the sum. 0! is 1, so subtract 1 for each extra zero.
            int zerosToRemove = combination.size() - sumAsString.length();
            sum -= zerosToRemove;
            String newSumAsString = String.valueOf(sum);

            // If the number was right on the edge of changing size adjust a second time...
            // We only need to adjust a max of 2 times since the size doesn't change that easily.
            if (newSumAsString.length() < sumAsString.length()) {
                zerosToRemove++;
                sum--;
                newSumAsString = String.valueOf(sum);
            }

            int numZerosInCombination = (int) combination.stream()
                    .filter(digit -> digit == 0)
                    .count();
            if (zerosToRemove > numZerosInCombination) {
                return null; // Sum is too small given the the small number of zeros. Don't need to do the rest.
            }

            sumAsString = newSumAsString;
        }

        // The sum we generate may not have as many digits as our combination. Pad with zeros in that case.
        // We cannot remove the zeros from the combination since a zero may appear somewhere in the sum.
        sumAsString = ZERO_NUM_TO_PADDING[combination.size() - sumAsString.length()] + sumAsString;

        List<Integer> sumAsSortedListOfInts = sumAsString
                .chars()
                .mapToObj(Character::getNumericValue)
                .sorted()
                .collect(Collectors.toList());

        // combination is already sorted.
        return sumAsSortedListOfInts.equals(combination) ? sum : null;
    }
}
