package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 28
 *
 * https://projecteuler.net/problem=28
 *
 * Interesting problem. I'll try to create a pattern for determining the direction to fill out the grid then find the
 * diagonal sums the lazy way.
 * ----------
 * Came back to this after seeing problem 58. I noticed a simple pattern for determining the values on the diagonal.
 * Starting with ring 0 (the center) the diagonals (or "corners") of a ring can be calculated by adding ring * 2 to the
 * previously calculated diagonal. Looking at the first 3 rings, we have:
 *  * ring 0 just has 1. Start there: 1
 *  * ring 1 will add 2 * 1 = 2 to the previous vals: 3, 5, 7, 9
 *  * ring 2 will add 2 * 2 = 4 to the previous vals: 13, 17, 21, 25
 */
public class PE0028 implements Problem {
    // Side length for a ring is ring * 2 + 1.
    private static final int MAX_RING = 500;

    @Override
    public ProblemSolution solve() {
        int curNum = 1;
        int sum = curNum;
        for (int ring = 1; ring <= 500; ring++) {
            int amountToAddForRing = ring * 2;
            for (int corner = 0; corner < 4; corner++) {
                curNum += amountToAddForRing;
                sum += curNum;
            }
        }

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of the diagonals of the spiral grid: " + sum)
                .build();
    }
}
