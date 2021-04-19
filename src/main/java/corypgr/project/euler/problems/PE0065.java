package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.ContinuedFractionUtil;
import corypgr.project.euler.problems.util.ContinuedFractionUtil.RationalFraction;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Problem 65
 *
 * https://projecteuler.net/problem=65
 *
 * Another of these infinite fraction questions. I think there's a bit of a typo in the example for sqrt(2). There
 * should be a comma after the result of each step (i.e. after 3/2 and 7/5), though maybe it's an issue with my browser.
 * We're shown how to calculate the each convergent from the infinite continued fraction, and we're given the infinite
 * continued fraction for e. We can use this to calculate the 100th convergent somewhat directly. Could do a recursive
 * solution of creating the fractions and simplifying it down.
 *
 * That seems pretty complicated though. Looking at the terms in the sequence I was convinced there is a pattern, at
 * least for the numerator. You can generate 2 out of every 3 numerators by just adding the previous 2 terms. The jump
 * is the confusing bit (from 19 to 87 and 193 to 1264). We can actually use the infinite continued fraction here. The
 * pattern is that the continued fraction tells you the multipliers of the previous numbers to use when computing the
 * next number. Probably best to show with an example:
 * 2 (start number. Index -1 our "whole part")
 * 3 (start number. Index 0 of the continued fraction. Maps to the 1 in the continued fraction, but that isn't helpful)
 * 8 (Index 1. Use current index as a multiplier of the previous term, plus the term before that) = 2 + (2 * 3)
 * 11 (Index 2) = 3 + (1 * 8)
 * 19 (Index 3) = 8 + (1 * 11)
 * 87 (Index 4) = 11 + (4 * 19)
 *
 * The same pattern holds for the denominators as well.
 *
 * Writing out the formula is interesting. It would be something like:
 * for c(n) = the value in the continued fraction, f(n) = c(n) * f(n - 1) + f(n - 2).
 *
 * We can calculate all of the numerators using this up to the 100th convergent.
 *
 * -------
 * Refactored this a bit after working on Problem 66. We now generate the full convergents (numerator and denominator)
 * and pull out the numerator value for the solution.
 */
public class PE0065 implements Problem {
    private static final int CONVERGENT_NUMBER_POSITION = 100;

    @Override
    public ProblemSolution solve() {
        RationalFraction convergentFraction = getTargetConvergentFraction();
        int sum = getSumOfDigits(convergentFraction.getNumerator());

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of the digits of the 100th convergent numerator of e: " + sum)
                .build();
    }

    private RationalFraction getTargetConvergentFraction() {
        ContinuedFractionUtil continuedFractionUtil = new ContinuedFractionUtil();
        Iterator<RationalFraction> convergentIterator =
                continuedFractionUtil.getConvergentsUsingContinuedFractionVals(2, getContinuedFractionIterator());

        // Uses an infinite stream while the iterator isn't infinite. Since we know when the iterator ends, this is ok.
        return Stream.generate(convergentIterator::next)
                .skip(CONVERGENT_NUMBER_POSITION - 1)
                .findFirst()
                .get();
    }

    private Iterator<Integer> getContinuedFractionIterator() {
        int[] continuedFraction = new int[CONVERGENT_NUMBER_POSITION];

        // Start by filling with all 1s. We'll replace every third element next.
        for (int i = 0; i < continuedFraction.length; i++) {
            continuedFraction[i] = 1;
        }

        for (int i = 1, num = 2; i < continuedFraction.length; i += 3, num += 2) {
            continuedFraction[i] = num;
        }
        return Arrays.stream(continuedFraction).iterator();
    }

    private int getSumOfDigits(BigInteger num) {
        return num.toString().chars()
                .map(Character::getNumericValue)
                .sum();
    }
}
