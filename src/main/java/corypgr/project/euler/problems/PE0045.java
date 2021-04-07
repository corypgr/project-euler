package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 45
 *
 * https://projecteuler.net/problem=45
 *
 * More of these Triangle and Pentagonal number problems. I could do something similar to problem 44 here, generate all
 * of the numbers up to a point and then start checking them. That doesn't seem necessary though. Here, we can sort of
 * increment through the numbers, keeping them roughl in line until they match.
 *
 * Note that the nested loop order doesn't matter. I had thought about putting the Hexagonal numbers on the outside since
 * there are less of them, but that doesn't change how many iterations we do. We can skip straight to the number given
 * in the problem though for our starting indices.
 */
public class PE0045 implements Problem {
    // The problem gives us a point to start at. Set to the index after that.
    private static final long TRIANGLE_START_INDEX = 285 + 1;
    private static final long PENTAGONAL_START_INDEX = 165 + 1;
    private static final long HEXAGONAL_START_INDEX = 143 + 1;

    @Override
    public ProblemSolution solve() {
        long next = findNextNumber();

        return ProblemSolution.builder()
                .solution(next)
                .descriptiveSolution("Next triangle number that is also a pentagonal and hexagonal number: " + next)
                .build();
    }

    private long findNextNumber() {
        long tIndex = TRIANGLE_START_INDEX;
        long pIndex = PENTAGONAL_START_INDEX;
        long hIndex = HEXAGONAL_START_INDEX;

        long triangle = getTriangleNumber(tIndex);
        long pentagonal = getPentagonalNumber(pIndex);
        long hexagonal = getHexagonalNumber(hIndex);
        while (true) {
            while (pentagonal < triangle) {
                pIndex++;
                pentagonal = getPentagonalNumber(pIndex);
            }

            while (hexagonal < triangle) {
                hIndex++;
                hexagonal = getHexagonalNumber(hIndex);
            }

            if (triangle == pentagonal && pentagonal == hexagonal) {
                return triangle;
            }

            tIndex++;
            triangle = getTriangleNumber(tIndex);
        }
    }

    private long getTriangleNumber(long index) {
        return (index * (index + 1)) / 2;
    }

    private long getPentagonalNumber(long index) {
        return (index * (3 * index - 1)) / 2;
    }

    private long getHexagonalNumber(long index) {
        return index * (2 * index - 1);
    }
}
