package util;

/**
 * Utility for computing the best product of a sequence of numbers.
 *
 * Examples: problems 8 and 11.
 */
public class SlidingWindowProduct {
    public long getBestProductSequence(long[] array, int sequenceLength) {
        int numZeroes = 0;
        long product = 1;
        long curLargestProduct = -1;
        for (int i = 0; i < array.length; i++) {

            // Multiply current product by the next number in the sequence.
            long newNum = array[i];
            if (newNum == 0L) {
                numZeroes++;
            } else {
                product *= newNum;
            }

            // Divide the current product by the number that fell off the back of the sequence.
            // Only if we've collected the sequence length.
            if (i >= sequenceLength) {
                long oldNum = array[i - sequenceLength];
                if (oldNum == 0L) {
                    numZeroes--;
                } else {
                    product /= oldNum;
                }
            }

            // numZeroes check is because the product would actually be 0 if we weren't ignoring zeroes.
            if (numZeroes == 0 && product > curLargestProduct) {
                curLargestProduct = product;
            }
        }
        return curLargestProduct;
    }
}
