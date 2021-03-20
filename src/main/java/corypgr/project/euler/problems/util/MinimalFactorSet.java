package corypgr.project.euler.problems.util;

import corypgr.project.euler.problems.prime.PrimeGenerator;

import java.util.Map;
import java.util.Set;

/**
 * Calculates the minimal prime number set needed to create (via products) all numbers up to maxVal;
 */
public class MinimalFactorSet {
    public CountMap<Long> getMinimalFactorSetForNumbersUpTo(long maxVal) {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet(maxVal);

        CountMap<Long> minimalFactorSet = new CountMap<>();
        for (int i = 2; i < maxVal; i++) {
            CountMap<Long> localFactors = getFactors(i, primes);
            mergeFactorSets(minimalFactorSet, localFactors);
        }

        return minimalFactorSet;
    }

    private static CountMap<Long> getFactors(long val, Set<Long> primes) {
        long remaining = val;
        CountMap<Long> factors = new CountMap<>();
        for (Long prime : primes) {
            while (remaining % prime == 0) {
                factors.addCount(prime);
                remaining = remaining / prime;
            }
        }
        return factors;
    }

    /**
     * Check if the new factors contain more elements than the ones we've already seen. Swap in the larger set if they
     * do. Ex: When we get to 16, the existing set will have (2, 3). The factors of 16 are (2, 4) which is more so we
     * swap that value in.
     */
    private static void mergeFactorSets(CountMap<Long> mergeInto, CountMap<Long> mergeFrom) {
        for (Map.Entry<Long, Integer> entry : mergeFrom.entrySet()) {
            if (!mergeInto.containsKey(entry.getKey()) ||
                    mergeInto.get(entry.getKey()) < entry.getValue()) {
                mergeInto.put(entry.getKey(), entry.getValue());
            }
        }
    }
}
