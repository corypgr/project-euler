package corypgr.project.euler.problems.util;

import corypgr.project.euler.problems.prime.PrimeGenerator;

import java.util.HashSet;
import java.util.List;
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

    public Set<Long> getProperPrimeDivisors(long value) {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        List<Long> primes = primeGenerator.generatePrimesList((long) Math.sqrt(value));
        return getProperPrimeDivisors(value, primes);
    }

    /**
     * Can pass in the primes that you want to check against. Can be useful if you're getting the prime divisors for
     * many numbers.
     */
    public Set<Long> getProperPrimeDivisors(long value, List<Long> primes) {
        Set<Long> divisors = new HashSet<>();
        long remaining = value;

        // Only need to check up to the sqrt(val), since there wouldn't be any more divisors after that.
        // Initial setting. Can change when we find divisors.
        long maxLoop = (long) Math.sqrt(value);
        for (int i = 0; i < primes.size() && primes.get(i) <= maxLoop; i++) {
            long prime = primes.get(i);
            while (remaining % prime == 0) {
                remaining /= prime;
                maxLoop = remaining; // After dividing, remaining might be a prime number, making it a proper divisor too.
                divisors.add(prime);
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
