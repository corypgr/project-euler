package problems;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Problem 8
 *
 * https://projecteuler.net/problem=8
 *
 * Naive solution is to calculate the product separately for each 13 digits in the number. If we keep track of zeroes,
 * and don't include them in the calculations, then we can do a sliding window strategy.
 */
public class PE0008 {
    private static final String FILE_PATH = "src/main/java/resources/PE0008_number";

    public static void main(String[] args) {
        String bigNumber = getNumberString();
        int numZeroes = 0;
        long product = 1;
        long curLargestProduct = -1;
        for (int i = 0; i < bigNumber.length(); i++) {

            // Multiply current product by the next number in the sequence.
            char newCharacter = bigNumber.charAt(i);
            if (newCharacter == '0') {
                numZeroes++;
            } else {
                product *= Character.getNumericValue(newCharacter);
            }

            // Divide the current product by the number that fell off the back of the sequence.
            // Only if we've collected the first 13 values.
            if (i >= 13) {
                char oldCharacter = bigNumber.charAt(i - 13);
                if (oldCharacter == '0') {
                    numZeroes--;
                } else {
                    product /= Character.getNumericValue(oldCharacter);
                }
            }

            // numZeroes check is because the product would actually be 0 if we weren't ignoring zeroes.
            if (numZeroes == 0 && product > curLargestProduct) {
                curLargestProduct = product;
            }
        }

        System.out.println("Greatest Product: " + curLargestProduct);
    }

    @SneakyThrows
    private static String getNumberString() {
        return Files.lines(Paths.get(FILE_PATH))
                .collect(Collectors.joining(""));
    }
}
