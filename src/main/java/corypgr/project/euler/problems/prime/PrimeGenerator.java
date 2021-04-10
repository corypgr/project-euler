package corypgr.project.euler.problems.prime;

import corypgr.project.euler.problems.util.LinkedHashSetWithModifiableIteration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Utilities for generating prime numbers.
 *
 * Uses the sieve of Eratosthenes algorithm to generate prime numbers. I have
 * some modifications to the basic algorithm though, to improve generation
 * time a bit. Basically, this modifies the list of known prime numbers
 * continuously and reuses the list when generating non-prime numbers.
 */
public class PrimeGenerator {
    public Set<Long> generatePrimesSet(long maxVal) {
        return generatePrimes(maxVal);
    }

    public List<Long> generatePrimesList(long maxVal) {
        LinkedHashSetWithModifiableIteration<Long> primes = generatePrimes(maxVal);

        List<Long> primeList = new ArrayList<>(primes.size());
        for (Long prime : primes) {
            primeList.add(prime);
        }
        return primeList;
    }

    private LinkedHashSetWithModifiableIteration<Long> generatePrimes(long maxVal) {
        LinkedHashSetWithModifiableIteration<Long> primes = new LinkedHashSetWithModifiableIteration<>();
        long maxPrimeToMultiply = (long) Math.sqrt(maxVal);

        // Add all numbers up to the maxVal starting with 2, the first (and best) prime number.
        for (long i = 2; i <= maxVal; i++) {
            primes.add(i);
        }

        // Remove from the Primes list all multiples of the currently seen prime value.
        // Here is where the modifications affect the values we actually see. For
        // example, even though 4 was added above, we won't process 4 at this level because
        // it will be removed.
        for (Long prime : primes) {
            // Don't need to go beyond the sqrt of the maxVal. All products after this will be larger than maxVal
            if (prime > maxPrimeToMultiply) {
                return primes;
            }

            // The innerIterator here is determining what values to multiply our prime
            // against to produce the products we remove.
            long lastProduct = -1;
            Set<Long> valuesToRemove = new HashSet<>();
            for (Iterator<Long> innerIterator = primes.iteratorStartingWith(prime); innerIterator.hasNext() && lastProduct < maxVal; ) {
                Long multiplier = innerIterator.next();

                lastProduct = prime * multiplier;
                valuesToRemove.add(lastProduct);
            }
            primes.removeAll(valuesToRemove);
        }
        return primes;
    }
}
