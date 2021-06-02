package corypgr.project.euler.problems.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Contains utilities to solve a basic 9x9 Sudoku puzzle.
 *
 * Attempts to logically solve the puzzle, falling back to a guess and check strategy if it cannot make logical progress.
 */
public class SudokuSolver {

    public Board solveBoard(Board board) {
        while (!board.isSolved()) {
            solveNextCell(board);
        }
        return board;
    }

    public Board solveNextCell(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board must not be null.");
        }

        if (board.isSolved()) {
            // Nothing to do.
            return board;
        }

        try {
            attemptSolveNextCell(board);
        } catch (IllegalStateException e) {

            // One of our guesses must be invalid.
            while (!board.reverseActions.isEmpty()) {
                Board.BoardAction action = board.reverseActions.pop();
                Integer solutionFromAction = action.getCell().getSolution();
                boolean wasGuess = action.getCell().isGuess();

                action.getCell().restoreFromClone(action.getOriginalState());

                if (action.getCell().getSolution() == null) {
                    board.getUnsolvedCells().add(action.getCell());
                }

                if (wasGuess) {
                    // Current cell we're looking at was the result of a guess. Cross off the guessed number.
                    action.getCell().removeValueFromPossibleSolutions(solutionFromAction);

                    // By crossing off the guessed number, we modify the state enough to try to find another solution.
                    return solveNextCell(board);
                }
            }

            // Can only reach this point if we reach an invalid state without any outstanding guesses.
            throw new IllegalStateException("Initial board state was invalid. No solutions available.");
        }
        return board;
    }

    /**
     * Tries various logical solving techniques before guessing a solution.
     *
     * Logical checks implemented are:
     *  * Checking whether a Cell has only 1 possible solution.
     *  * Checking whether a grouping of cells (vertical, horizontal, and 3x3 box) has only 1 Cell which can hold a
     *    number. Ex: Only 1 Cell in a vertical grouping has 1 as a possible solution. That Cell must contain 1.
     */
    private void attemptSolveNextCell(Board board) {
        Optional<Cell> cellWithOnly1Option = board.getUnsolvedCells().stream()
                .filter(cell -> cell.getPossibleSolutions().size() == 1)
                .findFirst();

        if (cellWithOnly1Option.isPresent()) {
            updateCellWithSolution(board, cellWithOnly1Option.get(),
                    cellWithOnly1Option.get().getPossibleSolutions().get(0), false);
            return;
        }

        for (List<Cell> grouping : board.getAllGroupings()) {
            CountMap<Integer> countMap = new CountMap<>();
            grouping.stream()
                    .map(Cell::getPossibleSolutions)
                    .flatMap(List::stream)
                    .forEach(countMap::addCount);

            Optional<Integer> valAppearsOnlyOnceInGrouping = countMap.entrySet().stream()
                    .filter(e -> e.getValue() == 1)
                    .map(Map.Entry::getKey)
                    .findFirst();
            if (valAppearsOnlyOnceInGrouping.isPresent()) {
                Cell cell = grouping.stream()
                        .filter(c -> c.getPossibleSolutions().contains(valAppearsOnlyOnceInGrouping.get()))
                        .findFirst()
                        .get();
                updateCellWithSolution(board, cell, valAppearsOnlyOnceInGrouping.get(), false);
                return;
            }
        }

        // Guess on a cell with the smallest number of possible solutions.
        // If our guess is incorrect, we move closer to the right answer.
        Cell unsolved = board.getUnsolvedCells().stream()
                .min((Comparator.comparingInt(c -> c.getPossibleSolutions().size())))
                .get();
        int guessSolution = unsolved.getPossibleSolutions().get(0);
        updateCellWithSolution(board, unsolved, guessSolution, true);
    }

    private void updateCellWithSolution(Board board, Cell cell, int solution, boolean guess) {
        // If we're guessing, we need to keep track of all actions taken since the guess so that we can
        // reverse the actions.
        if (board.hasGuesses() || guess) {
            board.addReverseAction(new Board.BoardAction(cell, cell.clone()));

            cell.getAllGroupingCells().stream()
                    .filter(c -> c != cell)
                    .filter(c -> c.containsValueFromPossibleSolutions(solution))
                    .forEach(c -> board.addReverseAction(new Board.BoardAction(c, c.clone())));
        }

        cell.setSolution(solution);
        cell.setGuess(guess);
        cell.setPossibleSolutions(Collections.emptyList());
        cell.getAllGroupingCells().forEach(c -> c.removeValueFromPossibleSolutions(solution));
        board.getUnsolvedCells().remove(cell);
    }

    /**
     * Holds the board state.
     */
    public static final class Board {
        @Getter
        private final Cell[][] grid;
        @Getter
        private final Set<Cell> unsolvedCells;

        /**
         * The actions taken since a guess. Includes any updates to individual Cells.
         */
        @Getter
        private final Stack<BoardAction> reverseActions;

        @Getter
        private final List<List<Cell>> allGroupings;

        public Board(int[][] numberGrid) {
            if (numberGrid == null || numberGrid.length != 9 || numberGrid[0].length != 9) {
                throw new IllegalArgumentException("Invalid input numberGrid. Must be non-null and 9x9.");
            }

            grid = new Cell[numberGrid.length][numberGrid[0].length];
            for (int row = 0; row < numberGrid.length; row++) {
                for (int  col = 0; col < numberGrid[row].length; col++) {
                    int curVal = numberGrid[row][col];
                    if (curVal > 9) {
                        throw new IllegalArgumentException("Invalid cell contained: " + curVal);
                    }

                    grid[row][col] = (curVal > 0) ? new Cell(curVal) : new Cell();
                }
            }

            this.allGroupings = new LinkedList<>();

            // Set up horizontal groupings.
            for (int row = 0; row < grid.length; row++) {
                List<Cell> horizontalGrouping = new ArrayList<>(grid[row].length);
                for (int col = 0; col < grid[row].length; col++) {
                    horizontalGrouping.add(grid[row][col]);
                }

                allGroupings.add(horizontalGrouping);
                horizontalGrouping.forEach(cell -> cell.setHorizontalGrouping(horizontalGrouping));
            }

            // Set up vertical groupings.
            for (int col = 0; col < grid[0].length; col++) {
                List<Cell> verticalGrouping = new ArrayList<>(grid.length);
                for (int row = 0; row < grid.length; row++) {
                    verticalGrouping.add(grid[row][col]);
                }

                allGroupings.add(verticalGrouping);
                verticalGrouping.forEach(cell -> cell.setVerticalGrouping(verticalGrouping));
            }

            // Set up box groupings. A box is one of the 3x3 cell areas.
            for (int row = 0; row < grid.length; row += 3) {
                for (int col = 0; col < grid[row].length; col += 3) {
                    List<Cell> boxGrouping = new ArrayList<>(grid.length);
                    boxGrouping.add(grid[row][col]);
                    boxGrouping.add(grid[row][col + 1]);
                    boxGrouping.add(grid[row][col + 2]);
                    boxGrouping.add(grid[row + 1][col]);
                    boxGrouping.add(grid[row + 1][col + 1]);
                    boxGrouping.add(grid[row + 1][col + 2]);
                    boxGrouping.add(grid[row + 2][col]);
                    boxGrouping.add(grid[row + 2][col + 1]);
                    boxGrouping.add(grid[row + 2][col + 2]);

                    allGroupings.add(boxGrouping);
                    boxGrouping.forEach(cell -> cell.setBoxGrouping(boxGrouping));
                }
            }

            Arrays.stream(grid)
                    .flatMap(Arrays::stream)
                    .forEach(Cell::updatePossibleSolutionsFromGroupings);

            this.unsolvedCells = Arrays.stream(grid)
                    .flatMap(Arrays::stream)
                    .filter(cell -> cell.getSolution() == null)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            this.reverseActions = new Stack<>();
        }

        public boolean isSolved() {
            return unsolvedCells.isEmpty();
        }

        public boolean hasGuesses() {
            return !reverseActions.isEmpty();
        }

        public void addReverseAction(BoardAction action) {
            reverseActions.push(action);
        }

        @Value
        private static final class BoardAction {
            private final Cell cell;

            private final Cell originalState;
        }
    }

    /**
     * A single number cell of the board.
     */
    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    @ToString(onlyExplicitlyIncluded = true)
    public static final class Cell implements Cloneable {

        @ToString.Include
        @Getter(AccessLevel.PUBLIC)
        private Integer solution;

        @ToString.Include
        @Getter(AccessLevel.PUBLIC)
        private boolean guess;

        @ToString.Include
        @Getter(AccessLevel.PUBLIC)
        private List<Integer> possibleSolutions;

        private List<Cell> boxGrouping;
        private List<Cell> verticalGrouping;
        private List<Cell> horizontalGrouping;

        private List<Cell> allGroupingCells;

        public Cell(Integer solution) {
            this.solution = solution;
            if (solution == null) {
                this.possibleSolutions = IntStream.rangeClosed(1, 9)
                        .boxed()
                        .collect(Collectors.toCollection(LinkedList::new));
            } else {
                this.possibleSolutions = Collections.emptyList();
            }
        }

        public Cell() {
            this(null);
        }

        private void updatePossibleSolutionsFromGroupings() {
            updatePossibleSolutionsFromGrouping(boxGrouping);
            updatePossibleSolutionsFromGrouping(horizontalGrouping);
            updatePossibleSolutionsFromGrouping(verticalGrouping);
        }

        private void updatePossibleSolutionsFromGrouping(List<Cell> grouping) {
            grouping.stream()
                    .map(Cell::getSolution)
                    .filter(Objects::nonNull)
                    .forEach(this::removeValueFromPossibleSolutions);
        }

        private boolean removeValueFromPossibleSolutions(Integer val) {
            boolean result = possibleSolutions.remove(val);
            if (result && possibleSolutions.isEmpty()) {
                throw new IllegalStateException("Must have at least 1 val in possibleSolutions.");
            }
            return result;
        }

        private boolean containsValueFromPossibleSolutions(Integer val) {
            return possibleSolutions.contains(val);
        }

        private List<Cell> getAllGroupingCells() {
            if (allGroupingCells != null) {
                return allGroupingCells;
            }

            allGroupingCells = new LinkedList<>(verticalGrouping);
            Stream.concat(horizontalGrouping.stream(), boxGrouping.stream())
                    .filter(Predicate.not(allGroupingCells::contains))
                    .forEach(allGroupingCells::add);
            return allGroupingCells;
        }

        @Override
        public Cell clone() {
            Cell clone = new Cell(this.solution);
            clone.setGuess(this.guess);
            clone.setPossibleSolutions(new LinkedList<>(this.possibleSolutions));
            return clone;
        }

        private void restoreFromClone(Cell clone) {
            this.setGuess(clone.isGuess());
            this.setSolution(clone.getSolution());
            this.setPossibleSolutions(clone.getPossibleSolutions());
        }
    }
}
