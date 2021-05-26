package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Problem 99
 *
 * https://projecteuler.net/problem=99
 *
 * Interesting problem. I tried calculating the numbers directly, but as I expected it took too long.
 *
 * My first thought is we should try to factor each base into f1^(exponent * n1) * f2^(exponent * n2) * ... where f* is
 * a prime factor and n* is the number of times that prime factor divides out of the base. We could then try to compare
 * the resulting exponents instead, but I don't know how to compare multiple factors against multiple factors in this
 * case...
 *
 * After I started researching the problem a little bit, I found someone suggesting you can compare two large numbers by
 * comparing log_2(base) * exponent. That sort of directly gave me the answer, so I feel like I cheated a bit, but
 * it makes sense. It's actually the "correct" way to do what I was thinking along the lines of factoring the base.
 *
 * For b^y = x, log_b(x) = y. Also (b^y)^z = b^(y*z). If we want to change all of our numbers to have a base 2, we can
 * see that base = 2^log_2(base). So base^exponent = (2^log_2(base))^exponent = 2^(log_2(base) * exponent). If all
 * numbers have 2 as their new base, we can skip calculating the full result and just compare log_2(base) * exponent.
 */
public class PE0099 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0099_powers";

    @Override
    public ProblemSolution solve() {
        Power[] powers = getPowers();
        double[] logResults = Arrays.stream(powers)
                .mapToDouble(power -> power.getExponent() * Math.log(power.getBase()))
                .toArray();

        double largest = -1;
        int largestIndex = -1;
        for (int i = 0; i < logResults.length; i++) {
            if (logResults[i] > largest) {
                largest = logResults[i];
                largestIndex = i;
            }
        }

        int solution = largestIndex + 1; // Account for zero indexing.
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("The line with the largest number: " + solution)
                .build();
    }

    @SneakyThrows
    private Power[] getPowers() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(this::lineToPower)
                .toArray(Power[]::new);
    }

    private Power lineToPower(String line) {
        String[] parts = line.split(",");
        return Power.builder()
                .base(Integer.parseInt(parts[0]))
                .exponent(Integer.parseInt(parts[1]))
                .build();
    }

    @Value
    @Builder
    private static final class Power {
        private final int base;
        private final int exponent;
    }
}
