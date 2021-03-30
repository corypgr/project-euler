package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.PermutationUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Problem 24
 *
 * https://projecteuler.net/problem=24
 *
 * Strategy is pretty straightforward. Generate all permutations, sort them, then pick out the millionth one. I'll try
 * to do this the simple way, and revisit if I run into memory or time issues.
 *
 * It is a little slow :) It takes about 2-2.5 seconds to generate the full list of permutations. Then another second to
 * convert and sort those values. I initially had PermutationUtil return a Set, but creating a List is much faster.
 * Experimented with a few other optimizations, but they didn't add up to enough savings:
 *  * Use a custom comparator instead of converting the lists to Numerical values. This was actually slower.
 *  * Convert list values to String instead of long (tried this before the current long conversion).
 */
public class PE0024 implements Problem {
    // Using byte since it is the smallest integer type.
    // List.of(..) thinks they're integers without the cast.
    private static final List<Byte> DIGITS = List.of((byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5,
            (byte) 6, (byte) 7, (byte) 8, (byte) 9);
    private static final int POSITION_TARGET = 1_000_000;

    @Override
    public ProblemSolution solve() {
        PermutationUtil<Byte> permutationUtil = new PermutationUtil<>();
        List<List<Byte>> permutations = permutationUtil.getAllPermutations(DIGITS);

        long solution = permutations.stream()
                .map(this::listToNumericVal)
                .sorted()
                .skip(POSITION_TARGET - 1) // Only process the element we want and avoid creating a new huge list.
                .findFirst()
                .get();

        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("1 millionth lexicographic permutation: " + solution)
                .build();
    }

    private long listToNumericVal(List<Byte> list) {
        long result = 0;
        for (Byte value : list) {
            result *= 10; // shift previous value down
            result += value;
        }
        return result;
    }
}
