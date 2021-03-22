package corypgr.project.euler.problems.util;

import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Helper mostly for problems 18 and 67.
 *
 * Basic strategy for this is to calculate the numbers from the bottom up. Given two adjacent cells, choose the larger
 * and add it to the cell that would be above both of those cells. This will put the sum of the best route in
 * cell (0, 0). Ex with most basic triangle:
 *     03
 *   07  04
 * 02  04  06
 *
 * After the pass on the bottom row, this becomes:
 *     03
 *   11  10
 * 02  04  06
 *
 * After the pass on the second row, this becomes:
 *     14
 *   11  10
 * 02  04  06
 * where the top element is the max sum path.
 */
public class TriangleMaxSumRoute {
    /**
     * Parse the input file and calculate the longest path.
     */
    public long findMaxSumRoute(Path inputFilePath) {
        int[][] triangleGrid = parseFile(inputFilePath);

        // Don't need to process the top row. That will have a single cell and contains our solution.
        for (int row = triangleGrid.length - 1; row > 0; row--) {

            // Don't need to process the last element in the loop. It is included in the comparisons.
            for (int col = 0; col < triangleGrid[row].length - 1; col++) {
                int maxBetweenAdjacentVals = Math.max(triangleGrid[row][col], triangleGrid[row][col + 1]);
                // The actual bubbling up of the value.
                triangleGrid[row - 1][col] += maxBetweenAdjacentVals;
            }
        }

        return triangleGrid[0][0];
    }

    @SneakyThrows
    private int[][] parseFile(Path inputFilePath) {
        return Files.lines(inputFilePath)
                .map(this::parseLine)
                .toArray(int[][]::new);
    }

    private int[] parseLine(String line) {
        return Arrays.stream(line.split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
