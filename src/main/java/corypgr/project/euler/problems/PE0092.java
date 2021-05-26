package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Arrays;

/**
 * Problem 92
 *
 * https://projecteuler.net/problem=92
 *
 * Looks like s simpler version of Problem 74. I will do a similar strategy here, using a recursive method and keeping
 * track of where values end (89 or 1). Unlike problem 74, our numbers cannot go above our max number. This is because
 * the square of a digit is not that large. 9*9 = 81. With at most 7 9s together, our sum of digit squares can be at
 * most 81 * 7 = 567. This means our effective lookup table only needs to contain 567 elements. All numbers above that
 * will reach a lookup location in the table after a single summation of the digit squares, and there isn't a point in
 * storing their result in the lookup table because they will never be retrieved.
 */
public class PE0092 implements Problem {
    private static final int MAX_NUM = 10_000_000;
    private static final int[] DIGIT_SQUARES = { 0, 1, 4, 9, 16, 25, 36, 49, 64, 81};
    private static final int MAX_LOOKUP_VAL = 567;

    @Override
    public ProblemSolution solve() {
        int[] numToTerminatingVal = new int[MAX_LOOKUP_VAL + 1]; // Add 1 for 0, so we can do direct lookups.
        Arrays.fill(numToTerminatingVal, -1);
        numToTerminatingVal[0] = 0;
        numToTerminatingVal[1] = 1;
        numToTerminatingVal[89] = 89;

        int countOf89 = 0;
        for (int i = 2; i < MAX_NUM; i++) {
            int terminatingVal = getTerminatingValue(i, numToTerminatingVal);
            if (terminatingVal == 89) {
                countOf89++;
            }
        }

        return ProblemSolution.builder()
                .solution(countOf89)
                .descriptiveSolution("Count of numbers below 10 million terminating at 89: " + countOf89)
                .build();
    }

    private int getTerminatingValue(int num, int[] numToTerminatingVal) {
        if (num <= MAX_LOOKUP_VAL && numToTerminatingVal[num] > -1) {
            return numToTerminatingVal[num];
        }

        int nextInChain = getSumOfDigitSquares(num);
        int terminatingResult = getTerminatingValue(nextInChain, numToTerminatingVal);
        if (num <= MAX_LOOKUP_VAL) {
            numToTerminatingVal[num] = terminatingResult;
        }
        return terminatingResult;
    }

    private int getSumOfDigitSquares(int num) {
        int result = 0;

        int remaining = num;
        while (remaining > 0) {
            result += DIGIT_SQUARES[remaining % 10];
            remaining /= 10;
        }
        return result;
    }
}
