package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Problem 86
 *
 * https://projecteuler.net/problem=86
 *
 * This one turned out to be fairly challenging. The first thing to see is that the shortest path from one corner to the
 * opposite corner is a path along 2 walls of the cuboid. If you act like those two walls are flat against each other,
 * instead of making an edge, then the route calculation is that of finding the hypotenuse of a right triangle. Given
 * a cuboid with a, b, c side lengths there are 4 routes that use 2 walls. 2 of these routes will have the same length
 * though, so we're left with only 3 routes that could be the shortest (as stated in the problem). The calculation of
 * these three routes are:
 *  * sqrt(a^2 + (b + c)^2)
 *  * sqrt((a + b)^2 + c^2)
 *  * sqrt((a + c)^2 + b^2)
 *
 * As an initial solution, I simply generated these shortest routes for every unique combination of side lengths, chose
 * the shortest of the lengths, and checked if it was an integer value. This finds the right solution, but even with a
 * number of optimizations (store square->root mapping, only do sqrt on the smallest of the sums, etc..) it takes
 * ~15 seconds to run.
 *
 * I noticed some interesting patterns from my initial solution. sqrt(a^2 + (b + c)^2) was always the smallest route.
 * The way I had set up my combinations, a was always the largest of a, b, c, so the first of these calculations always
 * gave the smallest route of the three. Removing the other calculations and min checking got my first solution down to
 * ~10 seconds. Still too slow.
 *
 * Since we're down to a single calculation, I thought we could change how we look at it. Ignoring the square root, what
 * we're really looking for is the sum of 2 squares, where a^2 is "fixed" and b + c <= 2*a (since b,c <= a). We can find
 * this in a few different ways. We can try all combinations of (b + c)^2 where b,c <= a, adding it to a^2 and check if
 * the result is a square. To optimize that, you can just check all q^2 where q <= 2*a, then determine the number of
 * ways b and c can add to be q. An alternative is to look at squares above a^2, and see if the difference between those
 * squares and a^2 is also a square. Then you determine the number of combinations of b and c that add up to that
 * difference.
 *
 * I went with the latter option. Doing some testing, it looks like it iterates less than 2*a times, so it would be a
 * bit faster. I came out with a pretty fast solution.
 */
public class PE0086 implements Problem {
    private static final int TARGET_INTEGER_SHORTEST_LENGTH = 1_000_000;

    @Override
    public ProblemSolution solve() {
        SquareSet squareSet = new SquareSet();
        int a = 1;
        int intShortestLengths = 0;
        for (; intShortestLengths < TARGET_INTEGER_SHORTEST_LENGTH; a++) {
            intShortestLengths += getNumIntShortestPathsForCuboidWithFixedSide(a, squareSet);
        }

        // a is our largest dimension, so that is our M. We increment 1 past the solution in our for loop.
        int m = a - 1;

        return ProblemSolution.builder()
                .solution(m)
                .descriptiveSolution("Smallest M to get to 1 million int shortest lengths: " + m)
                .build();
    }

    private int getNumIntShortestPathsForCuboidWithFixedSide(int a, SquareSet squareSet) {
        int aSquare = squareSet.getSquare(a);
        int maxSquareDifference = squareSet.getSquare(a * 2);

        int intShortestLengths = 0;
        for (int largerSquareRoot = a + 1; ; largerSquareRoot++) {
            int largerSquare = squareSet.getSquare(largerSquareRoot);
            int squareDifference = largerSquare - aSquare;
            if (squareDifference > maxSquareDifference) {
                return intShortestLengths;
            }

            int bPlusCIntRoot = squareSet.getRoot(squareDifference);
            if (bPlusCIntRoot > -1) {
                int numCombinations = getNumBPlusCCombinations(bPlusCIntRoot, a);
                intShortestLengths += numCombinations;
            }
        }
    }

    /**
     * Sum combinations are 1 + (bPlusC - 1), 2 + (bPlusC - 2), etc... There are bPlusC / 2 of these. However, since b
     * c <= a, we must remove all combinations where b,c > a. The formula for that is bPlusC - a - 1 if bPlusC > a.
     */
    private int getNumBPlusCCombinations(int bPlusC, int a) {
        int numCombos = bPlusC / 2;
        if (bPlusC > a) {
            numCombos -= (bPlusC - a - 1);
        }
        return numCombos;
    }

    /**
     * Small utility to keep track of squares and roots, letting us only calculate those values once in code. This is
     * mostly useful for getting roots.
     */
    private static final class SquareSet {
        private final Map<Integer, Integer> squareToRoot;
        private final List<Integer> rootToSquare;
        private int largestSquare;
        private int largestRoot;

        public SquareSet() {
            this.squareToRoot = new HashMap<>();
            this.rootToSquare = new ArrayList<>();
            this.rootToSquare.add(0); // 0th position

            this.largestSquare = 0;
            this.largestRoot = 0;
        }

        public int getSquare(int root) {
            if (root > this.largestRoot) {
                while (this.largestRoot < root) {
                    addNextSquare();
                }
            }

            return rootToSquare.get(root);
        }

        public int getRoot(int val) {
            if (val > this.largestSquare) {
                while (this.largestSquare < val) {
                    addNextSquare();
                }
            }

            return squareToRoot.getOrDefault(val, -1);
        }

        private void addNextSquare() {
            this.largestRoot++;
            this.largestSquare = this.largestRoot * this.largestRoot;
            this.squareToRoot.put(this.largestSquare, largestRoot);
            this.rootToSquare.add(this.largestSquare);
        }
    }
}
