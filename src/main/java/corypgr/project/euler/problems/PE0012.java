package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.ProperDivisors;

/**
 * Problem 12
 *
 * https://projecteuler.net/problem=12
 *
 * Interesting problem. There are a few notable things here:
 * 1. We are dealing with "divisors", not prime divisors, so the divisor logic can't be simplified too much using
 *  primes.
 * 2. You don't need to calculate all of the previous triangle numbers to determine the nth triangle number. Reusing
 *  sequential sum trick from before, the nth triangle number = (n + 1) * (n / 2).
 *  Ex: 7th triangle number = (7 + 1) * (7 / 2) = 8 * 3.5 = 28. Watch out for the decimals here.
 * 3. I was thinking we could use an exponential increase to find a triangle number with more than 500 divisors, but
 *  number of divisors aren't consistently increasing. Therefore, we have to check every triangle number > 500 to see if
 *  it has more than 500 divisors.
 *
 * Even though we can calculate a triangle number using a formula, since we basically have to check all triangle numbers
 * anyway, we can just use addition: the nth triangle number = (n - 1)th triangle number + n.
 *
 * This turned out to be pretty tricky. I was calculating the divisors far too slowly initially, and thought I might be
 * able to leverage the minimal factor sets for all numbers below 500 instead (See problem 5). The product of all of
 * those factors is not the right number.
 *
 * Instead, I ended up just optimizing my getNumDivisors() method. A single divisor is part of a pair of divisors for a
 * number. Take 8 as an example. Its divisors are 1, 2, 4, 8. That is, 1 * 8 and 2 * 4. We can leverage this to
 * drastically reduce the search space. Half of all of a number's divisors are less than the square root of that number,
 * and the other half are above the square root. We know that for every divisor less than the square root there is
 * exactly one above it, so we only have to check the number below the square root and double count them to get the real
 * divisor count. The only exception here is if the divisor == sqrt(value). In that case we only count the divisor once
 * since the "pair" here is the same divisor.
 */
public class PE0012 implements Problem {
    @Override
    public ProblemSolution solve() {
        ProperDivisors divisorsUtil = new ProperDivisors();

        long curTriangleNumber = 3;
        long curTriangleNumberIndex = 2;
        while (divisorsUtil.getNumDivisors(curTriangleNumber) <= 500) {
            curTriangleNumberIndex++;
            curTriangleNumber += curTriangleNumberIndex;
        }

        return ProblemSolution.builder()
                .solution(curTriangleNumber)
                .descriptiveSolution("Triangle Number with 500 or more divisors: " + curTriangleNumber)
                .build();
    }
}
