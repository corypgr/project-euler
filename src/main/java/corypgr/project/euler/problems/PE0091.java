package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Problem 91
 *
 * https://projecteuler.net/problem=91
 *
 * I feel like there should be some sort of elegant way to solve this, but the first idea that comes to mind is to simply
 * generate all possible triangle combinations, test if they are right triangles, and count the result.
 *
 * There are 51 integer numbers, so there are 51*51 = 2601 point combinations. Since we need 2 points, there are
 * 2601 * 2601 = 6765201 total triangle combinations (with the origin as one point). Many of these will just be lines,
 * and one will just be the point at (0,0). The rest we'll need to determine whether they are right triangles or not.
 *
 * We can use Pythagorean's theorem to tell if a triangle is a right triangle. We actually apply it a few times. First,
 * to determine the lengths of the sides (length = sqrt((x2 - x1)^2 + (y2 - y1)^2)). Then, using the largest side length
 * as c, check if a^2 + b^2 = c^2.
 */
public class PE0091 implements Problem {
    private static final int MAX_COORD = 50;
    private static final int[] SQUARES;
    static {
        SQUARES = new int[MAX_COORD + 1]; // plus 1 for 0 indexing.
        for (int i = 0; i <= MAX_COORD; i++) {
            SQUARES[i] = i * i;
        }
    }

    @Override
    public ProblemSolution solve() {
        long rightTriangleCount = Stream.generate(new TriangleCoordinateSupplier())
                .takeWhile(Objects::nonNull)
                .filter(this::isRightTriangle)
                .count();

        return ProblemSolution.builder()
                .solution(rightTriangleCount)
                .descriptiveSolution("Number of right triangles: " + rightTriangleCount)
                .build();
    }

    private boolean isRightTriangle(TriangleCoordinates coords) {
        if (isAxisLine(coords)) {
            return false;
        }

        int side1Square = getLengthSquared(coords.getP1(), coords.getP2());
        int side2Square = getLengthSquared(coords.getP1(), coords.getP3());
        int side3Square = getLengthSquared(coords.getP2(), coords.getP3());

        int aSquare;
        int bSquare;
        int cSquare;
        if (side1Square > side2Square && side1Square > side3Square) {
            cSquare = side1Square;
            aSquare = side2Square;
            bSquare = side3Square;
        } else if (side2Square > side1Square && side2Square > side3Square) {
            cSquare = side2Square;
            aSquare = side1Square;
            bSquare = side3Square;
        } else {
            cSquare = side3Square;
            aSquare = side1Square;
            bSquare = side2Square;
        }

        return aSquare + bSquare == cSquare;
    }

    private boolean isAxisLine(TriangleCoordinates coords) {
        return (coords.getP1().getX() == coords.getP2().getX() && coords.getP1().getX() == coords.getP3().getX()) ||
                (coords.getP1().getY() == coords.getP2().getY() && coords.getP1().getY() == coords.getP3().getY());
    }

    /**
     * The length of a side would be sqrt((x2 - x1)^2 + (y2 - y1)^2)). Since we're going to square the lengths later on
     * we can just return the result without taking the square root.
     */
    private int getLengthSquared(Point p1, Point p2) {
        int xSquare = SQUARES[Math.abs(p2.getX() - p1.getX())];
        int ySquare = SQUARES[Math.abs(p2.getY() - p1.getY())];
        return xSquare + ySquare;
    }

    private static final class TriangleCoordinateSupplier implements Supplier<TriangleCoordinates> {
        private final List<Point> points;
        private int p1Index;
        private int p2Index;
        private final Point origin;

        public TriangleCoordinateSupplier() {
            this.points = new ArrayList<>();
            for (int x = 0; x <= MAX_COORD; x++) {
                for (int y = 0; y <= MAX_COORD; y++) {
                    this.points.add(new Point(x, y));
                }
            }
            this.origin = points.get(0); // We know the first one is (0,0).

            // Index 0 is the same as the origin. Offset the indices so that we never have 2 of the same points
            // in our coordinates.
            this.p1Index = 1;
            this.p2Index = 2;
        }

        @Override
        public TriangleCoordinates get() {
            // Emulating a nested for loop logic here.
            if (p2Index >= points.size()) {
                p1Index++;
                p2Index = p1Index + 1;
            }

            // If p2Index is still >= points.size(), then p1Index must now be points.size() - 1, and we have
            // exhausted all unique combinations.
            if (p2Index >= points.size()) {
                return null;
            }

            Point p1 = points.get(p1Index);
            Point p2 = points.get(p2Index);
            TriangleCoordinates result = new TriangleCoordinates(p1, p2, origin);

            p2Index++;

            return result;
        }
    }

    @Value
    private static final class TriangleCoordinates {
        private final Point p1;
        private final Point p2;
        private final Point p3;
    }

    @Value
    private static final class Point {
        private final int x;
        private final int y;
    }
}
