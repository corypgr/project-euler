package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.math.BigInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Problem 94
 *
 * https://projecteuler.net/problem=94
 *
 * Interesting problem. It isn't very hard to generate all almost equilateral triangles with perimeters <= 1 billion. We
 * basically have a max at 333,333,333 for the two equal sides, and 333,333,334 for the unequal side. We can simply
 * loop through 1 - 333,333,333 (value v) and take two triangles for each v: v,v,v+1 and v,v,v-1.
 *
 * The other condition here is that the triangles we care about must have an integer area. We can use Heron's formula to
 * determine the area given 3 sides: For a,b,c side lengths and s = perimeter/2 = (a+b+c)/2,
 * area = sqrt(s(s-a)(s-b)(s-c)). We could calculate every area and then determine if the value is an integer, but there
 * are a few patterns we can probably leverage here. First, s is ether an integer or half of an integer (eg. 2.5). If s
 * is half an integer, then the area is going to be sqrt(XXXX.0625) where XXXX is some integer leading value. This will
 * never be an integer value. That means we can throw away all perimeters whose sums are odd. The perimeter is only odd
 * when the unequal side is odd (the equal sides * 2 is always even), and the unequal side is only odd when the equal
 * sides are even. So, we can throw away all triangles where the equals sides are an even number.
 *
 * Another thing we can do is avoid calculating the sqrt. We can keep track of all relevant squares, and just check if
 * s(s-a)(s-b)(s-c) is a square. This could be a really large number of squares though, possibly more than we can store
 * in memory. We don't actually need to track all of them. If we order our triangles in ascending perimeter size, then
 * our area will be strictly increasing as well. In that case, we just need to keep track of the "next" square. If
 * s(s-a)(s-b)(s-c) < the next square then it isn't a square. If it is larger, we can calculate squares until our
 * squares are >= s(s-a)(s-b)(s-c).
 */
public class PE0094 implements Problem {
    private static final int MAX_PERIMETER = 1_000_000_000;

    @Override
    public ProblemSolution solve() {
        AreaChecker areaChecker = new AreaChecker();

        int solution = Stream.generate(new TriangleSupplier())
                .takeWhile(triangle -> triangle.getPerimeter() <= MAX_PERIMETER)
                .filter(areaChecker::hasSquareArea)
                .mapToInt(Triangle::getPerimeter)
                .sum();
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("The sum of all almost equilateral triangle perimeters with integer side lengths" +
                        " and areas: " + solution)
                .build();
    }

    private static final class TriangleSupplier implements Supplier<Triangle> {
        private int nextEqualLength;

        // When true, the unequalSideLength will be equalSideLength - 1. Else, it will be equalSideLength + 1.
        private boolean nextUnequalLengthIsSmaller;

        public TriangleSupplier() {
            this.nextEqualLength = 1;

            // Start with false because 1,1,0 is invalid.
            this.nextUnequalLengthIsSmaller = false;
        }

        @Override
        public Triangle get() {
            Triangle result = new Triangle(nextEqualLength, nextEqualLength,
                    nextUnequalLengthIsSmaller ? nextEqualLength - 1 : nextEqualLength + 1);

            // Set up the next triangle value.
            nextUnequalLengthIsSmaller = !nextUnequalLengthIsSmaller;
            if (nextUnequalLengthIsSmaller) {
                // Increment by 2 so that nextEqualLength is always odd, and then our perimeter is always even.
                nextEqualLength += 2;
            }
            return result;
        }
    }

    @Value
    @RequiredArgsConstructor
    private static final class Triangle {
        private final int a;
        private final int b;
        private final int c;

        private final int perimeter;

        public Triangle(int a, int b, int c) {
            this(a, b, c, a + b + c);
        }
    }

    private static final class AreaChecker {
        private long nextSquare;
        private long nextSquareBase;

        public AreaChecker() {
            this.nextSquareBase = 1;
            this.nextSquare = 1;
        }

        public boolean hasSquareArea(Triangle triangle) {
            // We are guaranteed not to have any odd perimeters with our triangle generation logic.
            long s = triangle.getPerimeter() / 2;
            long areaSquared = s * (s - triangle.getA()) * (s - triangle.getB()) * (s - triangle.getC());
            //System.out.println(areaSquared);
            //System.out.println("perimeter: " + triangle.getPerimeter());
            boolean square = isSquare(areaSquared);
            if (square) {
                System.out.println(triangle);
                System.out.println(areaSquared);
                return true;
            }
            return false;
        }

        private boolean isSquare(long val) {
            if (val <= nextSquare) {
                return val == nextSquare;
            }

            nextSquareBase = (long) Math.sqrt(val);
            nextSquare = nextSquareBase * nextSquareBase;

            while (val > nextSquare) {
                nextSquareBase++;
                nextSquare = nextSquareBase * nextSquareBase;
            }

            return isSquare(val);
        }
    }
}
