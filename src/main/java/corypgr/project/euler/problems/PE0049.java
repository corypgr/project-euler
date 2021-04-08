package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.PermutationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Problem 49
 *
 * https://projecteuler.net/problem=49
 *
 * This problem is asking us to find a series of three 4 digit numbers which are:
 *  * Separated by the same value.
 *  * All Prime.
 *  * All permutations of each other.
 *
 *  Finding all the primes is an easy one to start with. With a max of 9999, this isn't even a very expensive operation.
 *  I think what we should do after that is: for each 4 digit prime, generate the possible permutations of the prime. If
 *  there are at least 2 other permutations which are Prime, see if they have the same increasing sequence. Keep the
 *  sequences that do, and toss out the one already stated in the problem.
 */
public class PE0049 implements Problem {
    private static final long MAX_PRIME = 9999L;
    private static final long MIN_PRIME = 1000L;

    @Override
    public ProblemSolution solve() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        PermutationUtil<Integer> permutationUtil = new PermutationUtil<Integer>();
        Set<Long> primes = primeGenerator.generatePrimesSet(MAX_PRIME);
        Set<Long> alreadyProcessedPrimes = new HashSet<>(); // For avoiding doing multiple permutation generations

        Set<List<Long>> primeSequences = primes.stream()
                .dropWhile(prime -> prime < MIN_PRIME)
                .filter(Predicate.not(alreadyProcessedPrimes::contains))
                .map(this::longToListOfInts)
                .map(permutationUtil::getAllPermutations)
                .map(list -> permutationsToPrimesGreaterThan1000(list, primes))
                .peek(alreadyProcessedPrimes::addAll)
                .map(this::longsToIncreasingSequence)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (primeSequences.size() != 2) {
            throw new IllegalStateException("Expecting exactly 2 sequences, but found: " + primeSequences.size());
        }

        List<Long> desiredSequence = primeSequences.stream()
                .filter(sequence -> sequence.get(0) != 1487L) // Remove the one from the question.
                .findFirst()
                .get();
        Long desiredSequenceConcatenated = concatenateLongs(desiredSequence);

        return ProblemSolution.builder()
                .solution(desiredSequenceConcatenated)
                .descriptiveSolution("The other sequential primes: " + desiredSequence)
                .build();
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

    private List<Long> permutationsToPrimesGreaterThan1000(List<List<Integer>> permutations, Set<Long> primes) {
        return permutations.stream()
                .map(this::permutationToLong)
                .filter(v -> v >= 1000)
                .distinct()
                .filter(primes::contains)
                .collect(Collectors.toList());
    }

    private long permutationToLong(List<Integer> permutation) {
        long result = 0;
        for (int val : permutation) {
            result *= 10;
            result += val;
        }
        return result;
    }

    /**
     * Returns null if there is no increasing sequence.
     */
    private List<Long> longsToIncreasingSequence(List<Long> longs) {
        if (longs.size() < 3) {
            return null;
        }

        Set<Long> longsAsSet = new HashSet<>(longs);

        for (int i = 0; i < longs.size(); i++) {
            long iVal = longs.get(i);
            for (int j = i + 1; j < longs.size(); j++) {
                long jVal = longs.get(j);

                long max = Math.max(iVal, jVal);
                long min = Math.min(iVal, jVal);
                long difference = max - min;
                long logicalNextAfterAddingDifference = max + difference;
                if (longsAsSet.contains(logicalNextAfterAddingDifference)) {
                    return List.of(min, max, logicalNextAfterAddingDifference);
                }
            }
        }
        return null;
    }

    private long concatenateLongs(List<Long> longs) {
        long result = 0;
        for (long val : longs) {
            result *= 10000;
            result += val;
        }
        return result;
    }
}
