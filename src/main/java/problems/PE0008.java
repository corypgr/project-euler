package problems;

import lombok.SneakyThrows;
import util.SlidingWindowProduct;

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
 *
 * Modified to use the SlidingWindowProduct class.
 */
public class PE0008 {
    private static final String FILE_PATH = "src/main/java/resources/PE0008_number";

    public static void main(String[] args) {
        long[] array = getNumberArray();
        SlidingWindowProduct productUtil = new SlidingWindowProduct();

        System.out.println("Greatest Product: " + productUtil.getBestProductSequence(array, 13));
    }

    @SneakyThrows
    private static long[] getNumberArray() {
        return Files.lines(Paths.get(FILE_PATH))
                .collect(Collectors.joining(""))
                .chars()
                .mapToLong(Character::getNumericValue)
                .toArray();
    }
}
