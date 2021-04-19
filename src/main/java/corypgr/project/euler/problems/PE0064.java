package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.ContinuedFractionUtil;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.stream.IntStream;

/**
 * Problem 64
 *
 * https://projecteuler.net/problem=64
 *
 * This problem was pretty challenging. I can see why I skipped it on my first pass when working on these years ago. To
 * start, I had a hard time following the explanation of the series for this problem. I also don't see an obvious pattern
 * for determining the periods. Found some threads at https://math.stackexchange.com/questions/265690 and
 * https://math.stackexchange.com/questions/980470 that gave me ideas for calculating the period.
 *
 * While I didn't see a pattern for generating the continued fraction, I did see that the end of each continued fraction
 * is 2 * the integer (non-repeating) portion. That gave me a stopping point.
 *
 * Initially, I tried to calculate the double value using basic Java math. At each step subtract the int val and take
 * the reciprocal, storing into a double as you go. You pull out the int portion of the double to generate the list of
 * the repeating fraction. This works ok for smaller numbers, but when we get to long lists of repeating fractions the
 * java math errors stack up and our division starts to give incorrect results. I tried a number of things to get around
 * that, like using BigDecimal with long scales. The error margins didn't improve well enough.
 *
 * Next I thought about what we were doing. We're generating a "fraction" that has the square root and some other
 * numbers. If we hold off on calculating the square root until we need to determine the int portion, we can simplify
 * the fraction well enough to only have a few calculations. Much lower error bar. I looked around for a while to find
 * Java libraries that do this for me... and didn't really find one. There may be something, but the few things I did
 * find (like apache commons Math library) can do rational numbers (ints a,b in the form a / b) but they don't deal with
 * irrational numbers (our square roots) at all.
 *
 * I thought it would be a lot of work to write the fraction math logic myself, but after looking at the patterns that
 * our fractions take, it isn't that hard. I rolled my own solution specifically for this scenario. We subtract the int
 * val from the fraction and then take the reciprocol, only doing our "real" math when we need to pull the int val. This
 * works, and runs very quickly as well.
 *
 * ------
 * Refactored the continued fraction functionality into ContinuedFractionUtil after working on Problem 66.
 */
public class PE0064 implements Problem {
    @Override
    public ProblemSolution solve() {
        ContinuedFractionUtil continuedFractionUtil = new ContinuedFractionUtil();
        long count = IntStream.rangeClosed(1, 10000)
                .mapToObj(continuedFractionUtil::getContinuedFractionOfSquareRoot)
                .filter(v -> v.getRepeatedContinuedPart().size() % 2 == 1)
                .count();

        return ProblemSolution.builder()
                .solution(count)
                .descriptiveSolution("Number of square roots with odd continued parts: " + count)
                .build();
    }
}
