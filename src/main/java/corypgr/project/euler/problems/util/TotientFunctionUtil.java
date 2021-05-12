package corypgr.project.euler.problems.util;

import lombok.Value;

import java.util.Set;

/**
 * Utils to calculate the Euler's Totient function phi(n) using the product formula.
 * See https://en.wikipedia.org/wiki/Euler%27s_totient_function
 */
public class TotientFunctionUtil {
    public long calculateTotientFunction(long value) {
        DivisorsUtil divisorsUtil = new DivisorsUtil();
        Set<Long> primeDivisors = divisorsUtil.getPrimeDivisors(value);
        return calculateTotientFunction(value, primeDivisors);
    }

    public long calculateTotientFunction(long value, Set<Long> primeDivisors) {
        if (value == 1) {
            // Special case. 1 is considered to be relatively prime to every positive number.
            return 1;
        }
        if (value < 1) {
            throw new IllegalArgumentException("Passed value must be larger than 1.");
        }
        if (primeDivisors == null || primeDivisors.isEmpty()) {
            throw new IllegalArgumentException("primeDivisors must have at least 1 element.");
        }

        SimpleFraction fraction = SimpleFraction.wholeNumber(value);
        for (Long primeDivisor : primeDivisors) {
            fraction = fraction.multiply(SimpleFraction.oneMinusReciprocolOf(primeDivisor));
        }
        return fraction.getLongVal();
    }

    @Value
    private static final class SimpleFraction {
        private final long numerator;
        private final long denominator;

        public static SimpleFraction wholeNumber(long wholeNumber) {
            return new SimpleFraction(wholeNumber, 1);
        }

        public static SimpleFraction oneMinusReciprocolOf(long wholeNumber) {
            return new SimpleFraction(wholeNumber - 1, wholeNumber);
        }

        public SimpleFraction multiply(SimpleFraction other) {
            return new SimpleFraction(this.numerator * other.numerator, this.denominator * other.denominator);
        }

        public long getLongVal() {
            long result = this.numerator / this.denominator;

            if (result * this.denominator != this.numerator) {
                throw new ArithmeticException("Result is not an exact long value");
            }
            return result;
        }
    }
}
