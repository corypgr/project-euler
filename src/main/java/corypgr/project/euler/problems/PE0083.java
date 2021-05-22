package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.ShortestPathSolver;
import corypgr.project.euler.problems.util.ShortestPathSolver.Node;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Problem 83
 *
 * https://projecteuler.net/problem=83
 *
 * Our last shortest path problem. A little strangely, this one is more straightforward than Problem 82 as far as
 * setting up the graph. We know what our start and end nodes are. And all Nodes can connect to all adjacent Nodes.
 */
public class PE0083 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0083_grid";

    @Override
    public ProblemSolution solve() {
        int solution = getShortestPathSum();
        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Minimal Path Sum: " + solution)
                .build();
    }

    private int getShortestPathSum() {
        Node[][] grid = getNodeGrid();

        // Set up down connections. Don't need to process the last row.
        for (int row = 0; row < grid.length - 1; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col].addChild(grid[row + 1][col]);
            }
        }

        // Set up up connections. Don't need to process the first row.
        for (int row = 1; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                grid[row][col].addChild(grid[row - 1][col]);
            }
        }

        // Set up right connections. Don't need to process the last col.
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length - 1; col++) {
                grid[row][col].addChild(grid[row][col + 1]);
            }
        }

        // Set up left connections. Don't need to process the first col.
        for (int row = 0; row < grid.length; row++) {
            for (int col = 1; col < grid[row].length; col++) {
                grid[row][col].addChild(grid[row][col - 1]);
            }
        }

        ShortestPathSolver solver = new ShortestPathSolver();
        return solver.findShortestPathDistance(grid[0][0], grid[grid.length - 1][grid[0].length - 1]);
    }

    @SneakyThrows
    private Node[][] getNodeGrid() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(line -> line.split(","))
                .map(this::stringsToNodes)
                .toArray(Node[][]::new);
    }

    private Node[] stringsToNodes(String[] strings) {
        return Arrays.stream(strings)
                .map(Integer::parseInt)
                .map(Node::new)
                .toArray(Node[]::new);
    }
}
