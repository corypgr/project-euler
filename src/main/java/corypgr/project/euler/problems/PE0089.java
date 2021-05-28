package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.RomanNumeral;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Problem 89
 *
 * https://projecteuler.net/problem=89
 *
 * This is kind of a cool problem. The solution seems pretty straightforward. We'll parse each roman numeral string into
 * an integer, then convert it to the minimal form, and compare the string lengths. Most of the work is happening in the
 * RomanNumeral class.
 */
public class PE0089 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0089_romans";

    @Override
    public ProblemSolution solve() {
        List<RomanNumeral> numerals = getRomanNumerals();
        int originalTextCharCount = numerals.stream()
                .map(RomanNumeral::getOriginalString)
                .mapToInt(String::length)
                .sum();
        int minimalTextCharCount = numerals.stream()
                .map(RomanNumeral::getMinimalString)
                .mapToInt(String::length)
                .sum();

        int charsSaved = originalTextCharCount - minimalTextCharCount;
        return ProblemSolution.builder()
                .solution(charsSaved)
                .descriptiveSolution("Characters saved when converting Roman Numerals to their minimal form: " + charsSaved)
                .build();
    }

    @SneakyThrows
    private List<RomanNumeral> getRomanNumerals() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(RomanNumeral::parseString)
                .collect(Collectors.toList());
    }
}
