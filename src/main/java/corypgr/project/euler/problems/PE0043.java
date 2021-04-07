package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem 43
 *
 * https://projecteuler.net/problem=43
 *
 * Seems kind of interesting. My first thought is to generate all 0 to 9 pandigital numbers using our PermutationUtil.
 * Then go through each checking the condition this is asking about.
 *
 * This runs a little slow, but I think most of it is due to how long it takes to generate the permutations. I added some
 * small optimizations, like the digitsToNumMap lookup table, but it still takes around 2 seconds to run.
 *
 * ---
 * This was rolling around my head a bit, and I thought of a much more efficient way to generate our matching numbers.
 *
 * Instead of taking all pandigital numbers and checking if they match our condition, we can instead take 3 digit numbers
 * (triples) that match the individual conditions and generate a "chain" of those triples. As an example, with 1406357289
 * the chain of triples is (4,0,6)->(0,6,3)->(6,3,5)->(3,5,7)->(5,7,2)->(7,2,8)->(2,8,9), where the first link (4,0,6)
 * is a number divisible by 2, the second link divisible by 3, and so on. The first digit of the number can be determined
 * by looking at the chain and finding which digit is not yet present.
 *
 * The below does this by first generating all possible potentially valid triples for each divisor, then recursively
 * (starting at the back) trying to find links that fit in front of the already generated chain tail. The complete chains
 * are converted into their corresponding numbers.
 */
public class PE0043 implements Problem {
    private static final List<Integer> DIGITS = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    private static final List<Integer> DIVISORS = List.of(2, 3, 5, 7, 11, 13, 17);

    @Override
    public ProblemSolution solve() {
        List<Set<Triple>> tripleSetsInDivisorOrder = new ArrayList<>();
        for (int prime : DIVISORS) {
            tripleSetsInDivisorOrder.add(getAllValidTriplesFor(prime));
        }

        Set<List<Triple>> allValidChains = new HashSet<>();
        for (Triple triple : tripleSetsInDivisorOrder.get(tripleSetsInDivisorOrder.size() - 1)) {
            allValidChains.addAll(getChains(tripleSetsInDivisorOrder, Collections.singletonList(triple)));
        }

        long sumOfMatchingValues = allValidChains.stream()
                .mapToLong(this::tripleChainToLong)
                .sum();

        return ProblemSolution.builder()
                .solution(sumOfMatchingValues)
                .descriptiveSolution("Sum of pandigital numbers matching divisible condition: " + sumOfMatchingValues)
                .build();
    }

    private Set<Triple> getAllValidTriplesFor(int divisor) {
        return Stream.iterate(divisor, val -> val < 1000, val -> val + divisor)
                .map(Triple::new)
                .filter(Triple::hasNoDuplicates)
                .collect(Collectors.toSet());
    }

    private Set<List<Triple>> getChains(List<Set<Triple>> tripleSetsInDivisorOrder, List<Triple> ending) {
        Set<List<Triple>> validTripleChains = new HashSet<>();
        Set<Triple> previousTripleSet = tripleSetsInDivisorOrder.get(tripleSetsInDivisorOrder.size() - 1 - ending.size());
        for (Triple previousTriple : previousTripleSet) {
            if (previousTriple.isValidLinkBefore(ending)) {
                List<Triple> newEnding = new LinkedList<>(ending);
                newEnding.add(0, previousTriple);

                if (newEnding.size() == 7) {
                    validTripleChains.add(newEnding);
                } else {
                    validTripleChains.addAll(getChains(tripleSetsInDivisorOrder, newEnding));
                }
            }
        }
        return validTripleChains;
    }

    /**
     * Assumes the chain is valid.
     */
    private long tripleChainToLong(List<Triple> chain) {
        Set<Integer> remainingDigits = new HashSet<>(DIGITS);
        remainingDigits.remove(chain.get(0).getA());
        remainingDigits.remove(chain.get(0).getB());
        for (Triple triple : chain) {
            remainingDigits.remove(triple.getC());
        }

        // Start with the first digit.
        long result = remainingDigits.iterator().next();

        result *= 10;
        result += chain.get(0).getA();
        result *= 10;
        result += chain.get(0).getB();
        for (Triple triple : chain) {
            result *= 10;
            result += triple.getC();
        }
        return result;
    }

    @Value
    private static class Triple {
        private final int a;
        private final int b;
        private final int c;

        public Triple(int threeDigitVal) {
            this.c = threeDigitVal % 10;
            int remaining = threeDigitVal / 10;
            this.b = remaining % 10;
            this.a = remaining / 10;
        }

        public boolean hasNoDuplicates() {
            return this.a != this.b && this.a != this.c && this.b != this.c;
        }

        public boolean isValidLinkBefore(List<Triple> chain) {
            if (this.b != chain.get(0).a || this.c != chain.get(0).b) {
                return false;
            }

            Set<Integer> allVals = new HashSet<>();
            allVals.add(this.a);
            allVals.add(this.b);
            allVals.add(this.c);
            for (Triple link : chain) {
                allVals.add(link.a);
                allVals.add(link.b);
                allVals.add(link.c);
            }

            // 2 Triples in a valid chain overlap with 2 digits. Together they have 4 unique values. That is the 3 values
            // from the first in the chain and 1 value from the end of the other chain. Extending this out, the number
            // of values in a completed chain should be size + 2. Here, we add 3 because this is not yet in the chain.
            int expectedSetSize = chain.size() + 3;
            return allVals.size() == expectedSetSize;
        }
    }
}
