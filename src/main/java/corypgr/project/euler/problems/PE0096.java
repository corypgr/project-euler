package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.SudokuSolver;
import corypgr.project.euler.problems.util.SudokuSolver.Board;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Problem 96
 *
 * https://projecteuler.net/problem=96
 *
 * Solving Sudoku puzzles. Very interesting. I wrote a brute force Sudoku solver in college, and I remember it took
 * around 1 second to solve a single puzzle, and up to 5 seconds on a "worst case" puzzle which was set up to force many
 * iterations. Even assuming my current machine is much faster, that would be too slow. Instead, I think I'll try to
 * solve the puzzles using logic tests, and only go over to brute force when needed.
 *
 * See SudokuSolver for the solving algorithm. One interesting thing here is that we only really care about the upper
 * left 3 digits. Since that's the case, I thought our SudokuSolver should have a sort of incremental solving mechanism.
 * Basically, we solve the puzzle one number cell at a time, and we can stop once we've found the upper left 3 values.
 */
public class PE0096 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0096_sudoku";

    @Override
    public ProblemSolution solve() {
        List<Board> boards = getSudokuBoards();
        SudokuSolver solver = new SudokuSolver();
        int sum = boards.stream()
                .mapToInt(board -> getUpperLeftCornerNumber(board, solver))
                .sum();

        return ProblemSolution.builder()
                .solution(sum)
                .descriptiveSolution("Sum of the 3 digit numbers in the top left corners: " + sum)
                .build();
    }

    /**
     * Returns the upper left 3 digits as if they were a 3 digit number. Solves the board up to the point where those
     * 3 numbers are locked in.
     */
    private int getUpperLeftCornerNumber(Board board, SudokuSolver solver) {
        while (!upperLeftCornerSolved(board)) {
            solver.solveNextCell(board);
        }

        int result = board.getGrid()[0][0].getSolution();
        result *= 10;
        result += board.getGrid()[0][1].getSolution();
        result *= 10;
        result += board.getGrid()[0][2].getSolution();
        return result;
    }

    private boolean upperLeftCornerSolved(Board board) {
        // If the board has guesses, we need to solve the entire board to make sure those guesses are correct.
        return board.isSolved() || (!board.hasGuesses() &&
                board.getGrid()[0][0].getSolution() != null &&
                board.getGrid()[0][1].getSolution() != null &&
                board.getGrid()[0][2].getSolution() != null);
    }

    @SneakyThrows
    private List<Board> getSudokuBoards() {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));

        int row = 0;
        List<Board> boards = new LinkedList<>();
        int[][] curGrid = new int[9][9];
        for (String line : lines) {
            if (!line.contains("Grid") && row < 9) {
                curGrid[row] = line.chars()
                        .map(Character::getNumericValue)
                        .toArray();
                row++;
            }

            if (row == 9) {
                Board newBoard = new Board(curGrid);
                boards.add(newBoard);
                row = 0;
            }
        }

        return boards;
    }
}
