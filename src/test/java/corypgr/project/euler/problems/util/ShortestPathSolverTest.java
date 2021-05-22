package corypgr.project.euler.problems.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import corypgr.project.euler.problems.util.ShortestPathSolver.Node;
import lombok.Builder;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

class ShortestPathSolverTest {
    private ShortestPathSolver solver;

    @BeforeEach
    void setup() {
        solver = new ShortestPathSolver();
    }

    @ParameterizedTest
    @MethodSource("connectedCases")
    void findShortestPathDistance_connectedGraph_expectedResult(GraphCaseData data) {
        assertEquals(data.getShortestPathDistance(), solver.findShortestPathDistance(data.getStart(), data.getEnd()));
    }

    @ParameterizedTest
    @MethodSource("connectedCases")
    void findShortestPath_connectedGraph_expectedResult(GraphCaseData data) {
        assertEquals(data.getShortestPath(), solver.findShortestPath(data.getStart(), data.getEnd()));
    }

    @ParameterizedTest
    @MethodSource("disconnectedCases")
    void findShortestPathDistance_disconnectedGraph_throws(GraphCaseData data) {
        assertThrows(IllegalStateException.class, () -> solver.findShortestPathDistance(data.getStart(), data.getEnd()));
    }

    @ParameterizedTest
    @MethodSource("disconnectedCases")
    void findShortestPath_disconnectedGraph_throws(GraphCaseData data) {
        assertThrows(IllegalStateException.class, () -> solver.findShortestPath(data.getStart(), data.getEnd()));
    }

    /**
     * Note that the solver modifies Nodes as it goes, so we need a fresh set of Nodes for every test. JUnit's
     * @ParameterizedTest functionality will call the @MethodSource method for every test, so we can reuse this
     * for multiple test types.
     */
    private static Stream<GraphCaseData> connectedCases() {
        return Stream.of(
                getSingleNodeGraph(),
                getSimpleConnectedGraph(),
                getComplexConnectedGraph());
    }

    /**
     * Note that the solver modifies Nodes as it goes, so we need a fresh set of Nodes for every test. JUnit's
     * @ParameterizedTest functionality will call the @MethodSource method for every test, so we can reuse this
     * for multiple test types.
     */
    private static Stream<GraphCaseData> disconnectedCases() {
        return Stream.of(
                getSimpleDisconnectedGraph(),
                getComplexDisconnectedGraph());
    }

    private static GraphCaseData getSingleNodeGraph() {
        Node startAndEnd = new Node(1);

        return GraphCaseData.builder()
                .start(startAndEnd)
                .end(startAndEnd)
                .shortestPathDistance(1)
                .shortestPath(List.of(startAndEnd))
                .build();
    }

    private static GraphCaseData getSimpleConnectedGraph() {
        Node start = new Node(1);
        Node end = new Node(2);
        start.addChild(start); // self-loop
        start.addChild(end);

        return GraphCaseData.builder()
                .start(start)
                .end(end)
                .shortestPathDistance(3)
                .shortestPath(List.of(start, end))
                .build();
    }

    private static GraphCaseData getComplexConnectedGraph() {
        Node start = new Node(1);
        Node end = new Node(2);
        Node largeStep = new Node(1_000_000);
        Node smallStep1 = new Node(3);
        Node smallStep2 = new Node(4);

        start.addChild(largeStep);
        start.addChild(smallStep1);
        start.addChild(smallStep2);
        smallStep1.addChild(largeStep);
        smallStep1.addChild(smallStep2);
        smallStep2.addChild(smallStep1);
        smallStep2.addChild(smallStep2);
        smallStep2.addChild(end);

        return GraphCaseData.builder()
                .start(start)
                .end(end)
                .shortestPathDistance(7)
                .shortestPath(List.of(start, smallStep2, end))
                .build();
    }

    private static GraphCaseData getSimpleDisconnectedGraph() {
        Node start = new Node(1);
        Node end = new Node(2);
        start.addChild(start); // self-loop

        return GraphCaseData.builder()
                .start(start)
                .end(end)
                .build();
    }

    private static GraphCaseData getComplexDisconnectedGraph() {
        Node start = new Node(1);
        Node end = new Node(2);

        Node largeStep = new Node(1_000_000);
        Node smallStep1 = new Node(3);
        Node smallStep2 = new Node(4);

        start.addChild(largeStep);
        start.addChild(smallStep1);
        start.addChild(smallStep2);
        smallStep1.addChild(largeStep);
        smallStep1.addChild(smallStep2);
        smallStep2.addChild(smallStep1);
        smallStep2.addChild(smallStep2);

        return GraphCaseData.builder()
                .start(start)
                .end(end)
                .build();
    }

    @Value
    @Builder
    private static final class GraphCaseData {
        private final Node start;
        private final Node end;

        private final int shortestPathDistance;
        private final List<Node> shortestPath;
    }
}
