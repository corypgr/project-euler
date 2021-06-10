package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * Problem 95
 *
 * https://projecteuler.net/problem=95
 *
 * The first thought for these types of questions is often to use a recursive solution. I think I'll do a bit of a
 * variation to avoid the potential stack overflow. First, we'll generate all of the next elements for each integer.
 * Then we'll try to generate a chain for each integer using the next element mapping. If we find an amicable chain,
 * we determine the length and minimum member and create another mapping with that information. All nodes in our chain
 * that is not part of the amicable chain loop is marked as part of an invalid chain, since if it were part of an
 * amicable chain that value would have repeated. Similarly, if our values exceed 1 million, all values in the chain are
 * marked invalid.
 *
 * The above works, but is pretty slow. Takes around ~7 seconds to find the appropriate solution. Finding the divisors
 * of each number was the slow part here. I swapped out using DivisorsUtil for generating our nextElementMap, instead
 * using a strategy similar to the Sieve of Eratosthenes. This brought us under 1 second.
 */
public class PE0095 implements Problem {
    private static final int MAX_NUMBER = 1_000_000;

    @Override
    public ProblemSolution solve() {
        int[] nextElementMap = getNextElementMap();

        ChainDetail[] detailMap = new ChainDetail[MAX_NUMBER + 1]; // Offset to account for 0 indexing.
        IntStream.rangeClosed(0, MAX_NUMBER)
                .forEach(i -> fillChainDetailFrom(i, nextElementMap, detailMap));

        ChainDetail largestChain = Arrays.stream(detailMap)
                .max(Comparator.comparingInt(ChainDetail::getMembersInChain))
                .get();
        return ProblemSolution.builder()
                .solution(largestChain.getSmallestMember())
                .descriptiveSolution("Largest amicable chain has " + largestChain.getMembersInChain() + " members and" +
                        " the smallest member is: " + largestChain.getSmallestMember())
                .build();
    }

    /**
     * Strategy is similar to the Sieve of Eratosthenes. For every possible divisor we determine the multipliers for
     * the divisor and add the divisor to the current divisor sum for each multiplier.
     */
    private int[] getNextElementMap() {
        int[] result = new int[MAX_NUMBER + 1]; // Offset to account for 0 indexing.
        Arrays.fill(result, 1); // 1 is a proper divisor of all but 0 and 1.
        // 0 and 1 do not have any proper divisors.
        result[0] = 0;
        result[1] = 0;
        for (int divisor = 2; divisor <= MAX_NUMBER; divisor++) {
            // Skip the divisor itself, since a number is not a proper divisor of itself.
            for (int multiple = divisor + divisor; multiple <= MAX_NUMBER; multiple += divisor) {
                result[multiple] = result[multiple] + divisor;
            }
        }
        return result;
    }

    private void fillChainDetailFrom(int num, int[] nextElementMap, ChainDetail[] detailMap) {
        if (detailMap[num] != null) {
            return;
        }

        Set<Integer> seenValues = new HashSet<>();
        int nextVal = num;
        Node dummyHead = new Node(-1); // Dummy start node.
        Node curNode = dummyHead;

        // If we've seen the value, we've found an amicable chain. If it exceeds the Max number, the chain is not valid.
        // If the nextVal already has a detail, then we've already processed it. Any nodes before it cannot be part of
        // an amicable chain, or they would also have a mapping.
        while (!seenValues.contains(nextVal) && nextVal <= MAX_NUMBER && detailMap[nextVal] == null) {
            seenValues.add(nextVal);
            Node nextNode = new Node(nextVal);
            curNode.setNext(nextNode);
            curNode = nextNode;
            nextVal = nextElementMap[nextVal];
        }

        // We haven't completed the chain. Set all nodes in the chain as not being a valid chain.
        if (!seenValues.contains(nextVal)) {
            // Don't set for nextVal. It either is already set or exceeds our max.
            seenValues.forEach(v -> detailMap[v] = ChainDetail.NON_VALID_DETAIL);
            return;
        }

        // We have completed the amicable chain. All nodes before the nextVal node is not part of the amicable chain.
        curNode = dummyHead.getNext();
        while (curNode.getVal() != nextVal) {
            detailMap[curNode.getVal()] = ChainDetail.NON_VALID_DETAIL;
            curNode = curNode.getNext();
        }

        // The rest of the chain is part of the amicable chain.
        Node startOfAmicableChain = curNode;
        int lengthOfChain = 0;
        int smallestVal = Integer.MAX_VALUE;
        while (curNode != null) {
            lengthOfChain++;
            if (curNode.getVal() < smallestVal) {
                smallestVal = curNode.getVal();
            }
            curNode = curNode.getNext();
        }

        curNode = startOfAmicableChain;
        ChainDetail amicableDetail = ChainDetail.builder()
                .smallestMember(smallestVal)
                .membersInChain(lengthOfChain)
                .build();
        while (curNode != null) {
            detailMap[curNode.getVal()] = amicableDetail;
            curNode = curNode.getNext();
        }
    }

    @Value
    @Builder
    private static final class ChainDetail {
        public static final ChainDetail NON_VALID_DETAIL = ChainDetail.builder()
                .isValidAmicableChain(false)
                .build();

        @Builder.Default
        private final int smallestMember = -1;
        @Builder.Default
        private final int membersInChain = -1;
        @Builder.Default
        private final boolean isValidAmicableChain = true;
    }

    @Data
    private static final class Node {
        private final int val;
        private Node next;
    }
}
