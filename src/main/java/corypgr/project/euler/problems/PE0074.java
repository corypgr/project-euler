package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.LongStream;

/**
 * Problem 74
 *
 * https://projecteuler.net/problem=74
 *
 * This is interesting. My first thought is we can write a sort of dynamic programming solution that memoizes how many
 * links are in a chain after the current point. The problem with that is the chain will loop, but we don't know where
 * exactly...
 *
 * Next idea, create linked lists for each chain, with a mapping from the value to the node in the chain. When we see a
 * specific value that we've already processed, we can simply drop into the chain we've already computed and walk the
 * nodes until we see repeats. This would at least save us recomputing the chain values.
 */
public class PE0074 implements Problem {
    private static final long MAX_NUM = 1_000_000;
    private static final long TARGET_CHAIN_LENGTH = 60;

    private static final long[] DIGIT_FACTORIALS;
    static {
        DIGIT_FACTORIALS = new long[10];
        DIGIT_FACTORIALS[0] = 1;
        for (int i = 1; i < 10; i++) {
            DIGIT_FACTORIALS[i] = DIGIT_FACTORIALS[i - 1] * i;
        }
    }

    @Override
    public ProblemSolution solve() {
        Map<Long, Node> valToNode = new HashMap<>();

        long countOfChainsAtTargetLength = LongStream.range(1, MAX_NUM)
                .map(v -> getNonRepeatingLength(v, valToNode))
                .filter(v -> v == TARGET_CHAIN_LENGTH)
                .count();
        return ProblemSolution.builder()
                .solution(countOfChainsAtTargetLength)
                .descriptiveSolution("Number of chains of 60 length: " + countOfChainsAtTargetLength)
                .build();
    }

    private long getNonRepeatingLength(long value, Map<Long, Node> valToNode) {
        Node node = connectChain(value, valToNode);
        Set<Long> seenVals = new HashSet<>();
        while (!seenVals.contains(node.getValue())) {
            seenVals.add(node.getValue());
            node = node.getNext();
        }
        return seenVals.size();
    }

    private Node connectChain(long value, Map<Long, Node> valToNode) {
        if (valToNode.containsKey(value)) {
            valToNode.get(value);
        }

        Node startNode = new Node(value);
        valToNode.put(value, startNode);
        Node prevNode = startNode;
        long nextVal = getNextValue(value);
        while (!valToNode.containsKey(nextVal)) {
            Node nextNode = new Node(nextVal);
            valToNode.put(nextVal, nextNode);
            prevNode.setNext(nextNode);

            prevNode = nextNode;
            nextVal = getNextValue(nextVal);
        }
        prevNode.setNext(valToNode.get(nextVal));

        return startNode;
    }

    private long getNextValue(long value) {
        long result = 0;

        long remaining = value;
        while (remaining > 0) {
            result += DIGIT_FACTORIALS[(int) remaining % 10];
            remaining /= 10;
        }
        return result;
    }

    @Data
    private static final class Node {
        private final long value;

        @Setter
        private Node next;
    }
}
