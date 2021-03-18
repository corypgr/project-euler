package problems;

import util.Problem;
import util.ProblemSolution;

/**
 * Problem 2
 *
 * https://projecteuler.net/problem=2
 *
 * Another summation. Not much to discuss here. I'll do the dynamic programming
 * solution, and just keep a sum of all even values we see as we go. I can't
 * think of an easy way to avoid the mod operation to detect even numbers,
 * though if there is a way then that would make a good optimization.
 */
public class PE0002 implements Problem {

    @Override
    public ProblemSolution solve() {
        int previous = 1;
        int current = 2;

        int sum = 0;

        while (current < 4_000_000) {
            if (current % 2 == 0) {
                sum += current;
            }

            int next = previous + current;
            previous = current;
            current = next;
        }

        System.out.println("Sum: " + sum);
        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum: " + sum)
                .build();
    }
}
