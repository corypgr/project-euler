package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Builder;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Problem 32
 *
 * https://projecteuler.net/problem=32
 *
 * This doesn't seem too hard. Do multiplication on 2 axes (multiplicand and multiplier) until you have more than 9
 * digits total on either axes. Then check the numbers to see if they are 1-9. It is potentially a lot of multiplying
 * though, so I'll try to use simple addition where I can.
 *
 * Determining min and max values for our multiplicand and multiplier is interesting here. For all cases where the
 * multiplier is 1 we have multiplicand == product, so we know that the multiplier should start at least at 2. We're
 * targeting a multiplicand/multiplier/product digit number of 9, so with 2 as the min multiplier we know that the
 * multiplicand can be at most 4 digits in order for the product to be also 4 digits. The largest number * 2 < 10000 is
 * 4999, which is a start at a max value. It repeats values though. The smallest multiplicand that doesn't repeat values
 * would be 4987. We can take this a bit further by looking at the product. The absolute largest product would be 9876.
 * Divide that by 2 and we get 4938 as the max multiplicand. We can probably do better, but there isn't much point.
 * Finally the multiplicand and multiplier are effectively interchangeable, so these bounds apply to both.
 */
public class PE0032 implements Problem {
    private static final int MAX_MULTIPLICAND = 4938;
    private static final int MIN_MULTIPLICAND = 2;
    private static final MultiplicationTrioData STARTER_TRIO_DATA = MultiplicationTrioData.builder()
            .isPandigital(false)
            .isLongerThan9Digits(false)
            .build();

    @Override
    public ProblemSolution solve() {
        Set<Integer> pandigitalProducts = new HashSet<>();
        for (int multiplicand = MIN_MULTIPLICAND; multiplicand <= MAX_MULTIPLICAND; multiplicand++) {
            int product = multiplicand;
            MultiplicationTrioData lastData = STARTER_TRIO_DATA;

            for (int multiplier = MIN_MULTIPLICAND; multiplier <= MAX_MULTIPLICAND && !lastData.isLongerThan9Digits();
                    multiplier++) {
                product += multiplicand;
                lastData = analyzeMultiplicationTrio(multiplicand, multiplier, product);
                if (lastData.isPandigital()) {
                    pandigitalProducts.add(product);
                }
            }
        }

        int sumOfProducts = pandigitalProducts.stream()
                .mapToInt(Integer::intValue)
                .sum();
        return ProblemSolution.builder()
                .solution(sumOfProducts)
                .descriptiveSolution("Sum of Multiplication trio pandigital products: " + sumOfProducts)
                .build();
    }

    private MultiplicationTrioData analyzeMultiplicationTrio(int multiplicand, int multiplier, int product) {
        String numbersConcat = "" + multiplicand + multiplier + product;
        if (numbersConcat.length() > 9) {
            return MultiplicationTrioData.builder()
                    .isPandigital(false)
                    .isLongerThan9Digits(true)
                    .build();
        }

        Set<Integer> uniqueDigits = numbersConcat.chars()
                .mapToObj(Character::getNumericValue)
                .collect(Collectors.toSet());

        // 0 is not included in the pandigital identities.
        return MultiplicationTrioData.builder()
                .isPandigital(uniqueDigits.size() == 9 && !uniqueDigits.contains(0))
                .isLongerThan9Digits(false)
                .build();
    }

    @Value
    @Builder
    private static class MultiplicationTrioData {
        private final boolean isPandigital;
        private final boolean isLongerThan9Digits;
    }
}
