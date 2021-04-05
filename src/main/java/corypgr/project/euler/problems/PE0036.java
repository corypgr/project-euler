package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.stream.IntStream;

/**
 * Problem 36
 *
 * https://projecteuler.net/problem=36
 *
 * Pretty straightforward. Cycle through all numbers less than 1 million, checking if both bases are palindromes.
 * Converting from one base to the other is probably the challenge here, but Java's Integer.toString() can take a param
 * that specifies the base. That will simplify things here.
 */
public class PE0036 implements Problem {
    private static final int MAX_VAL = 1_000_000;
    @Override
    public ProblemSolution solve() {
        int sumOfDoubleBasePalindromes = IntStream.range(0, MAX_VAL)
                .filter(this::isDoubleBasePalindrome)
                .sum();

        return ProblemSolution.builder()
                .solution(sumOfDoubleBasePalindromes)
                .descriptiveSolution("Sum of all double base palindromes under 1 million: " + sumOfDoubleBasePalindromes)
                .build();
    }

    private boolean isDoubleBasePalindrome(int val) {
        return isPalindrome(Integer.toString(val, 10)) &&
                isPalindrome(Integer.toString(val, 2));
    }

    private boolean isPalindrome(String val) {
        int middleIndex = val.length() / 2;
        int endIndex = val.length() - 1;
        for (int i = 0; i < middleIndex; i++) {
            if (val.charAt(i) != val.charAt(endIndex - i)) {
                return false;
            }
        }
        return true;
    }
}
