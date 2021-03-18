package problems;

import lombok.SneakyThrows;
import util.Problem;
import util.ProblemSolution;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Problem 13
 *
 * https://projecteuler.net/problem=13
 *
 * The main part of this problem is dealing with integer values that don't fit in the standard primitive types.
 * However, Java has BigInteger.
 */
public class PE0013 implements Problem {
    private static final String FILE_PATH = "src/main/java/resources/PE0013_numbers";

    @Override
    public ProblemSolution solve() {
        List<BigInteger> numbers = getNumbers();

        BigInteger sum = numbers.stream()
                .reduce(BigInteger.ZERO, BigInteger::add);

        long first10Digits = Long.parseLong(sum.toString().substring(0, 10));
        return ProblemSolution.builder()
                .solution(first10Digits)
                .descriptiveSolution("Sum of numbers: " + sum.toString() + ". First 10 digits: " + first10Digits)
                .build();
    }

    @SneakyThrows
    private static List<BigInteger> getNumbers() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(BigInteger::new)
                .collect(Collectors.toList());
    }
}
