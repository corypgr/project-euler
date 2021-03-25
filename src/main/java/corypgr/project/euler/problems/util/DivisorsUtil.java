package corypgr.project.euler.problems.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Small utility for determining the divisors in a number.
 */
public class DivisorsUtil {
    public Set<Long> getDivisors(long value) {
        Set<Long> divisors = getProperDivisors(value);
        divisors.add(value); // If we want all divisors, then include the passed in value along with the proper divisors.
        return divisors;
    }

    public Set<Long> getProperDivisors(long value) {
        if (value <= 1) {
            return new HashSet<>();
        }

        Set<Long> divisors = new HashSet<>();
        divisors.add(1L); // Only 1 doesn't have 1 as a proper divisor.

        // Don't need to check past sqrt(value). We know there will be exactly 1 larger divisor (greater than
        // sqrt(value)) which we can compute and there won't be any other divisors after sqrt(value).
        long maxDivisor = (long) Math.sqrt(value);
        for (long i = 2; i <= maxDivisor; i++) {
            if (value % i == 0) {
                divisors.add(i);
                divisors.add(value / i); // might be the same as i. Set will dedupe for us.
            }
        }
        return divisors;
    }

    public long getNumDivisors(long value) {
        long numDivisors = 0;

        // Don't need to check past sqrt(value). Can double count each divisor before that since we know there will be
        // exactly 1 larger divisor (greater than sqrt(value)) and there won't be any other divisors after sqrt(value).
        long maxDivisor = (long) Math.sqrt(value);
        for (int i = 1; i <= maxDivisor; i++) {
            if (value % i == 0) {
                if (i == maxDivisor) {
                    numDivisors++;
                } else {
                    numDivisors += 2;
                }
            }
        }
        return numDivisors;
    }
}
