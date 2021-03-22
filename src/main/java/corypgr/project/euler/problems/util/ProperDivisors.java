package corypgr.project.euler.problems.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Small utility for determining the proper divisors in a number.
 */
public class ProperDivisors {
    public Set<Long> getDivisors(long value) {
        Set<Long> divisors = new HashSet<>();

        // Don't need to check past sqrt(value). We know there will be exactly 1 larger divisor (greater than
        // sqrt(value)) which we can compute and there won't be any other divisors after sqrt(value).
        long maxDivisor = (long) Math.sqrt(value);
        for (long i = 1; i <= maxDivisor; i++) {
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
