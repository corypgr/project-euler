package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.CountMap;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Problem 51
 *
 * https://projecteuler.net/problem=51
 *
 * Had to think a bit about this one. I think a good strategy is to zero out specific digits of
 * prime numbers, and count the ones that match after the zeroing out.
 *
 * The process of doing this isn't going to be super simple though... I'm going to try to generate
 * permutations of the places to zero out, starting with 1 digit and working up.
 *
 * What should the max prime be? No idea. We'll increase in multiples of 10 until we find the number
 * we're looking for. Multiples of 10 is because the first digit could be replaced, going from 1 to 9, so
 * we need the full range of numbers which can start with 1 to 9.
 */
public class PE0051 implements Problem {
    private static final long MAX_PRIME = 999999;
    private static final long MIN_PRIME = 100000;
    private static final int TARGET_FAMILY_SIZE = 8;

    @Override
    public ProblemSolution solve() {
        Match smallestMatch = findSmallestPrimeThatMasksToFamily();

        return ProblemSolution.builder()
                .solution(smallestMatch.getSmallestPrime())
                .descriptiveSolution("Smallest prime part of an 8 value prime family: " + smallestMatch)
                .build();
    }

    private Match findSmallestPrimeThatMasksToFamily() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet(MAX_PRIME);
        List<List<Integer>> primesAsListOfInts = primes.stream()
                .dropWhile(prime -> prime < MIN_PRIME)
                .map(this::longToListOfInts)
                .collect(Collectors.toList());

        int numDigits = String.valueOf(MAX_PRIME).length();
        for (int numZeroes = 1; numZeroes < numDigits; numZeroes++) {
            List<List<Integer>> masks = getMasks(numDigits, numZeroes);

            for (List<Integer> mask : masks) {
                CountMap<Long> countMap = new CountMap<>();
                primesAsListOfInts.stream()
                        .filter(prime -> isMaskingOnSameDigit(prime, mask))
                        .map(prime -> mask(prime, mask, 0))
                        .map(this::intsToLong)
                        .forEach(countMap::addCount);

                Optional<Long> matchedMaskedPrime = countMap.entrySet().stream()
                        .filter(entry -> entry.getValue() == TARGET_FAMILY_SIZE)
                        .map(Map.Entry::getKey)
                        .findFirst();

                if (matchedMaskedPrime.isPresent()) {
                    return new Match(getSmallestPrimeFromMaskedPrime(matchedMaskedPrime.get(), mask, primes), mask);
                }
            }
        }
        return null;
    }

    private long getSmallestPrimeFromMaskedPrime(long maskedPrime, List<Integer> mask, Set<Long> primes) {
        List<Integer> maskedPrimeAsInts = longToListOfInts(maskedPrime);
        // Pad with zeroes if the first digits were masked away.
        while (maskedPrime < MIN_PRIME) {
            maskedPrime *= 10;
            maskedPrimeAsInts.add(0, 0);
        }

        return IntStream.range(0, 9)
                .mapToObj(valToInsert -> mask(maskedPrimeAsInts, mask, valToInsert))
                .map(this::intsToLong)
                .filter(primes::contains)
                .findFirst()
                .get();
    }

    private List<Integer> longToListOfInts(long val) {
        List<Integer> list = new LinkedList<>();
        long remaining = val;
        while (remaining > 0L) {
            list.add(0, (int) remaining % 10);
            remaining /= 10;
        }
        return list;
    }

    private long intsToLong(List<Integer> ints) {
        long result = 0;
        for (int val : ints) {
            result *= 10;
            result += val;
        }
        return result;
    }

    private boolean isMaskingOnSameDigit(List<Integer> primeAsInts, List<Integer> mask) {
        long digitCount =  mask.stream()
                .map(primeAsInts::get)
                .distinct()
                .count();

        return digitCount == 1L;
    }

    private List<Integer> mask(List<Integer> primeAsInts, List<Integer> mask, int valToInsert) {
        List<Integer> newList = new ArrayList<>(primeAsInts);
        mask.forEach(index -> newList.set(index, valToInsert));
        return newList;
    }

    /**
     * Custom permutation generator to be more efficient. If we used PermutationUtil, it would do a lot of
     * unnecessary work and we would need to dedupe values.
     * <p>
     * Returns a list of values to "mask", where each value is the index of a value to mask.
     */
    private List<List<Integer>> getMasks(int numDigits, int numZeroedOut) {
        List<List<Integer>> masks = new LinkedList<>();

        // start with 0, 1, ...
        List<Integer> currentMask = IntStream.range(0, numZeroedOut)
                .boxed()
                .collect(Collectors.toList());
        while (currentMask != null) {
            masks.add(currentMask);
            currentMask = getNextMask(currentMask, numDigits);
        }
        return masks;
    }

    private List<Integer> getNextMask(List<Integer> mask, int numDigits) {
        for (int i = mask.size() - 1; i >= 0; i--) {
            int maxVal = numDigits - mask.size() + i;
            if (mask.get(i) < maxVal) {
                List<Integer> nextMask = new ArrayList<>(mask);
                int insertVal = mask.get(i) + 1;
                for (int j = i; j < mask.size(); j++, insertVal++) {
                    nextMask.set(j, insertVal);
                }
                return nextMask;
            }
        }
        return null;
    }

    @Value
    private static class Match {
        private final long smallestPrime;
        private final List<Integer> mask;
    }
}
