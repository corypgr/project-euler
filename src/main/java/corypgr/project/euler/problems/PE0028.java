package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Value;

/**
 * Problem 28
 *
 * https://projecteuler.net/problem=28
 *
 * Interesting problem. I'll try to create a pattern for determining the direction to fill out the grid then find the
 * diagonal sums the lazy way.
 */
public class PE0028 implements Problem {
    private static final int MAX = 1001;

    @Override
    public ProblemSolution solve() {
        int startRow = MAX / 2;
        int startCol = MAX / 2;
        int[][] grid = new int[MAX][MAX];
        int index = 1;

        grid[startRow][startCol] = index; // seed the grid with the first number to make our getMove calculation easier.
        index++;

        // Fill Grid.
        Move move = new Move(startRow, startCol + 1, Direction.RIGHT);
        int maxIndex = MAX * MAX;
        while (index <= maxIndex) {
            grid[move.getRow()][move.getCol()] = index;
            index++;
            move = getMove(grid, move);
        }

        // Calculate sums on Diagonals.
        long sum = 0;
        for (int i = 0; i < MAX; i++) {
            sum += grid[i][i]; // downward diagonal.
            sum += grid[MAX - i - 1][i]; // upward diagonal
        }
        sum -= 1; // We double count the middle square, which is always 1.

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of the diagonals of the spiral grid: " + sum)
                .build();
    }

    /**
     * Operates on the concept that when you're going in one direction then the cell next to you on the inside should be
     * empty, otherwise you change direction. For example, in the 5x5 grid:
     *   0 0 0 0 0    0 0 0 0 0    0 0 0 0 0    0 0 0 0 0
     *   0 0 0 0 0    0 0 0 0 0    0 0 0 0 0    0 0 0 0 0
     *   0 0 1 2 0 -> 0 0 1 2 0 -> 0 0 1 2 0 -> 0 0 1 2 0
     *   0 0 0 0 0    0 0 0 3 0    0 0 4 3 0    0 5 4 3 0
     *   0 0 0 0 0    0 0 0 0 0    0 0 0 0 0    0 0 0 0 0
     *
     * Here we just placed the 2. Since the space below that is 0 we know that we should turn down. After placing the 3
     * below the 2, we are going left. Now we can see there is a number (2) above the 3 so we keep going left. After we
     * place the 5 we see the space above 5 is 0 so we change direction again.
     */
    private Move getMove(int[][] grid, Move lastMove) {
        if (lastMove.getDirection() == Direction.RIGHT && grid[lastMove.getRow() + 1][lastMove.getCol()] == 0) {
            return new Move(lastMove.getRow() + 1, lastMove.getCol(), Direction.DOWN);
        } else if (lastMove.getDirection() == Direction.RIGHT) {
            return new Move(lastMove.getRow(), lastMove.getCol() + 1, Direction.RIGHT);
        } else if (lastMove.getDirection() == Direction.DOWN && grid[lastMove.getRow()][lastMove.getCol() - 1] == 0) {
            return new Move(lastMove.getRow(), lastMove.getCol() - 1, Direction.LEFT);
        } else if (lastMove.getDirection() == Direction.DOWN) {
            return new Move(lastMove.getRow() + 1, lastMove.getCol(), Direction.DOWN);
        } else if (lastMove.getDirection() == Direction.LEFT && grid[lastMove.getRow() - 1][lastMove.getCol()] == 0) {
            return new Move(lastMove.getRow() - 1, lastMove.getCol(), Direction.UP);
        } else if (lastMove.getDirection() == Direction.LEFT) {
            return new Move(lastMove.getRow(), lastMove.getCol() - 1, Direction.LEFT);
        } else if (lastMove.getDirection() == Direction.UP && grid[lastMove.getRow()][lastMove.getCol() + 1] == 0) {
            return new Move(lastMove.getRow(), lastMove.getCol() + 1, Direction.RIGHT);
        } else { // direction up
            return new Move(lastMove.getRow() - 1, lastMove.getCol(), Direction.UP);
        }
    }

    @Value
    private static class Move {
        private final int row;
        private final int col;
        private final Direction direction;
    }

    private enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }
}
