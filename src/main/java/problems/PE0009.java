package problems;

import lombok.Value;

/**
 * Problem 9
 *
 * https://projecteuler.net/problem=9
 *
 * We can do a little math so that we're not checking too many values, but other than that this is a brute-force
 * strategy.
 */
public class PE0009 {
    private static final long GOAL = 1000L;

    public static void main(String[] args) {
        Triplet triplet = findTriplet();
        System.out.println("Triplet: " + triplet);
        System.out.println("Product: " + triplet.getA() * triplet.getB() * triplet.getC());
    }

    private static Triplet findTriplet() {
        for (long a = 1; a < GOAL; a++) {
            for (long b = a + 1; b < GOAL; b++) {
                long c = GOAL - a - b;
                if ((a * a) + (b * b) == c * c) {
                    return new Triplet(a, b, c);
                }
            }
        }
        throw new IllegalStateException("No valid triplet found");
    }

    @Value
    private static class Triplet {
        private final long a;
        private final long b;
        private final long c;
    }
}
