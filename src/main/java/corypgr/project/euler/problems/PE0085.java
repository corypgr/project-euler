package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

/**
 * Problem 85
 *
 * https://projecteuler.net/problem=85
 *
 * There's probably some clever way to do this, but I don't see it. I'm going to take an iterative, and possibly
 * inefficient, approach. I'll basically generate all sizes of rectangles which contain less than 2 million rectangles,
 * along with the ones that are just longer than 2 million, and keep track of which one is closest to 2 million.
 *
 * To do this generation, we can do a nested for loop. for width and height:
 *  * Create rectangle x with width and height.
 *  * Using previously seen rectangles, determine how many rectangles (n) can fit in x.
 *  * if abs(2 million - n) < current closest (x,n), mark (x,n) as the new closest.
 */
public class PE0085 implements Problem {
    private static final int TARGET_NESTING = 2_000_000;

    @Override
    public ProblemSolution solve() {
        int bestRectangleDistanceFromTarget = Integer.MAX_VALUE;
        NestingRectangle bestRectangle = null;

        NestingRectangle curRectangle;

        // Only care about starting shapes that are squares in the outer loop. Non-square shapes are equal to
        // rectangles we have already seen.
        for (curRectangle = new NestingRectangle(1, 1); curRectangle.getNumRectanglesFitInside() <= TARGET_NESTING;
             curRectangle = new NestingRectangle(curRectangle.getWidth() + 1, curRectangle.getWidth() + 1)) {

            for (; curRectangle.getNumRectanglesFitInside() <= TARGET_NESTING;
                 curRectangle = new NestingRectangle(curRectangle.getWidth(), curRectangle.getHeight() + 1)) {

                int distanceFromTarget = Math.abs(TARGET_NESTING - curRectangle.getNumRectanglesFitInside());
                if (distanceFromTarget < bestRectangleDistanceFromTarget) {
                    bestRectangleDistanceFromTarget = distanceFromTarget;
                    bestRectangle = curRectangle;
                }
            }
        }

        int areaOfBestRectangle = bestRectangle.getWidth() * bestRectangle.getHeight();
        return ProblemSolution.builder()
                .solution(areaOfBestRectangle)
                .descriptiveSolution("The area of the rectangle whose nesting is closest to 2 million: " + areaOfBestRectangle)
                .build();
    }

    @Value
    private static final class NestingRectangle {
        private final int width;
        private final int height;
        private final int numRectanglesFitInside;

        public NestingRectangle(int width, int height) {
            this.width = width;
            this.height = height;
            this.numRectanglesFitInside = getNumRectanglesFitInside(width, height);
        }

        private static int getNumRectanglesFitInside(int width, int height) {
            int count = 0;
            for (int innerWidth = 1; innerWidth <= width; innerWidth++) {
                for (int innerHeight = 1; innerHeight <= height; innerHeight++) {
                    // Examples (width x height):
                    // * a 1x1 rectangle fits 2 times width-wise in a 2x3 rectangle, and 3 times height-wise.
                    // * a 1x2 rectangle fits 2 time width-wise in a 2x3 rectangle, and 2 times height-wise.
                    int numInnerRectanglesThatFitWide = width - innerWidth + 1;
                    int numInnerRectanglesThatFitHigh = height - innerHeight + 1;

                    int numInnerRectanglesThatFit = numInnerRectanglesThatFitWide * numInnerRectanglesThatFitHigh;
                    count += numInnerRectanglesThatFit;
                }
            }
            return count;
        }
    }
}
