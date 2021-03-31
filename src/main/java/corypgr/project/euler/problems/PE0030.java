package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.CombinationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Problem 30
 *
 * https://projecteuler.net/problem=30
 *
 * The first thing I think about when I see this problem is that I need to determine the upper bound of the number
 * space. 9^5 = 59049, so 5 digit numbers must be possible. What about 6 digits? 59049 * 6 = 354294. 6 digits is
 * possible, with 354294 as the absolute upper limit. 7? 59049 * 7 = 413343, which isn't 7 digits long so 7 doesn't
 * work. Logically, nothing over 7 will work either.
 *
 * Can I narrow down the 354294 limit even more? This number doesn't start with 9, so we can relax that a bit more:
 * (3^5) + ((9^5) * 5) = 243 + 295245 = 295488 as the new limit. Applying the same idea again, we can limit the first
 * digit to 2: (2^5) + ((9^5) * 5) = 32 + 295245 = 295277. We could probably narrow this a bit more, but we're not
 * gaining a lot at this point.
 *
 * So we have an upper limit, the naive thing to do would be to just run through all of the numbers from 10 (single
 * digits don't work) to the upper limit, calculating the 5th digit sums and comparing against the number to see if they
 * match. That will have a lot of duplicated work. Consider 11112 and 21111. They have the same digits, and thus will
 * have the same 5th digit sums. A better strategy would be to generate all combinations of digits, compute the 5th
 * digit sums for each, and check if that sum contains exactly the original digits.
 *
 * This strategy still seems like a lot of computation though... Checking the math, it isn't actually that many. Only
 * 5005 total combinations:
 * (r + n - 1)! / r!(n - 1)! = (6 + 10 - 1)! / 6!(10 - 1)! = 5005 which is a lot smaller than our upper limit.
 *
 * Our upper limit is somewhat less useful with this strategy. If we can generate the combinations in ascending order then
 * we can efficiently toss out some of the combinations. Namely, all combinations with only digits that are 3 or higher.
 * That should be (6 choose 6 with repeats) number of combinations = 462. 5005 - 462 = 4543 total combinations. A 1/10th
 * decrease in computation after we've determined all the combinations.
 */
public class PE0030 implements Problem {
    private static final List<Integer> DIGITS = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    private static final Integer MAX_NUM_DIGITS = 6;
    private static final Integer MAX_FIRST_DIGIT = 2;

    @Override
    public ProblemSolution solve() {
        CombinationUtil<Integer> combinationUtil = new CombinationUtil<>();
        List<List<Integer>> combinations = combinationUtil.getAllCombinationsWithRepeats(DIGITS, MAX_NUM_DIGITS);

        long sum = combinations.stream()
                .skip(10) // First ten combinations are 0 - 9. These don't result in "sums", so skip them.
                .filter(combination -> combination.get(0) <= MAX_FIRST_DIGIT) // If the first digit is > 2, then the
                                                                              // combination is larger than the upper limit.
                .map(this::getNumberIfIsSumOfFifthPowers)
                .filter(Objects::nonNull)
                .mapToLong(Long::longValue)
                .sum();

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of numbers that can be written as the sum of fifth powers of their " +
                        "digits: " + sum)
                .build();
    }

    /**
     * Computes the sum of the fifth powers of the digits. Then checks if that sum's digits are an anagram of the
     * combination itself. Returns the sum if it is, and null otherwise.
     */
    private Long getNumberIfIsSumOfFifthPowers(List<Integer> combination) {
        long sum = combination.stream()
                .mapToLong(v -> (long) Math.pow(v, 5))
                .sum();

        List<Integer> sumAsSortedListOfInts = String.valueOf(sum)
                .chars()
                .mapToObj(Character::getNumericValue)
                .sorted()
                .collect(Collectors.toList());

        // The sum we generate may not have as many digits as our combination. Pad with zeros in that case.
        // We cannot remove the zeros from the combination since a zero may appear somewhere in the sum.
        if (sumAsSortedListOfInts.size() < combination.size()) {
            int numZerosToInsert = combination.size() - sumAsSortedListOfInts.size();
            for (int i = 0; i < numZerosToInsert; i++) {
                sumAsSortedListOfInts.add(0, 0);
            }
        }

        // combination is already sorted.
        return sumAsSortedListOfInts.equals(combination) ? sum : null;
    }
}
