package corypgr.project.euler.problems.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class SudokuSolverTest {
    private static final int[][] EASY_BOARD = {
            {0, 0, 3, 0, 2, 0, 6, 0, 0},
            {9, 0, 0, 3, 0, 5, 0, 0, 1},
            {0, 0, 1, 8, 0, 6, 4, 0, 0},
            {0, 0, 8, 1, 0, 2, 9, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 6, 7, 0, 8, 2, 0, 0},
            {0, 0, 2, 6, 0, 9, 5, 0, 0},
            {8, 0, 0, 2, 0, 3, 0, 0, 9},
            {0, 0, 5, 0, 1, 0, 3, 0, 0}
    };
    private static final int[][] HARD_BOARD = {
            {0, 4, 3, 0, 8, 0, 2, 5, 0},
            {6, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 1, 0, 9, 4},
            {9, 0, 0, 0, 0, 4, 0, 7, 0},
            {0, 0, 0, 6, 0, 8, 0, 0, 0},
            {0, 1, 0, 2, 0, 0, 0, 0, 3},
            {8, 2, 0, 5, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 5},
            {0, 3, 4, 0, 9, 0, 7, 1, 0}
    };

    private static final int[][] EASY_BOARD_SOLUTION = {
            {4, 8, 3, 9, 2, 1, 6, 5, 7},
            {9, 6, 7, 3, 4, 5, 8, 2, 1},
            {2, 5, 1, 8, 7, 6, 4, 9, 3},
            {5, 4, 8, 1, 3, 2, 9, 7, 6},
            {7, 2, 9, 5, 6, 4, 1, 3, 8},
            {1, 3, 6, 7, 9, 8, 2, 4, 5},
            {3, 7, 2, 6, 8, 9, 5, 1, 4},
            {8, 1, 4, 2, 5, 3, 7, 6, 9},
            {6, 9, 5, 4, 1, 7, 3, 8, 2}
    };

    private static final int[][] HARD_BOARD_SOLUTION = {
            {1, 4, 3, 9, 8, 6, 2, 5, 7},
            {6, 7, 9, 4, 2, 5, 3, 8, 1},
            {2, 8, 5, 7, 3, 1, 6, 9, 4},
            {9, 6, 2, 3, 5, 4, 1, 7, 8},
            {3, 5, 7, 6, 1, 8, 9, 4, 2},
            {4, 1, 8, 2, 7, 9, 5, 6, 3},
            {8, 2, 1, 5, 6, 7, 4, 3, 9},
            {7, 9, 6, 1, 4, 3, 8, 2, 5},
            {5, 3, 4, 8, 9, 2, 7, 1, 6}
    };

    private static final int[][] UNSOLVABLE_BOARD = {
            {0, 0, 3, 0, 2, 0, 6, 0, 3}, // Added another 3 in the top row.
            {9, 0, 0, 3, 0, 5, 0, 0, 1},
            {0, 0, 1, 8, 0, 6, 4, 0, 0},
            {0, 0, 8, 1, 0, 2, 9, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 6, 7, 0, 8, 2, 0, 0},
            {0, 0, 2, 6, 0, 9, 5, 0, 0},
            {8, 0, 0, 2, 0, 3, 0, 0, 9},
            {0, 0, 5, 0, 1, 0, 3, 0, 0}
    };

    private static final int[][] INVALID_INITIAL_BOARD_1 = {
            {0, 0, 3, 0, 2, 0, 6, 0, 10}, // 10 isn't a valid digit.
            {9, 0, 0, 3, 0, 5, 0, 0, 1},
            {0, 0, 1, 8, 0, 6, 4, 0, 0},
            {0, 0, 8, 1, 0, 2, 9, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 6, 7, 0, 8, 2, 0, 0},
            {0, 0, 2, 6, 0, 9, 5, 0, 0},
            {8, 0, 0, 2, 0, 3, 0, 0, 9},
            {0, 0, 5, 0, 1, 0, 3, 0, 0}
    };

    // Not enough rows.
    private static final int[][] INVALID_INITIAL_BOARD_2 = {
            {9, 0, 0, 3, 0, 5, 0, 0, 1},
            {0, 0, 1, 8, 0, 6, 4, 0, 0},
            {0, 0, 8, 1, 0, 2, 9, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 6, 7, 0, 8, 2, 0, 0},
            {0, 0, 2, 6, 0, 9, 5, 0, 0},
            {8, 0, 0, 2, 0, 3, 0, 0, 9},
            {0, 0, 5, 0, 1, 0, 3, 0, 0}
    };

    // Not enough columns
    private static final int[][] INVALID_INITIAL_BOARD_3 = {
            {0, 0, 3, 0, 2, 0, 6, 0},
            {9, 0, 0, 3, 0, 5, 0, 0},
            {0, 0, 1, 8, 0, 6, 4, 0},
            {0, 0, 8, 1, 0, 2, 9, 0},
            {7, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 6, 7, 0, 8, 2, 0},
            {0, 0, 2, 6, 0, 9, 5, 0},
            {8, 0, 0, 2, 0, 3, 0, 0},
            {0, 0, 5, 0, 1, 0, 3, 0}
    };

    // Too many columns
    private static final int[][] INVALID_INITIAL_BOARD_4 = {
            {0, 0, 3, 0, 2, 0, 6, 0, 0, 1},
            {9, 0, 0, 3, 0, 5, 0, 0, 1, 2},
            {0, 0, 1, 8, 0, 6, 4, 0, 0, 3},
            {0, 0, 8, 1, 0, 2, 9, 0, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 8, 4},
            {0, 0, 6, 7, 0, 8, 2, 0, 0, 5},
            {0, 0, 2, 6, 0, 9, 5, 0, 0, 0},
            {8, 0, 0, 2, 0, 3, 0, 0, 9, 6},
            {0, 0, 5, 0, 1, 0, 3, 0, 0, 7}
    };

    // Too many rows
    private static final int[][] INVALID_INITIAL_BOARD_5 = {
            {0, 0, 3, 0, 2, 0, 6, 0, 0},
            {9, 0, 0, 3, 0, 5, 0, 0, 1},
            {0, 0, 1, 8, 0, 6, 4, 0, 0},
            {0, 0, 8, 1, 0, 2, 9, 0, 0},
            {7, 0, 0, 0, 0, 0, 0, 0, 8},
            {0, 0, 6, 7, 0, 8, 2, 0, 0},
            {0, 0, 2, 6, 0, 9, 5, 0, 0},
            {8, 0, 0, 2, 0, 3, 0, 0, 9},
            {0, 0, 5, 0, 1, 0, 3, 0, 0},
            {0, 0, 4, 0, 0, 0, 0, 0, 0}
    };


    private SudokuSolver solver;

    @BeforeEach
    void setup() {
        solver = new SudokuSolver();
    }

    @ParameterizedTest
    @MethodSource("invalidInitialGrids")
    void boardConstructor_invalidInitialGrid_throws(int[][] grid) {
        assertThrows(IllegalArgumentException.class, () -> new SudokuSolver.Board(grid));
    }

    static Stream<int[][]> invalidInitialGrids() {
        return Stream.of(
                null,
                INVALID_INITIAL_BOARD_1,
                INVALID_INITIAL_BOARD_2,
                INVALID_INITIAL_BOARD_3,
                INVALID_INITIAL_BOARD_4,
                INVALID_INITIAL_BOARD_5);
    }

    @Test
    void solveBoard_unsolvableBoard_throws() {
        SudokuSolver.Board board = new SudokuSolver.Board(UNSOLVABLE_BOARD);
        assertThrows(IllegalStateException.class, () -> solver.solveBoard(board));
    }

    @Test
    void solveBoard_easyGrid_expectedResult() {
        SudokuSolver.Board board = new SudokuSolver.Board(EASY_BOARD);
        board = solver.solveBoard(board);
        assertArrayEquals(EASY_BOARD_SOLUTION, boardTo2DIntArray(board));
        assertFalse(board.hasGuesses());
    }

    @Test
    void solveBoard_hardGrid_expectedResult() {
        SudokuSolver.Board board = new SudokuSolver.Board(HARD_BOARD);
        board = solver.solveBoard(board);
        assertArrayEquals(HARD_BOARD_SOLUTION, boardTo2DIntArray(board));
        assertTrue(board.hasGuesses());
    }

    @Test
    void solveNextCell_unsolvableBoard_throws() {
        SudokuSolver.Board board = new SudokuSolver.Board(UNSOLVABLE_BOARD);
        assertThrows(IllegalStateException.class, () -> {
            // Only way to exit is by throwing.
            while (true) {
                solver.solveNextCell(board);
            }
        });
    }

    @Test
    void solveNextCell_boardAlreadySolved_doesNothing() {
        SudokuSolver.Board board = new SudokuSolver.Board(EASY_BOARD);
        board = solver.solveBoard(board);
        int[][] solved = boardTo2DIntArray(board);

        board = solver.solveNextCell(board);
        int[][] tryAnotherSolve = boardTo2DIntArray(board);

        assertArrayEquals(solved, tryAnotherSolve);
    }

    /**
     * The easy grid can be solved with only logic. Checks that every call to solveNextCell makes progress by decreasing
     * the number of unsolved cells by 1.
     */
    @Test
    void solveNextCell_easyGrid_expectedResult() {
        SudokuSolver.Board board = new SudokuSolver.Board(EASY_BOARD);
        int unsolvedCount = board.getUnsolvedCells().size();
        while (!board.isSolved()) {
            solver.solveNextCell(board);
            unsolvedCount--;
            assertEquals(unsolvedCount, board.getUnsolvedCells().size());
        }

        assertArrayEquals(EASY_BOARD_SOLUTION, boardTo2DIntArray(board));
        assertFalse(board.hasGuesses());
    }

    /**
     * The hard grid requires some guessing to be solved. Can't check that we only decrease here, so we just check that
     * we get to the same answer.
     */
    @Test
    void solveNextCell_hardGrid_expectedResult() {
        SudokuSolver.Board board = new SudokuSolver.Board(HARD_BOARD);

        while (!board.isSolved()) {
            solver.solveNextCell(board);
        }

        assertArrayEquals(HARD_BOARD_SOLUTION, boardTo2DIntArray(board));
        assertTrue(board.hasGuesses());
    }

    private int[][] boardTo2DIntArray(SudokuSolver.Board board) {
        SudokuSolver.Cell[][] boardGrid = board.getGrid();
        int[][] result = new int[boardGrid.length][boardGrid[0].length];
        for (int row = 0; row < boardGrid.length; row++) {
            for (int col = 0; col < boardGrid[row].length; col++) {
                result[row][col] = boardGrid[row][col].getSolution();
            }
        }
        return result;
    }
}
