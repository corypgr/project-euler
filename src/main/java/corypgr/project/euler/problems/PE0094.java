package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.DivisorsUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
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
 *
 * The above works, but is both too slow and gives overflows for integers and longs due to the large multiplications.
 * Switching to BigIntegers makes it far too slow.
 *
 * After researching a little, I found the following sequence that lined up closely with the numbers I had found before
 * overflowing: https://oeis.org/A003500 which is the sequence for Heronian triangles with consecutive sides. This was
 * very close to the sequence I was generating where:
 *  * Consecutive sides Sequence | Almost Equilateral Perimeter
 *  * 14                         | 16
 *  * 52                         | 50
 *  * 194                        | 196
 *  * 724                        | 722
 *
 * The difference was that for our solution, we alternate subtracting and adding 2 to the consecutive sides solution.
 * Doing this by hand for the values under 1 billion actually gave me the correct solution, but this isn't satisfying,
 * largely because I don't quite follow why they're related.
 *
 * Going back to researching Heronian Triangles, there are formulas for generating all Heronian Triangles provided at
 * https://en.wikipedia.org/wiki/Heronian_triangle where you use 3 variables with GCD(m, n, k) = 1. That would probably
 * be expensive to compute. The wiki page says it's more efficient to generate all integer triangles and check if the
 * areas are integer values, which is what I was trying to do :P
 *
 * There is a special note for Isosceles Heronian Triangles, which relies only on having 2 coprime variables:
 * https://en.wikipedia.org/wiki/Integer_triangle#Isosceles_Heronian_triangles This looks pretty promising. We can find
 * all such variables by doing something similar to what we did in Problem 75, then run them through the 2 formulas,
 * keeping the values where the 3 sides are v,v,v-1 or v,v,v+1.
 *
 * What are the possible coprime values of u and v for our isosceles Heronian Triangle formulas? With a max size of
 * 333,333,334 and the largest side given by u^2 + v^2, sqrt(333,333,334) ~= 18,267 is a good place to start for a max u
 * value. For a max v, we need to look at sqrt(333,333,334 / 2) ~= 12,909 since we're adding 2 squares together and
 * u > v.
 */
public class PE0094 implements Problem {
    private static final int MAX_SIDE_LENGTH = 333_333_334;
    private static final int MAX_U = (int) Math.sqrt(MAX_SIDE_LENGTH);
    private static final int MAX_V = (int) Math.sqrt(MAX_SIDE_LENGTH / 2);

    @Override
    public ProblemSolution solve() {
        long solution = Stream.generate(new HeronianTriangleSupplier())
                .takeWhile(Objects::nonNull)
                .filter(this::isAlmostEquilateral)
                .mapToLong(Triangle::getPerimeter)
                .sum();

        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("The sum of all almost equilateral triangle perimeters with integer side lengths" +
                        " and areas: " + solution)
                .build();
    }

    private boolean isAlmostEquilateral(Triangle triangle) {
        return Math.abs(triangle.getA() - triangle.getC()) == 1;
    }

    private static final class HeronianTriangleSupplier implements Supplier<Triangle> {
        private final int[] valToSquareMap;
        private final List<List<Long>> valToPrimeDivisorsMap;

        // u > v and u + v is odd for our formulas to work.
        private int u;
        private int v;

        // Pre-stored triangle values. We populate these and only return these values.
        private Triangle formulaATriangle;
        private Triangle formulaBTriangle;

        public HeronianTriangleSupplier() {
            this.valToPrimeDivisorsMap = getPrimeDivisorsMap();
            this.valToSquareMap = getValToSquareMap();

            // Start with our u > v and u + v odd conditions met.
            this.u = 2;
            this.v = 1;
            setTrianglesWithUandV();
        }

        @Override
        public Triangle get() {
            if (formulaATriangle != null) {
                Triangle result = formulaATriangle;
                formulaATriangle = null;
                return result;
            }
            if (formulaBTriangle != null) {
                Triangle result = formulaBTriangle;
                formulaBTriangle = null;
                return result;
            }

            advanceUandV();
            findNextUandV();
            if (v > MAX_V) {
                return null;
            }

            return get();
        }

        /**
         * Finds the next U and V which are coprime and produce Triangles, taking into account our short-circuiting.
         */
        private void findNextUandV() {
            while (v <= MAX_V && !areCoprime(u, v)) {
                advanceUandV();
            }

            if (v > MAX_V) {
                return;
            }

            setTrianglesWithUandV();
            if (formulaATriangle == null && formulaBTriangle == null) {
                rolloverV();
                findNextUandV();
            }
        }

        private void advanceUandV() {
            u += 2; // Increment u by 2 to maintain our u + v is odd condition.
            if (u > MAX_U) {
                rolloverV();
            }
        }

        private void rolloverV() {
            v++;
            u = v + 1; // Keeps u > v and u + v is odd conditions.
        }

        private boolean areCoprime(int a, int b) {
            List<Long> aSortedPrimeDivisors = valToPrimeDivisorsMap.get(a);
            List<Long> bSortedPrimeDivisors = valToPrimeDivisorsMap.get(b);
            int aIndex = 0;
            int bIndex = 0;
            while (aIndex < aSortedPrimeDivisors.size() && bIndex < bSortedPrimeDivisors.size()) {
                long aVal = aSortedPrimeDivisors.get(aIndex);
                long bVal = bSortedPrimeDivisors.get(bIndex);

                if (aVal == bVal) {
                    return false;
                } else if (aVal < bVal) {
                    aIndex++;
                } else {
                    bIndex++;
                }
            }

            // no matches found.
            return true;
        }

        private List<List<Long>> getPrimeDivisorsMap() {
            PrimeGenerator primeGenerator = new PrimeGenerator();
            List<Long> primes = primeGenerator.generatePrimesList((long) Math.sqrt(MAX_U));
            DivisorsUtil divisorsUtil = new DivisorsUtil();

            List<List<Long>> result = new ArrayList<>();
            result.add(Collections.emptyList()); // 0th element.

            LongStream.rangeClosed(1, MAX_U)
                    .mapToObj(u -> getSortedPrimeDivisors(u, divisorsUtil, primes))
                    .forEach(result::add);
            return result;
        }

        private List<Long> getSortedPrimeDivisors(long val, DivisorsUtil divisorsUtil, List<Long> primes) {
            return divisorsUtil.getPrimeDivisors(val, primes).stream()
                    .sorted()
                    .collect(Collectors.toList());
        }

        private int[] getValToSquareMap() {
            int[] result = new int[MAX_U + 1]; // offset 0 value.
            for (int i = 0; i <= MAX_U; i++) {
                result[i] = i * i;
            }
            return result;
        }

        /**
         * Attempts to set up our triangles for the current u and v. There is short-circuiting here, were we don't set
         * a triangle after we've passed certain thresholds. This lets us skip to higher v values more quickly. Full
         * short-circuiting kicks in when neither triangle would be set.
         */
        private void setTrianglesWithUandV() {
            int equalSides = valToSquareMap[u] + valToSquareMap[v];
            if (equalSides > MAX_SIDE_LENGTH) {
                // Both triangles use equalSides, so neither will be set.
                return;
            }

            int unequalSideFormulaA = 2 * (valToSquareMap[u] - valToSquareMap[v]);
            // The unequalSide grows faster than the equalSides here. After unequalSide > equalSides, the difference
            // between the two will only grow larger, so we can skip setting the triangle as a signal to move to v + 1.
            if (unequalSideFormulaA <= MAX_SIDE_LENGTH && unequalSideFormulaA <= equalSides + 1) {
                formulaATriangle = new Triangle(equalSides, equalSides, unequalSideFormulaA);
            }

            int unequalSideFormulaB = 4 * u * v;
            // The equalSides grow faster than the unequalSide here. After equalSides > unequalSide, the difference
            // between the two will only grow larger, so we can skip setting the triangle as a signal to move to v + 1.
            if (unequalSideFormulaB <= MAX_SIDE_LENGTH && equalSides <= unequalSideFormulaB + 1) {
                formulaBTriangle = new Triangle(equalSides, equalSides, unequalSideFormulaB);
            }
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
}
