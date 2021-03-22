package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import corypgr.project.euler.problems.util.TriangleMaxSumRoute;

import java.nio.file.Paths;

/**
 * Problem 67
 *
 * https://projecteuler.net/problem=67
 *
 * See TriangleMaxSumRoute for strategy. Also applies to problem 18.
 */
public class PE0067 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0067_triangle";

    @Override
    public ProblemSolution solve() {
        TriangleMaxSumRoute triangleRouteUtil = new TriangleMaxSumRoute();
        long maxSum = triangleRouteUtil.findMaxSumRoute(Paths.get(FILE_PATH));

        return ProblemSolution.builder()
                .solution(maxSum)
                .descriptiveSolution("Sum for the best route: " + maxSum)
                .build();
    }
}
