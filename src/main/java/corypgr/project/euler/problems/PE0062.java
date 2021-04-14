package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Problem 62
 *
 * https://projecteuler.net/problem=62
 *
 * We only need to detect if a value is a permutation of already seen cubes. We can generate cubes, sorting the digits
 * to create something to check against, and keep track of all of the numbers we've seen so far that create that sorted
 * set of digits. When we find a sortedDigits number with 5 originals, we just pick out the smallest of the originals.
 */
public class PE0062 implements Problem {
    @Override
    public ProblemSolution solve() {
        long smallestCubeWith5CubedPermutations = findSmallestCubeWith5CubedPermutations();
        return ProblemSolution.builder()
                .solution(smallestCubeWith5CubedPermutations)
                .descriptiveSolution("The smallest cube whose permutations are also cubes: "
                        + smallestCubeWith5CubedPermutations)
                .build();
    }

    private long findSmallestCubeWith5CubedPermutations() {
        Map<Long, List<Long>> sortedDigitsToOriginals = new HashMap<>();

        for (long i = 1L; ; i++) {
            long cube = i * i * i;
            long sortedDigits = numToReverseSortedDigitsNum(cube);
            List<Long> curListForSortedDigits = addToMap(sortedDigitsToOriginals, cube, sortedDigits);
            if (curListForSortedDigits.size() == 5) {
                return curListForSortedDigits.stream()
                        .mapToLong(Long::longValue)
                        .min()
                        .getAsLong();
            }
        }
    }

    /**
     * Returns the list stored for the sortedDigits key.
     */
    private List<Long> addToMap(Map<Long, List<Long>> sortedDigitsToOriginals, long cube, long sortedDigits) {
        List<Long> list = sortedDigitsToOriginals.getOrDefault(sortedDigits, null);
        if (list == null) {
            list = new LinkedList<>();
        }
        list.add(cube);

        sortedDigitsToOriginals.put(sortedDigits, list);
        return list;
    }

    private long numToReverseSortedDigitsNum(long num) {
        List<Integer> digits = new LinkedList<>();
        long remaining = num;
        while (remaining > 0) {
            digits.add(0, (int) (remaining % 10));
            remaining /= 10;
        }

        // Reverse order so that 0s are at the end. If 0s are at the front, they'll be dropped.
        digits.sort(Comparator.reverseOrder());
        long result = 0;
        for (int digit : digits) {
            result *= 10;
            result += digit;
        }
        return result;
    }
}
