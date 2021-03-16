package problems;

import java.util.HashSet;

/**
 * Problem 1
 *
 * https://projecteuler.net/problem=1
 *
 * The classic problem of determining the multiples of 3 and 5. The easy
 * solution is to just check every number below 1000 to see if it is divisible
 * by 3 or 5. We can be more efficient by generating all of these numbers
 * directly using addition and handling collisions. Since we're dealing with a
 * pretty small number of elements I think dumping all of the multiples of 3
 * and 5 into a HashSet and then adding the numbers up works well. If we had a
 * particularly large set of numbers to compute we could use a bit vector or
 * some other optimization to avoid the large memory footprint.
 */
public class PE0001 {
    public static void main(String[] args) {
        HashSet<Integer> multiples = new HashSet<Integer>();

        for (int i = 3; i < 1000; i += 3) {
            multiples.add(i);
        }

        for (int i = 5; i < 1000; i += 5) {
            multiples.add(i);
        }

        int sum = multiples.stream()
                .mapToInt(Integer::intValue)
                .sum();

        System.out.println("Sum: " + sum);
    }
}
