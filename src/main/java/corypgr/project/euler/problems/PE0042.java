package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Problem 42
 *
 * https://projecteuler.net/problem=42
 *
 * Seems pretty similar to problem 22, as least for the parsing and word value part. Will largely copy the strategy
 * used in problem 22 for that. I'll calculate all of the word sums, determine the max, then find all triangle numbers
 * up to that max. Last we check which words have a sum equal to a triangle number and count the results.
 *
 * Note I won't split the parsing loging out into a separate class, shared by problems 22 and 42, since it is a pretty
 * specialized format and has a very narrow use. Similarly, I won't split out the getAlphabetSum(..) logic because it
 * is very small. DRY has its limits.
 */
public class PE0042 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0042_words";

    @Override
    public ProblemSolution solve() {
        Set<String> words = getWords();
        List<Integer> wordAlphabetSums = words.stream()
                .map(this::getAlphabetSum)
                .collect(Collectors.toList()); // List so that we don't drop duplicates.

        int maxAlphabetSum = wordAlphabetSums.stream()
                .mapToInt(Integer::intValue)
                .max()
                .getAsInt();

        Set<Integer> triangleNumbers = getTriangleNumbers(maxAlphabetSum);

        long countOfTriangleWords = wordAlphabetSums.stream()
                .filter(triangleNumbers::contains)
                .count();

        return ProblemSolution.builder()
                .solution(countOfTriangleWords)
                .descriptiveSolution("Number of triangle Words: " + countOfTriangleWords)
                .build();
    }

    private Set<Integer> getTriangleNumbers(int maxVal) {
        return Stream.iterate(1, i -> i + 1)
                .map(this::calculateTriangleNum)
                .takeWhile(triangleNum -> triangleNum <= maxVal)
                .collect(Collectors.toSet());
    }

    private int calculateTriangleNum(int index) {
        // Integer division works fine here since the initial multiplication guarantees that it is dividing
        // from an even number.
        return (index * (index + 1)) / 2;
    }

    private int getAlphabetSum(String name) {
        return name.chars()
                .map(c -> c - 64) // A is char # 65, so for all letters just subtract 64
                .sum();
    }

    @SneakyThrows
    private Set<String> getWords() {
        String fileContents = Files.readString(Paths.get(FILE_PATH));
        return Arrays.stream(fileContents.split(","))
                .map(name -> name.replaceAll("[\" \n]", "")) // remove random quotes, spaces, and newlines
                .map(String::toUpperCase) // Make sure we're only dealing with uppercase
                .collect(Collectors.toSet());
    }
}
