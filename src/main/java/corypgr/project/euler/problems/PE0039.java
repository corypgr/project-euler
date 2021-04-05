package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 39
 *
 * https://projecteuler.net/problem=39
 *
 * Not too hard. Iterate over perimeters from 3 (min with all sides at 1) to 1000. For each perimeter caclulate the
 * number of (a, b, c) sets where a^2 + b^2 == c^2 by iterating over the various combinations.
 */
public class PE0039 implements Problem {
    // Min would be 1 on each side, which isn't a valid right triangle, but an ok starting place.
    private static final int MIN_PERIMETER = 3;
    private static final int MAX_PERIMETER = 1000;

    @Override
    public ProblemSolution solve() {
        int bestPerimeter = 0;
        int bestPerimeterCount = 0;
        for (int p = MIN_PERIMETER; p <= MAX_PERIMETER; p++) {
            int countForP = numCombinationsToGetToPerimeter(p);
            if (countForP > bestPerimeterCount) {
                bestPerimeter = p;
                bestPerimeterCount = countForP;
            }
        }

        return ProblemSolution.builder()
                .solution(bestPerimeter)
                .descriptiveSolution("The perimeter with the highest number of (a,b,c) combinations: " + bestPerimeter)
                .build();
    }

    private int numCombinationsToGetToPerimeter(int perimeter) {
        int count = 0;

        // Max checks assume at least 1 per remaining side.
        for (int a = 1; a <= perimeter - 2; a++) {
            int aSquared = a * a;
            for (int b = a; a + b <= perimeter - 1; b++) {
                int bSquared = b * b;
                int c = perimeter - a - b;
                int cSquared = c * c;
                if (aSquared + bSquared == cSquared) {
                    count++;
                }
            }
        }
        return count;
    }
}
