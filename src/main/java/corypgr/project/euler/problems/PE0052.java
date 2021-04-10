package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.CountMap;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Problem 52
 *
 * https://projecteuler.net/problem=52
 *
 * Seems pretty straightforward. Starting at 1, for each number generate the six multiples, then check if they're all
 * permutations of each other.
 *
 * There's something of an optimations we can make here. We know that the first digit of our number must be 1, since 6*2
 * is 12 and would therefore increase our number of digits. We can expand that further and say that our numbers can only
 * be up to 16.(6) % of the next multiple of 10 before the multiplication by 6 increases the number to more digits. We
 * can build our iteration around this idea.
 */
public class PE0052 implements Problem {
    private static final double MAX_VAL_MULTIPLIER = 1.0 / 6;

    @Override
    public ProblemSolution solve() {
        int solution = findSmallestIntWithMultiplePermutations();
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Smallest number whose first 6 multiples all contain the same digits: " + solution)
                .build();
    }

    private int findSmallestIntWithMultiplePermutations() {
        // Start with upper level. We're operating within each level.
        for (int level = 10; ; level *= 10) {
            int maxWithinLevel = (int) (level * MAX_VAL_MULTIPLIER);
            int minWithinLevel = level / 10;
            Optional<Integer> result = IntStream.rangeClosed(minWithinLevel, maxWithinLevel)
                    .mapToObj(this::getMultiples)
                    .filter(this::allMultiplesShareDigits)
                    .map(multiples -> multiples.get(0)) // First value is the integer itself.
                    .findFirst();

            if (result.isPresent()) {
                return result.get();
            }
        }
    }

    private List<Integer> getMultiples(int val) {
        List<Integer> result = new LinkedList<>();
        for (int adder = val; result.size() < 6; adder += val) {
            result.add(adder);
        }
        return result;
    }

    private boolean allMultiplesShareDigits(List<Integer> multiples) {
        CountMap<Integer> firstMap = intToCountMap(multiples.get(0));
        for (int i = 1; i < multiples.size(); i++) {
            CountMap<Integer> map = intToCountMap(multiples.get(i));
            if (!map.equals(firstMap)) {
                return false;
            }
        }
        return true;
    }

    private CountMap<Integer> intToCountMap(int val) {
        CountMap<Integer> list = new CountMap<>();
        int remaining = val;
        while (remaining > 0L) {
            list.addCount(remaining % 10);
            remaining /= 10;
        }
        return list;
    }
}
