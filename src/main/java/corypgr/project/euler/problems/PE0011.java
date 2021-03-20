package corypgr.project.euler.problems;

import lombok.SneakyThrows;
import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.SlidingWindowProduct;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.LongStream;

/**
 * Problem 11
 *
 * https://projecteuler.net/problem=11
 *
 * This is similar to problem 8, but now we're dealing with a 2D array instead of a single numeric string. I'm thinking
 * we use the same strategy, but have it traverse the array in multiple ways.
 */
public class PE0011 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0011_grid";

    public ProblemSolution solve() {
        long[][] grid = getNumberGrid();
        
        long bestRowProduct = getBestRowProduct(grid);
        long bestColumnProduct = getBestColumnProduct(grid);
        long bestUpDiagonalProduct = getBestUpDiagonalProduct(grid);
        long bestDownDiagonalProduct = getBestDownDiagonalProduct(grid);
        
        long bestVal = LongStream.of(bestRowProduct, bestColumnProduct, bestUpDiagonalProduct, bestDownDiagonalProduct)
                .max()
                .getAsLong();

        return ProblemSolution.builder()
                .solution(bestVal)
                .descriptiveSolution("Best Product: " + bestVal)
                .build();
    }

    private static long getBestRowProduct(long[][] grid) {
        SlidingWindowProduct productUtil = new SlidingWindowProduct();

        long curLargestProduct = -1;
        for (int row = 0; row < grid.length; row++) {
            long bestRowProduct = productUtil.getBestProductSequence(grid[row], 4);
            if (bestRowProduct > curLargestProduct) {
                curLargestProduct = bestRowProduct;
            }
        }
        return curLargestProduct;
    }

    /**
     * Turn each column into rows and reuse getBestRowProduct.
     */
    private static long getBestColumnProduct(long[][] grid) {
        long[][] newLines = new long[grid[0].length][];
        for (int col = 0; col < grid[0].length; col++) {
            long[] colLine = new long[grid[0].length];
            for (int row = 0; row < grid.length; row++) {
                colLine[row] = grid[row][col];
            }
            newLines[col] = colLine;
        }

        return getBestRowProduct(newLines);
    }

    /**
     * Turn each up diagonal into rows and reuse getBestRowProduct.
     */
    private static long getBestUpDiagonalProduct(long[][] grid) {
        long[][] newLines = new long[(grid.length * 2) - 1][]; // One diagonal is in both parts. We skip it the second time.
        int newLineIndex = 0;

        for (int row = 0; row < grid.length; row++) {
            int diagRow = row;
            int diagCol = 0;
            long[] diagLine = new long[row + 1]; // line is the row index + 1 since that is how long the diagonal is.
            for (; diagCol <= row; diagCol++,diagRow--) {
                diagLine[diagCol] = grid[diagRow][diagCol];
            }
            newLines[newLineIndex] = diagLine;
            newLineIndex++;
        }

        for (int row = grid.length - 1; row > 0; row--) { // Stop before 0 since that long diagonal is taken care of above.
            int diagRow = row;
            int diagCol = grid[row].length - 1;
            long[] diagLine = new long[grid.length - row]; // line is the -1 * row index + 20 since that is how long the diagonal is.
            for (; diagRow < grid.length; diagCol--,diagRow++) {
                diagLine[grid[0].length - 1 - diagCol] = grid[diagRow][diagCol];
            }
            newLines[newLineIndex] = diagLine;
            newLineIndex++;
        }

        return getBestRowProduct(newLines);
    }

    /**
     * Turn each down diagonal into rows and reuse getBestRowDiagonal.
     */
    private static long getBestDownDiagonalProduct(long[][] grid) {
        long[][] newLines = new long[39][]; // 39 diagonals in this direction.
        int newLineIndex = 0;

        for (int row = 0; row < grid.length; row++) {
            int diagRow = row;
            int diagCol = 0;
            long[] diagLine = new long[grid.length - row]; // line is the row index + 1 since that is how long the diagonal is.
            for (; diagRow < grid.length; diagCol++,diagRow++) {
                diagLine[diagCol] = grid[diagRow][diagCol];
            }
            newLines[newLineIndex] = diagLine;
            newLineIndex++;
        }

        for (int row = 0; row < grid.length - 1; row++) { // Stop before 0 since that long diagonal is taken care of above.
            int diagRow = row;
            int diagCol = grid[row].length - 1;
            long[] diagLine = new long[row + 1]; // line is the row index + 1 since that is how long the diagonal is.
            for (; diagRow >= 0; diagCol--,diagRow--) {
                diagLine[grid[0].length - 1 - diagCol] = grid[diagRow][diagCol];
            }
            newLines[newLineIndex] = diagLine;
            newLineIndex++;
        }

        return getBestRowProduct(newLines);
    }

    @SneakyThrows
    private static long[][] getNumberGrid() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(PE0011::parseNumberLine)
                .toArray(long[][]::new);
    }

    private static long[] parseNumberLine(String line) {
        return Arrays.stream(line.split(" "))
                .mapToLong(Long::parseLong)
                .toArray();
    }
}
