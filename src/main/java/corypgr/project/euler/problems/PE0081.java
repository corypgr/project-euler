package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.Value;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
 */
public class PE0081 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0081_grid";

    @Override
    public ProblemSolution solve() {
        Cell[][] grid = getGrid();
        Queue<GridCoordinates> queue = new LinkedList<>();
        Set<GridCoordinates> coordsAddedToQueue = new HashSet<>();

        GridCoordinates initialCoordinates = new GridCoordinates(0, 0);
        queue.add(initialCoordinates);
        coordsAddedToQueue.add(initialCoordinates);
        while (!queue.isEmpty()) {
            GridCoordinates coords = queue.remove();
            Cell cell = getCellFromGrid(coords, grid);

            GridCoordinates rightCoords = coords.getCoordsForRightCell();
            Cell rightCell = getCellFromGrid(rightCoords, grid);
            if (rightCell != null) {
                rightCell.setMinimalSumFromLeft(cell.getMinimalSum());
                if (!coordsAddedToQueue.contains(rightCoords)) {
                    queue.add(rightCoords);
                    coordsAddedToQueue.add(rightCoords);
                }
            }

            GridCoordinates downCoords = coords.getCoordsForDownCell();
            Cell downCell = getCellFromGrid(downCoords, grid);
            if (downCell != null) {
                downCell.setMinimalSumFromUp(cell.getMinimalSum());
                if (!coordsAddedToQueue.contains(downCoords)) {
                    queue.add(downCoords);
                    coordsAddedToQueue.add(downCoords);
                }
            }
        }

        Cell lastCell = grid[grid.length - 1][grid[0].length - 1];

        return ProblemSolution.builder()
                .solution(lastCell.getMinimalSum())
                .descriptiveSolution("Minimal Path sum: " + lastCell.getMinimalSum())
                .build();
    }

    private Cell getCellFromGrid(GridCoordinates coords, Cell[][] grid) {
        if (coords.getRow() >= grid.length || coords.getCol() >= grid[0].length) {
            return null;
        }

        return grid[coords.getRow()][coords.getCol()];
    }

    @SneakyThrows
    private Cell[][] getGrid() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(line -> line.split(","))
                .map(this::stringsToCells)
                .toArray(Cell[][]::new);
    }

    private Cell[] stringsToCells(String[] strings) {
        return Arrays.stream(strings)
                .map(Integer::parseInt)
                .map(Cell::new)
                .toArray(Cell[]::new);
    }

    @Value
    private static class GridCoordinates {
        private final int row;
        private final int col;

        public GridCoordinates getCoordsForRightCell() {
            return new GridCoordinates(row, col + 1);
        }

        public GridCoordinates getCoordsForDownCell() {
            return new GridCoordinates(row + 1, col);
        }
    }

    @Data
    private static class Cell {
        private final int val;
        private int minimalSumFromUp;
        private int minimalSumFromLeft;
        private int minimalSum;

        public Cell(int val) {
            this.val = val;
            this.minimalSumFromUp = -1;
            this.minimalSumFromLeft = -1;
            this.minimalSum = val; // Will update when other minimalSum values come in.
        }

        public void setMinimalSumFromUp(int sum) {
            this.minimalSumFromUp = sum;
            if (this.minimalSumFromUp < this.minimalSumFromLeft || this.minimalSumFromLeft == -1) {
                this.minimalSum = this.minimalSumFromUp + this.val;
            }
        }

        public void setMinimalSumFromLeft(int sum) {
            this.minimalSumFromLeft = sum;
            if (this.minimalSumFromLeft < this.minimalSumFromUp || this.minimalSumFromUp == -1) {
                this.minimalSum = this.minimalSumFromLeft + this.val;
            }
        }
    }
}
