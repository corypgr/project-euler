package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Problem 81
 *
 * https://projecteuler.net/problem=81
 *
 * Sort of a modification of problems 18 and 67. I'm thinking we can kind of radiate outward. Basically, we start by
 * wrapping every cell in a structure that contains the minimal sum from above and to the left. Then we put the value
 * in the upper left in a queue, and process the queue as follows (for each value):
 *  * Determine the minimal sum for this value given the minimal sums from above and to the left.
 *  * Update the wrapped values to the right and down with the minimal sum from this value.
 *  * Add updated wrapped values to the queue if they aren't already there.
 *
 * We process the queue until it is empty, and use the minimal sum from the last value in the grid.
 * ----------------
 * After I walked away from this I started to think that my other solution, while it worked, was probably more complex
 * than it needed to be. We can do the same basic strategy by overriding the grid values directly to be
 * grid[row][col] + min(grid[row - 1][col], grid[row][col - 1]). Also, instead of using a queue we can just traverse
 * the grid in the normal order (all of row 1, all of row 2, etc..), since that order ensures the desired values for
 * the cell above and to the left of the current cell is always set.
 */
public class PE0081 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0081_grid";

    @Override
    public ProblemSolution solve() {
        int[][] grid = getGrid();

        // Some special processing for the first row and col to avoid handling out of bounds edge cases.
        // The first cell (grid[0][0]) doesn't change. It just seeds the rest of the cells.
        // The minimal path sum for the first row and col is just the cell + the cell behind it.
        for (int col = 1; col < grid[0].length; col++) {
            grid[0][col] += grid[0][col - 1];
        }
        for (int row = 1; row < grid.length; row++) {
            grid[row][0] += grid[row - 1][0];
        }

        // Process remaining rows and columns.
        for (int row = 1; row < grid.length; row++) {
            for (int col = 1; col < grid[row].length; col++) {
                grid[row][col] += Math.min(grid[row - 1][col], grid[row][col - 1]);
            }
        }

        int solution = grid[grid.length - 1][grid[0].length - 1];
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Minimal Path Sum: " + solution)
                .build();
    }

    @SneakyThrows
    private int[][] getGrid() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(line -> line.split(","))
                .map(this::stringsToInts)
                .toArray(int[][]::new);
    }

    private int[] stringsToInts(String[] strings) {
        return Arrays.stream(strings)
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
