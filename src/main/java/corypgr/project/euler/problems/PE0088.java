package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Problem 88
 *
 * https://projecteuler.net/problem=88
 *
 * This seems like a pretty cool problem. This looks sort of like a combinations problem, but we don't know what the
 * subset of valid natural numbers is. Even with that, I think we can do something similar to what we do in
 * CombinationUtil to generate the current sequence. The first thing to recognize is the answer is never going to be all
 * 1s, since the product of all 1s is 1. Next, the answer won't ever be (k - 1) 1s with a single non-1 value (r). The
 * product there is just r, but the sum is r + (k - 1).
 *
 * So, we have to start with (k - 1) 1s and then 2 2s in our set. We increment our last integer until sum == product or
 * product > sum. At that point, we follow a very similar approach to what we're doing in CombinationUtil for rolling
 * over the numbers before the current largest number. We keep track of the smallest product-sum number of all of the
 * candidates found.
 *
 * The above strategy works, but is pretty slow. I was initially recomputing the full sum and product of my values for
 * every combination, and that solution wouldn't even finish. I moved the sums and Products to an array, dynamic
 * programming style, where we update the ith position when we update the ith value in our set. The last element is the
 * full sum or product. That brought things down to ~10 seconds.
 *
 * Trimming things down more, I realized that the last element in the values array (the most iterated over element) can
 * actually be computed directly. If we know the sum (s) and product (p) of the first k - 1 element, then we can solve
 * for the last element (x) in this equation:
 *    p * x = s + x
 *    (p * x) - x = s
 *    (p - 1) * x = s
 *    x = s / (p - 1)
 * If x can be found as a whole integer number, then s + x is one of our candidates for minimal product-sum numbers.
 * Swapping in this optimization brings us under 1 second runtime.
 */
public class PE0088 implements Problem {
    private static final int MAX_K = 12000;

    @Override
    public ProblemSolution solve() {
        int sumOfMinimalProductSumNumbers = IntStream.rangeClosed(2, MAX_K)
                .map(this::getMinimumProductSumNumber)
                .distinct()
                .sum();

        return ProblemSolution.builder()
                .solution(sumOfMinimalProductSumNumbers)
                .descriptiveSolution("Sum of all the minimal product-sum numbers for 2 <= k <= 12000: "
                        + sumOfMinimalProductSumNumbers)
                .build();
    }

    private int getMinimumProductSumNumber(int k) {
        ValueSet valueSet = new ValueSet(k);

        int smallestProductSum = Integer.MAX_VALUE;
        Integer productSum = valueSet.getNextProductSumNumber();
        while (productSum != null) {
            if (productSum < smallestProductSum) {
                smallestProductSum = productSum;
            }
            productSum = valueSet.getNextProductSumNumber();
        }
        return smallestProductSum;
    }

    private static final class ValueSet {
        private int[] values;

        // element at i is values[0] + values[1] + ... + values[i]
        // Last element contains the sum of all in values.
        private int[] sumOfValues;

        // element at i is values[0] * values[1] * ... * values[i]
        // Last element contains the product of all in values.
        private int[] productOfValues;

        private int nextCombinationMaxVal;

        public ValueSet(int k) {
            this.values = new int[k];
            this.sumOfValues = new int[k];
            this.productOfValues = new int[k];
            this.nextCombinationMaxVal = k; // The set of values {1, ... , 1, 2, k} will always result in a product-sum number.

            Arrays.fill(values, 1);
            values[values.length - 1] = 0; // Set to 0 so we will compute the last val when we generate the first combination.
            values[values.length - 2] = 2;

            sumOfValues[0] = values[0];
            productOfValues[0] = values[0];
            for (int i = 1; i < values.length; i++) {
                sumOfValues[i] = sumOfValues[i - 1] + values[i];
                productOfValues[i] = productOfValues[i - 1] * values[i];
            }
        }

        private int getSum() {
            return sumOfValues[sumOfValues.length - 1];
        }

        private int getProduct() {
            return productOfValues[productOfValues.length - 1];
        }

        public Integer getNextProductSumNumber() {
            while (values != null) {
                generateNextCombination();

                if (getProduct() == getSum()) {
                    return getSum();
                }
            }

            return null;
        }

        private void generateNextCombination() {
            if (nextCombinationMaxVal == 1) {
                // No elements in values is < 1, so just return null instead of iterating over the whole array.
                values = null;
                return;
            }

            int lastIndex = values.length - 1;
            int secondLastIndex = lastIndex - 1;

            for (int i = lastIndex; i >= 0; i--) {
                if (values[i] < nextCombinationMaxVal) {
                    int newVal = values[i] + 1;

                    // Special handling for 0 to avoid -1 indexing below.
                    if (i == 0) {
                        values[0] = newVal;
                        sumOfValues[0] = newVal;
                        productOfValues[0] = newVal;
                    }

                    // Fill all elements from incremented spot with the same value, except the last value.
                    for (int j = Math.max(i, 1); j < lastIndex; j++) {
                        values[j] = newVal;
                        sumOfValues[j] = sumOfValues[j - 1] + newVal;
                        productOfValues[j] = productOfValues[j - 1] * newVal;
                    }

                    // lastVal = s / (p - 1) If lastVal is not an int val, then this will make product > sum, and we'll
                    // call back into this method with a new max val.
                    int lastVal = (int) Math.ceil((double) sumOfValues[secondLastIndex]
                            / (productOfValues[secondLastIndex] - 1));

                    values[lastIndex] = lastVal;
                    sumOfValues[lastIndex] = sumOfValues[secondLastIndex] + lastVal;
                    productOfValues[lastIndex] = productOfValues[secondLastIndex] * lastVal;

                    // Current last val is the absolute max with the previous k - 1 values. Next time, it is guaranteed
                    // to be lower than that.
                    nextCombinationMaxVal = lastVal - 1;
                    return;
                }
            }

            // Exhausted all combinations
            values = null;
        }
    }
}
