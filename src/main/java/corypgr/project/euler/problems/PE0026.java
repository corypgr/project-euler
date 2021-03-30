package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.DivisionUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 26
 *
 * https://projecteuler.net/problem=26
 *
 * I was actually asked a question similar to this in an interview, so I've given some thought on how to detect the
 * repeating part of the decimal. Basically, you track the remainder and when it repeats that is where your repeating
 * decimal is. We can roll our own division algorithm to determine this.
 */
public class PE0026 implements Problem {
    @Override
    public ProblemSolution solve() {
        DivisionUtil util = new DivisionUtil();
        int bestDenominator = 0;
        int bestDenominatorLength = 0;

        for (int i = 1; i < 1000; i++) {
            String divisionString = util.divideWithRepeatingDecimalRepresentation(1, i);
            String[] divisionParts = divisionString.split("[()]");

            // Only process the pieces that have parens. If it is length 1 then there are no parens.
            // The second part is the piece between the parens.
            if (divisionParts.length != 1 && divisionParts[1].length() > bestDenominatorLength) {
                bestDenominatorLength = divisionParts[1].length();
                bestDenominator = i;
            }
        }

        return ProblemSolution.builder()
                .solution(bestDenominator)
                .descriptiveSolution("Denominator with longest repeating decimal: " + bestDenominator)
                .build();
    }
}
