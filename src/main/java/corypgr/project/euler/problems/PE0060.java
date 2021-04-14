package corypgr.project.euler.problems;

import corypgr.project.euler.problems.prime.PrimeGenerator;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Problem 60
 *
 * https://projecteuler.net/problem=60
 *
 * This seems like a sort of graph problem. For each prime number, we can determine all other primes that that prime
 * number can concatenate with to create a prime. Drop all primes who have less than 4 other primes to concatenate with.
 * Then check the overlap of the remaining sets to see if there is a grouping of 5 or more.
 *
 * Prime checks for this are interesting. Since we get up to 8 digit numbers, I can't generate all primes up through that
 * range. These larger numbers are pretty sparse, so we use our old division check for primes after a certain point.
 */
public class PE0060 implements Problem {
    private static final long MAX_PRIME_GENERATED = 50000L;
    private static final long MAX_GROUPING_PRIME = 10000L;
    private static final int GROUPING_TARGET = 5;

    @Override
    public ProblemSolution solve() {
        Map<Long, List<Long>> primeToGrouping = getPrimesWithGroupingAmount();
        List<Set<Long>> targetGroupings = new LinkedList<>();
        Set<Long> seenPrimes = new HashSet<>();
        for (Map.Entry<Long, List<Long>> entry : primeToGrouping.entrySet()) {
            seenPrimes.add(entry.getKey());
            targetGroupings.addAll(findTargetGrouping(primeToGrouping, entry.getValue(), Set.of(entry.getKey()), seenPrimes));
        }

        long minSum = targetGroupings.stream()
                .mapToLong(grouping -> grouping.stream()
                        .mapToLong(Long::longValue)
                        .sum())
                .min()
                .getAsLong();
        return ProblemSolution.builder()
                .solution(minSum)
                .descriptiveSolution("Minimum sum of a group of 5 primes where any 2 primes concatenate to make another" +
                        " prime: " + minSum)
                .build();
    }

    private List<Set<Long>> findTargetGrouping(Map<Long, List<Long>> primeToGrouping, List<Long> intersectionSoFar,
                                          Set<Long> groupingSoFar, Set<Long> seenPrimes) {
        if (groupingSoFar.size() >= GROUPING_TARGET) {
            return List.of(groupingSoFar);
        }

        List<Set<Long>> targetGroupings = new LinkedList<>();
        Set<Long> newSeenPrimes = new HashSet<>(seenPrimes);

        for (long prime : intersectionSoFar) {
            if (!newSeenPrimes.contains(prime)) {
                newSeenPrimes.add(prime);
                List<Long> newIntersection = intersection(intersectionSoFar, primeToGrouping.get(prime));
                if (newIntersection.size() >= GROUPING_TARGET) {
                    Set<Long> newGroupingSet = new HashSet<>(groupingSoFar);
                    newGroupingSet.add(prime);
                    targetGroupings.addAll(findTargetGrouping(primeToGrouping, newIntersection, newGroupingSet, newSeenPrimes));
                }
            }
        }
        return targetGroupings;
    }

    private Map<Long, List<Long>> getPrimesWithGroupingAmount() {
        PrimeGenerator primeGenerator = new PrimeGenerator();
        Set<Long> primes = primeGenerator.generatePrimesSet(MAX_PRIME_GENERATED);

        Map<Long, List<Long>> primeToGrouping = new HashMap<>();
        Iterator<Long> primeIterOuter = primes.iterator();
        for (long primeOuter = primeIterOuter.next(); primeOuter < MAX_GROUPING_PRIME; primeOuter = primeIterOuter.next()) {
            List<Long> grouping = new LinkedList<>();
            Iterator<Long> primeIterInner = primes.iterator();
            for (long primeInner = primeIterInner.next(); primeInner < MAX_GROUPING_PRIME; primeInner = primeIterInner.next()) {
                if (primeInner == primeOuter) {
                    // Doing this here so that we end up with a sorted list.
                    grouping.add(primeOuter);
                } else if (isConcatenatablePrime(primeOuter, primeInner, primes)) {
                    grouping.add(primeInner);
                }
            }

            if (grouping.size() >= GROUPING_TARGET) {
                primeToGrouping.put(primeOuter, grouping);
            }
        }
        return primeToGrouping;
    }

    private boolean isConcatenatablePrime(long a, long b, Set<Long> primes) {
        long concatLeft = Long.parseLong(a + "" + b);
        if (!isPrime(concatLeft, primes)) {
            return false;
        }

        long concatRight = Long.parseLong(b + "" + a);
        return isPrime(concatRight, primes);
    }

    private boolean isPrime(long num, Set<Long> generatedPrimes) {
        if (num <= MAX_PRIME_GENERATED) {
            return generatedPrimes.contains(num);
        }

        int maxToCheck = (int) Math.sqrt(num);
        Iterator<Long> primeIter = generatedPrimes.iterator();
        for (long prime = primeIter.next(); primeIter.hasNext() && prime <= maxToCheck; prime = primeIter.next()) {
            if (num % prime == 0) {
                return false;
            }
        }
        return true;
    }

    private List<Long> intersection(List<Long> sortedA, List<Long> sortedB) {
        if (sortedA == null || sortedB == null) {
            return Collections.emptyList();
        }

        List<Long> result = new LinkedList<>();

        Iterator<Long> aIter = sortedA.iterator();
        long aVal;
        Iterator<Long> bIter = sortedB.iterator();
        long bVal = bIter.next();
        while (aIter.hasNext() && bIter.hasNext()) {
            aVal = aIter.next();
            while (aVal < bVal && aIter.hasNext()) {
                aVal = aIter.next();
            }
            while (bVal < aVal && bIter.hasNext()) {
                bVal = bIter.next();
            }

            if (aVal == bVal) {
                result.add(aVal);
            }
        }

        return result;
    }
}
