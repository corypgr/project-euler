package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;

/**
 * Problem 15
 *
 * https://projecteuler.net/problem=15
 *
 * I feel like there's probably a purely math-based way to do this, but I can't think of what it is. Instead, I'll do a
 * sort of dynamic programming solution. The problem is stated as using a 20x20 grid, but the positions where a choice
 * needs to be made actually make up a 21x21 grid. Using a 21x21 grid, we'll compute the number of routes leaving a
 * specific spot, computing backwards from the last cell.
 */
public class PE0015 implements Problem {

    @Override
    public ProblemSolution solve() {
        long[][] grid = new long[21][21];
        grid[20][20] = 1; // the very last cell is the end. Count as 1 route.

        long solution = getNumberRoutesForCell(0, 0, grid);
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Number of routes: " + solution)
                .build();
    }

    private long getNumberRoutesForCell(int row, int col, long[][] grid) {
        if (row >= grid.length || col >= grid[0].length) {
            return 0; // Hitting the edge of the grid. No routes this way.
        }

        if (grid[row][col] > 0) {
            return grid[row][col];
        }

        long rightNumberRoutes = getNumberRoutesForCell(row, col + 1, grid);
        long downNumberRoutes = getNumberRoutesForCell(row + 1, col, grid);

        // Only care about total number of complete routes, so only count the valid routes out of the cell without
        // counting the cell itself.
        grid[row][col] = rightNumberRoutes + downNumberRoutes;
        return grid[row][col];
    }
}
