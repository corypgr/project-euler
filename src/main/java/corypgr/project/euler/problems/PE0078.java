package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Problem 78
 *
 * https://projecteuler.net/problem=78
 *
 * Another one of these partition problems. This one is closer to Problem 76, but there's an interesting change. In this
 * problem, we aren't told how big we need to go, and our target is unclear at "divisible by 1 million". We can use the
 * same initial strategy, but where we have a changing pool of subset values. i.e. for coins in problem 31 we had 2, 5,
 * 10, etc.. and in Problem 76 we had 1-99. Here we just have >= 1.
 *
 * The above doesn't work... First, the partition numbers grow very large. I had to switch to modding the results by
 * 1 million to keep the values small enough. Since we only care if the result is divisible by 1 million, and we're only
 * doing addition, we only need the results % 1 million. Then we run out of memory before a solution is actually found.
 * Even if it did work, it was taking far too long :)
 *
 * After digging around a little bit, I found an efficient way to generate the partition numbers using a recurrence at
 * https://en.wikipedia.org/wiki/Partition_function_(number_theory)#Recurrence_relations which is based on generating
 * Pentagonal numbers. After switching the algorithm to that, and using BigInteger, I reached the solution in... 3.5
 * seconds. Swapping in my modding solution to keep things as integers dropped it down to a less than 1 second solution.
 */
public class PE0078 implements Problem {
    private static final int MOD_AMOUNT = 1_000_000;

    @Override
    public ProblemSolution solve() {
        int solution = getTargetN();
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Value of n to give a number of partitions divisible by 1 million: " + solution)
                .build();
    }

    private int getTargetN() {
        List<Long> nToPartitionNumber = new ArrayList<>();
        nToPartitionNumber.add(1L); // p(0) = 1.
        LinkedList<PentagonalNumber> pentagonalNumbers = new LinkedList<>();
        pentagonalNumbers.add(PentagonalNumber.fromGeneratingK(1)); // First Pentagonal Number, used to generate others.

        for (int n = 1; ; n++) {
            long partitionNumber = getPartitionNumber(nToPartitionNumber, pentagonalNumbers, n);
            nToPartitionNumber.add(partitionNumber);
            // partitonNumber is already modded by MOD_AMOUNT. x % MOD_AMOUNT % MOD_AMOUNT == x % MOD_AMOUNT.
            if (partitionNumber == 0) {
                return n;
            }
        }
    }

    private long getPartitionNumber(List<Long> nToPartitionNumber, LinkedList<PentagonalNumber> pentagonalNumbers,
                                          int n) {
        fillPentagonalNumbers(pentagonalNumbers, n);
        long result = 0;
        for (PentagonalNumber pentagonalNumber : pentagonalNumbers) {
            int nMinusPentagonalNumber = n - pentagonalNumber.getVal();
            if (nMinusPentagonalNumber < 0) {
                return result % MOD_AMOUNT;
            }

            long recurrencePartitionNumber = nToPartitionNumber.get(nMinusPentagonalNumber);
            result = pentagonalNumber.isAddPartitionRecurrence() ?
                    result + recurrencePartitionNumber :
                    result - recurrencePartitionNumber;
        }

        return result % MOD_AMOUNT;
    }

    private void fillPentagonalNumbers(LinkedList<PentagonalNumber> pentagonalNumbers, int newMax) {
        while (pentagonalNumbers.getLast().getVal() < newMax) {
            pentagonalNumbers.add(pentagonalNumbers.getLast().getNextPentagonalNumber());
        }
    }

    @Value
    private static class PentagonalNumber {
        private final int generatingK;
        private final int val;
        private final boolean addPartitionRecurrence;

        public static PentagonalNumber fromGeneratingK(int k) {
            // Guaranteed to be a positive whole number, regardless of positive or negative input.
            int val = (k * (3 * k - 1)) / 2;
            boolean addPartitionRecurrence = (k + 1) % 2 == 0;
            return new PentagonalNumber(k, val, addPartitionRecurrence);
        }

        /**
         * The k sequence for increasing pentagonal numbers is 1, -1, 2, -2, 3, -3, etc...
         * Use the current k to determine what the next k should be based on whether k is negative or not.
         */
        public PentagonalNumber getNextPentagonalNumber() {
            int nextK = this.generatingK > 0 ? -1 * this.generatingK : (-1 * this.generatingK) + 1;
            return PentagonalNumber.fromGeneratingK(nextK);
        }
    }
}
