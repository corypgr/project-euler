package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;

/**
 * Problem 25
 *
 * https://projecteuler.net/problem=25
 *
 * Another case where the size of the numbers (and arithmetic) is the challenge. I think BigInteger is going to save us
 * again here.
 */
public class PE0025 implements Problem {
    @Override
    public ProblemSolution solve() {
        BigInteger nMinus2 = BigInteger.ONE;
        BigInteger nMinus1 = BigInteger.ONE;
        BigInteger n = nMinus2.add(nMinus1);
        int nIndex = 3;

        while (n.toString().length() < 1000) {
            nMinus2 = nMinus1;
            nMinus1 = n;
            n = nMinus2.add(nMinus1);
            nIndex++;
        }

        return ProblemSolution.builder()
                .solution(nIndex)
                .descriptiveSolution("Index of first term with 1000 digits: " + nIndex)
                .build();
    }
}
