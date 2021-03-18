package problems;

import util.Problem;
import util.ProblemSolution;

/**
 * Problem 4
 *
 * https://projecteuler.net/problem=4
 *
 * I can't think of a great way to optimize this right now. We need to generate all of the products, check if they are
 * palindromes, and keep the largest palindrome. We can short-circuit a bit by checking products of larger values first.
 */
public class PE0004 implements Problem {

    @Override
    public ProblemSolution solve() {
        long bestPalindromeProduct = -1;
        long lastProduct = Integer.MAX_VALUE;
        for (int i = 999; i > 0 && i * i > bestPalindromeProduct; i--) {
            for (int j = i; j > 0 && lastProduct > bestPalindromeProduct; j--) {
                lastProduct = i * j;
                if (lastProduct > bestPalindromeProduct && isPalindrome(lastProduct)) {
                    bestPalindromeProduct = lastProduct;
                }
            }
            // Reset so this doesn't affect the next iteration.
            lastProduct = Integer.MAX_VALUE;
        }
        return ProblemSolution.builder()
                .solution(bestPalindromeProduct)
                .descriptiveSolution("Best Palindrom Product: " + bestPalindromeProduct)
                .build();
    }

    /**
     * Just do normal check against the character on the opposite side of the string.
     */
    private static boolean isPalindrome(long value) {
        String strVal = Long.toString(value);
        for (int i = 0; i < strVal.length() / 2; i++) {
            if (strVal.charAt(i) != strVal.charAt(strVal.length() - i - 1)) {
                return false;
            }
        }
        return true;
    }
}
