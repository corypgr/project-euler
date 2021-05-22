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
 * Problem 82
 *
 * https://projecteuler.net/problem=82
 *
 * Marked as a more challenging version of Problem 81. Looking ahead, I see Problem 82 is another of these shortest path
 * problems. Decided to write a generic solver. All this class needs to do is generate the starting graph and indicate
 * what the start and end nodes are.
 *
 * The start and end nodes is the interesting bit for this problem. Since we can start at any val on the first column
 * and end at any val on the last column, there isn't a defined start or end. To solve for this, we can use a dummy
 * start and dummy end node, both with weights 0, which are connected to all possible start and all possible end routes.
 * Our solver will work out the actual best start and end from that.
 */
public class PE0082 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0082_grid";

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
        Node dummyStart = new Node(0);
        Node dummyEnd = new Node(0);

        // Set up the dummy start and end Nodes. All Nodes in the first column are children of the dummy start. The
        // dummy end is a child of all Nodes in the last column.
        int lastCol = grid[0].length - 1;
        for (int row = 0; row < grid.length; row++) {
            dummyStart.addChild(grid[row][0]);
            grid[row][lastCol].addChild(dummyEnd);
        }

        // We can move vertically in any column, but logically we'll never do so in the first or last columns.
        // Since we can start in any of the first column and end in any of the last, moving vertically in either
        // column would only increase our sum.

        // Set up down connections. Don't need to process the last row, or the first and last columns.
        for (int row = 0; row < grid.length - 1; row++) {
            for (int col = 1; col < lastCol; col++) {
                grid[row][col].addChild(grid[row + 1][col]);
            }
        }

        // Set up up connections. Don't need to process the first row, or the first and last columns.
        for (int row = 1; row < grid.length; row++) {
            for (int col = 1; col < lastCol; col++) {
                grid[row][col].addChild(grid[row - 1][col]);
            }
        }

        // Set up right connections. Don't need to process the last column.
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length - 1; col++) {
                grid[row][col].addChild(grid[row][col + 1]);
            }
        }

        ShortestPathSolver solver = new ShortestPathSolver();
        return solver.findShortestPathDistance(dummyStart, dummyEnd);
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
