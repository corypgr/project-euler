package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Problem 22
 *
 * https://projecteuler.net/problem=22
 *
 * The concept here doesn't seem very hard. Read the file, sort it, and then compute the value for each name. The real
 * question is if this will run fast enough to meet my arbitrary time limit of a few seconds. Turns out it is :)
 */
public class PE0022 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0022_names";

    @Override
    public ProblemSolution solve() {
        List<String> sortedNames = getSortedNames();
        long sum = 0;
        for (int i = 0; i < sortedNames.size(); i++) {
            long nameScore = getAlphabetSum(sortedNames.get(i)) * (i + 1);
            sum += nameScore;
        }

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of all name scores: " + sum)
                .build();
    }

    private long getAlphabetSum(String name) {
            return name.chars()
                    .map(c -> c - 64) // A is char # 65, so for all letters just subtract 64
                    .sum();
    }

    @SneakyThrows
    private List<String> getSortedNames() {
        String fileContents = Files.readString(Paths.get(FILE_PATH));
        return Arrays.stream(fileContents.split(","))
                .map(name -> name.replaceAll("[\" \n]", "")) // remove random quotes, spaces, and newlines
                .map(String::toUpperCase) // Make sure we're only dealing with uppercase
                .sorted()
                .collect(Collectors.toList());
    }
}
