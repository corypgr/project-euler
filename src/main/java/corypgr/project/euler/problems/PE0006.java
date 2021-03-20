package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 6
 *
 * https://projecteuler.net/problem=6
 *
 * Very straightforward. I remember there is a minor optimization to avoid the addition for the square of the sum. The
 * sum of a list of integers (increasing by 1) is: ((first + end) * (size / 2)). I'll apply this, but it won't affect
 * runtime significantly.
 */
public class PE0006 implements Problem {
    private static final long MAX_VAL = 100L;

    @Override
    public ProblemSolution solve() {
        long sumOfSquares = getSumOfSquares();
        long squareOfSum = getSquareOfSum();

        long solution = (squareOfSum - sumOfSquares);
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Difference: " + solution)
                .build();
    }

    private static long getSumOfSquares() {
         long sum = 0;
         for (int i = 1; i <= MAX_VAL; i++) {
             sum += i * i;
         }
         return sum;
    }

    private static long getSquareOfSum() {
        long sum = (MAX_VAL + 1) * (MAX_VAL / 2);
        return sum * sum;
    }
}
