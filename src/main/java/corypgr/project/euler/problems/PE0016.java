package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Problem 16
 *
 * https://projecteuler.net/problem=16
 *
 * Looks like another case where the size of the number is larger than can fit in a primitive. I'm curious if the
 * desired solution is to write out a custom arithmetic function with something like a bit vector. I'll go with the
 * simpler option of using BigInteger again.
 */
public class PE0016 implements Problem {
    @Override
    public ProblemSolution solve() {
        BigInteger bigNum = BigInteger.TWO.pow(1000);

        int solution = bigNum.toString().chars()
                .map(Character::getNumericValue)
                .sum();
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Sum of digits: " + solution)
                .build();
    }
}
