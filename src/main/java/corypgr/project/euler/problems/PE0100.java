package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Problem 100
 *
 * https://projecteuler.net/problem=100
 *
 * The last problem (for me). Looks like a pretty interesting problem too.
 *
 * I think we can do a sort of binary search to find our solution. We can safely assume that the Blue disc count will be
 * more than 50% of the total. We can run a binary search on the range (size / 2, size), calculating the probability of
 * taking 2 discs at each point. If we run out of numbers without finding the 50% exact probability, then we increment
 * the size and start over.
 *
 * The binary search approach worked, but it was too slow. The program didn't find a correct solution after running
 * for several minutes. This was partly because I had to switch to BigInteger, but mostly because the actual solution
 * is a huge number. I got a bit smarter. What we're trying to do here is solve for 1 / 2, where b = blue discs and
 * s = total discs: (b / s) * ((b - 1) / (s - 1) = 1 / 2. We can reduce this:
 * b * (b - 1) / s * (s - 1) = 1 / 2
 * b^2 - b = (s^2 - s) / 2
 * b^2 - b - (s^2 - s) / 2 = 0
 *
 * If we know s, we can solve for b by using the quadratic formula:
 * (-b + sqrt(b^2 - 4 * a * c)) / (2 * a) with a = 1, b = -1, c = -(s^2 - s) / 2
 * (1 + sqrt(1 + 2 * (s^2 - s)) / 2
 *
 * Using this, I could run a single calculation to find the blue disc count. If we didn't get an int value, then it
 * wasn't the correct solution. Further, since the sqrt was always around an odd number (1 + 2 * x), the result of
 * 1 + sqrt(x) would be even and we would have a valid solution. So, if our sqrt was an int value, we had a valid
 * solution.
 *
 * Swapping this in, but still incrementing the total size by 1, was still too slow... Next I was curious if there was
 * a pattern. I could quickly calculate the first several values using my new calculation:
 * b / s
 * 1 / 1  // This didn't seem quite right, but I left it as an edge case.
 * 3 / 4
 * 15 / 21
 * 85 / 120
 * 493 / 697
 * 2871 / 4060
 * ...
 *
 * There is a pattern for s!!! s(n) = 6 * s(n - 1) - s(n - 2) - 2. Calculating s until we find one over 10^12, we can
 * find b by using our formula above.
 */
public class PE0100 implements Problem {
    private static final long TARGET_SIZE = 1_000_000_000_000L;

    @Override
    public ProblemSolution solve() {
        List<Long> sizes = new ArrayList<>();
        sizes.add(1L);
        sizes.add(4L);
        for (int i = 2; sizes.get(i - 1) < TARGET_SIZE; i++) {
            long next = sizes.get(i - 1) * 6 - sizes.get(i - 2) - 2;
            sizes.add(next);
        }
        long lastSize = sizes.get(sizes.size() - 1);
        long blueDiscCount = getBlueDiscCount(lastSize);

        return ProblemSolution.builder()
                .solution(blueDiscCount)
                .descriptiveSolution("Number of blue discs for a box over 10^12 to have 50% probability of getting" +
                        " 2 blue discs: " + blueDiscCount)
                .build();
    }

    private long getBlueDiscCount(long size) {
        BigInteger bigSize = BigInteger.valueOf(size);
        BigInteger valInsideRoot = BigInteger.ONE.add(BigInteger.TWO.multiply(bigSize.pow(2).subtract(bigSize)));
        BigInteger[] quadraticRoot = valInsideRoot.sqrtAndRemainder();

        // Check that we get an integer square root.
        if (quadraticRoot[1].equals(BigInteger.ZERO)) {
            return quadraticRoot[0].add(BigInteger.ONE).divide(BigInteger.TWO).longValueExact();
        }

        throw new IllegalArgumentException("Input size does not have an exact number of blue discs to get 50%.");
    }
}
