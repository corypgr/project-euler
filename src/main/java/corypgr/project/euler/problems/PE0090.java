package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.CombinationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Problem 90
 *
 * https://projecteuler.net/problem=90
 *
 * Initially when I saw this problem I had thought it looked pretty challenging, and it's marked as having 40%
 * difficulty. Thinking further on it, I think it may be easier than I had initially assumed.
 *
 * Since order doesn't matter on the cubes, there's 10 choose 6 (without repetition) = 210 cube configurations. That's
 * small. I'm thinking we can generate all cube combinations, and simply test all pairs of those combinations to see if
 * we can generate all squares below 100.
 */
public class PE0090 implements Problem {
    private static final List<Integer> DIGITS = List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    private static final List<Pair> SQUARES = List.of(
            new Pair(0, 1),
            new Pair(0, 4),
            new Pair(0, 9),
            new Pair(1, 6),
            new Pair(2, 5),
            new Pair(3, 6),
            new Pair(4, 9),
            new Pair(6, 4),
            new Pair(8, 1));
    private static final Predicate<Set<Integer>> SIX_OR_NINE_PREDICATE = cube -> cube.contains(6) || cube.contains(9);

    @Override
    public ProblemSolution solve() {
        List<Set<Integer>> cubeCombinations = getCubeCombinations();
        int count = 0;
        for (int i = 0; i < cubeCombinations.size(); i++) {
            Set<Integer> cubeA = cubeCombinations.get(i);

            for (int j = i; j < cubeCombinations.size(); j++) {
                Set<Integer> cubeB = cubeCombinations.get(j);

                if (canRepresentAllSquares(cubeA, cubeB)) {
                    count++;
                }
            }
        }

        return ProblemSolution.builder()
                .solution(count)
                .descriptiveSolution("Number of arrangements of 2 cubes which can represent all squares: " + count)
                .build();
    }

    private boolean canRepresentAllSquares(Set<Integer> cubeA, Set<Integer> cubeB) {
        return SQUARES.stream()
                .allMatch(squarePair -> cubesContainValues(cubeA, cubeB, squarePair.getA(), squarePair.getB()));
    }

    private boolean cubesContainValues(Set<Integer> cubeA, Set<Integer> cubeB,  int val1, int val2) {
        Predicate<Set<Integer>> val1Predicate = getPredicateForVal(val1);
        Predicate<Set<Integer>> val2Predicate = getPredicateForVal(val2);

        return (val1Predicate.test(cubeA) && val2Predicate.test(cubeB)) ||
                (val1Predicate.test(cubeB) && val2Predicate.test(cubeA));
    }

    private Predicate<Set<Integer>> getPredicateForVal(int val) {
        if (val == 6 || val == 9) {
            return SIX_OR_NINE_PREDICATE;
        } else {
            return cube -> cube.contains(val);
        }
    }

    private List<Set<Integer>> getCubeCombinations() {
        CombinationUtil<Integer> combinationUtil = new CombinationUtil<>();
        List<List<Integer>> combinations = combinationUtil.getAllCombinationsWithoutRepeats(DIGITS, 6);

        // Converting to Sets so that we can do simple contains checks on each Set.
        return combinations.stream()
                .map(HashSet::new)
                .collect(Collectors.toList());
    }

    @Value
    private static final class Pair {
        private final int a;
        private final int b;
    }
}
