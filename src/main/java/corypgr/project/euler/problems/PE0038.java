package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Problem 38
 *
 * https://projecteuler.net/problem=38
 *
 * Fairly straightforward. Start generating these sequences, storing the largest as we go.
 *
 * Since the series of numbers we multiply by has to be > 1, we can set a max for our initial
 * integer at something which when multiplied by 1 is 4 digits and when multiplied by 2 is 5 digits.
 * The largest such number is 9999, but the largest non-repeating number would be 9876:
 * 9876 * 1 = 9876
 * 9876 * 2 = 19752
 * The concatenation of those primes is 9 digits. Though the result isn't pandigital, and could be shortened,
 * this is a decent starting point.
 */
public class PE0038 implements Problem {
    private static final int MAX_START_NUM = 9876;

    @Override
    public ProblemSolution solve() {
        String bestConcatenatedProduct = "123456789"; // Some dummy value. Set to the minimum pandigital product.
        for (int start = 1; start <= MAX_START_NUM; start++) {
            StringBuilder concatenatedProducts = new StringBuilder();
            for (int product = start; concatenatedProducts.length() < 9; product += start) {
                concatenatedProducts.append(product);
            }

            String concatenatedProductsStr = concatenatedProducts.toString();
            if (isPandigital(concatenatedProductsStr) && concatenatedProductsStr.compareTo(bestConcatenatedProduct) > 0) {
                bestConcatenatedProduct = concatenatedProductsStr;
            }
        }

        int solutionAsNum = Integer.parseInt(bestConcatenatedProduct);
        return ProblemSolution.builder()
                .solution(solutionAsNum)
                .descriptiveSolution("Largest pandigital concatenated product: " + solutionAsNum)
                .build();
    }

    private boolean isPandigital(String val) {
        if (val.length() != 9) {
            return false;
        }

        Set<Integer> uniqueDigits = val.chars()
                .mapToObj(Character::getNumericValue)
                .collect(Collectors.toSet());

        // 0 is not a number in the pandigital sequence.
        return uniqueDigits.size() == 9 && !uniqueDigits.contains(0);
    }
}
