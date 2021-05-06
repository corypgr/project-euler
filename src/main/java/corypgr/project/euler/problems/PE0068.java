package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.PermutationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Problem 68
 *
 * https://projecteuler.net/problem=68
 *
 * This was an interesting question. I actually solved this on paper, using a few optimistic guesses and going from
 * there. To start, we know that 10 must be in the outer ring because it is 2 digits long. We're looking for a 16 digit
 * result. All values in the inner ring are counted twice, so if 10 were in the inner ring we would end up with a 17
 * digit result. Since 10 is in the outer ring, the largest first digit possible is 6 (smallest from 6-10). Next, I
 * chose the optimistic assumption that 5 is directly connected to 6 to get the second largest value. Then I tried
 * combinations until I found a set of chains who all sum to the same value. This was the answer.
 *
 * Being able to solve this on paper is nice, but the challenge here should be to use programming to come up with the
 * answer. To do this, I'll make the same assumptions as above (6 - 10 in outer ring and 5 connected to 6). I'll plan to
 * generate all sequences by generating all permutations of the values in each ring (where 6 and 5 are the starting
 * values), then combine those permutations in all possible ways to find all sequences. This is actually a pretty small
 * number of sequences. There are only 24 ways to permute 4 objects. So we have 24 * 24 = 576 different sequences. We'll
 * check each sequence to see if each group of 3 adds up to the same number, and keep the largest value.
 */
public class PE0068 implements Problem {
    private static final int OUTER_RING_START = 6;
    private static final int INNER_RING_START = 5;

    private static final List<Integer> OUTER_RING_OTHER_VALS = List.of(7, 8, 9, 10);
    private static final List<Integer> INNER_RING_OTHER_VALS = List.of(1, 2, 3, 4);

    @Override
    public ProblemSolution solve() {
        PermutationUtil<Integer> permutationUtil = new PermutationUtil<>();
        List<List<Integer>> outerRingPermutations = permutationUtil.getAllPermutations(OUTER_RING_OTHER_VALS);
        outerRingPermutations = prependToLists(OUTER_RING_START, outerRingPermutations);
        List<List<Integer>> innerRingPermutations = permutationUtil.getAllPermutations(INNER_RING_OTHER_VALS);
        innerRingPermutations = prependToLists(INNER_RING_START, innerRingPermutations);

        long largestSequence = 0L;
        for (List<Integer> outerRing : outerRingPermutations) {
            for (List<Integer> innerRing : innerRingPermutations) {
                Sequence sequence = getSequence(outerRing, innerRing);
                if (sequence.isValidSequence()) {
                    largestSequence = Math.max(sequence.getLongValue(), largestSequence);
                }
            }
        }

        return ProblemSolution.builder()
                .solution(largestSequence)
                .descriptiveSolution("Largest 16 digit sequence in a magic 5-gon ring: " + largestSequence)
                .build();
    }

    private List<List<Integer>> prependToLists(int val, List<List<Integer>> lists) {
        List<List<Integer>> result = new LinkedList<>();
        for (List<Integer> list : lists) {
            List<Integer> newList = new ArrayList<>();
            newList.add(val);
            newList.addAll(list);
            result.add(newList);
        }
        return result;
    }

    private static Sequence getSequence(List<Integer> outerRing, List<Integer> innerRing) {
        return new Sequence(
                new Triplet(outerRing.get(0), innerRing.get(0), innerRing.get(1)),
                new Triplet(outerRing.get(1), innerRing.get(1), innerRing.get(2)),
                new Triplet(outerRing.get(2), innerRing.get(2), innerRing.get(3)),
                new Triplet(outerRing.get(3), innerRing.get(3), innerRing.get(4)),
                new Triplet(outerRing.get(4), innerRing.get(4), innerRing.get(0)));
    }

    @Value
    private static class Sequence {
        private final Triplet a;
        private final Triplet b;
        private final Triplet c;
        private final Triplet d;
        private final Triplet e;

        public long getLongValue() {
            // Same cheating as in Triplet.
            return Long.parseLong("" + a.getLongValue() + b.getLongValue() +
                    c.getLongValue() + d.getLongValue() + e.getLongValue());
        }

        public boolean isValidSequence() {
            int aSum = a.getSum();
            return aSum == b.getSum() && aSum == c.getSum() && aSum == d.getSum() && aSum == e.getSum();
        }
    }

    @Value
    private static class Triplet {
        private final int a;
        private final int b;
        private final int c;

        public int getSum() {
            return a + b + c;
        }

        public long getLongValue() {
            // Cheating a little here. It would be more efficient to add each value and multiply by 10 to shift things,
            // but since a value can be 10, we need to do some extra logic to make that work. String parsing is easier,
            // and this will be executed only a few times, so it isn't worth doing it the hard way.
            return Long.parseLong("" + a + b + c);
        }
    }
}
