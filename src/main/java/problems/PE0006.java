package problems;

/**
 * Problem 6
 *
 * https://projecteuler.net/problem=6
 *
 * Very straightforward. I remember there is a minor optimization to avoid the addition for the square of the sum. The
 * sum of a list of integers (increasing by 1) is: ((first + end) * (size / 2)). I'll apply this, but it won't affect
 * runtime significantly.
 */
public class PE0006 {
    private static final long MAX_VAL = 100L;

    public static void main(String[] args) {
        long sumOfSquares = getSumOfSquares();
        long squareOfSum = getSquareOfSum();

        System.out.println("Difference: " + (squareOfSum - sumOfSquares));
    }

    private static long getSumOfSquares() {
         long sum = 0;
         for (int i = 1; i <= MAX_VAL; i++) {
             sum += i * i;
         }
         return sum;
    }

    private static long getSquareOfSum() {
        long sum = (MAX_VAL + 1) * (MAX_VAL / 2);
        return sum * sum;
    }
}
