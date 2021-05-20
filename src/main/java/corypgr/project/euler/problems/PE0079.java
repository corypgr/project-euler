package corypgr.project.euler.problems;

import corypgr.project.euler.problems.util.Problem;
import corypgr.project.euler.problems.util.ProblemSolution;
import lombok.SneakyThrows;
import lombok.Value;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Problem 79
 *
 * https://projecteuler.net/problem=79
 *
 * My idea here is to construct a graph for the digits of the hints, where node a -> b if a comes before b. Then we
 * find the best traversal of the graph by iteratively doing:
 *  * Find the node which has no other nodes pointing at it. This will be the first node of the subsequence.
 *  * Remove all connections from the first node to other nodes, or just ignore the first node for the remainder.
 *  * Repeat.
 *
 * This strategy is guaranteed to work because there is only a single solution. There must be a clear path from the
 * first to last node, which means if we iteratively remove the first node from the graph we can easily find the next
 * first node.
 */
public class PE0079 implements Problem {
    private static final String FILE_PATH = "src/main/java/corypgr/project/euler/problems/resources/PE0079_passcodes";

    @Override
    public ProblemSolution solve() {
        long solution = 0;
        Set<Node> digitGraph = createDigitGraph();
        while (!digitGraph.isEmpty()) {
            Node firstInChain = getFirstNode(digitGraph);
            solution *= 10;
            solution += firstInChain.getVal();

            digitGraph.remove(firstInChain);
        }

        return ProblemSolution.builder()
                .solution(solution)
                .descriptiveSolution("Smallest password: " + solution)
                .build();
    }

    private Node getFirstNode(Set<Node> digitGraph) {
        Set<Node> nodesWithoutIncomingConnections = new HashSet<>(digitGraph);
        digitGraph.stream()
                .map(Node::getChildren)
                .flatMap(Set::stream)
                .forEach(nodesWithoutIncomingConnections::remove);

        if (nodesWithoutIncomingConnections.size() != 1) {
            throw new IllegalStateException("Expecting only a single node without any incoming connections.");
        }
        return nodesWithoutIncomingConnections.iterator().next();
    }

    private Set<Node> createDigitGraph() {
        Map<Integer, Node> valToNode = new HashMap<>();
        for (Hint hint : getHints()) {
            Node a = valToNode.getOrDefault(hint.getA(), new Node(hint.getA()));
            valToNode.put(hint.getA(), a);
            Node b = valToNode.getOrDefault(hint.getB(), new Node(hint.getB()));
            valToNode.put(hint.getB(), b);
            Node c = valToNode.getOrDefault(hint.getC(), new Node(hint.getC()));
            valToNode.put(hint.getC(), c);

            a.addChild(b);
            a.addChild(c);
            b.addChild(c);
        }
        return new HashSet<>(valToNode.values());
    }

    @SneakyThrows
    private List<Hint> getHints() {
        return Files.lines(Paths.get(FILE_PATH))
                .map(Integer::parseInt)
                .map(Hint::new)
                .collect(Collectors.toList());
    }

    @Value
    private static class Hint {
        private final int a;
        private final int b;
        private final int c;

        public Hint(int fullVal) {
            if (fullVal >= 1000) {
                throw new IllegalArgumentException("Only expecting 3 digits. Found " + fullVal);
            }
            this.c = fullVal % 10;
            int remaining = fullVal / 10;
            this.b = remaining % 10;
            remaining /= 10;
            this.a = remaining % 10;
        }
    }

    @Value
    private static class Node {
        private final int val;
        private final Set<Node> children;

        public Node(int val) {
            this.val = val;
            this.children = new HashSet<>();
        }

        public void addChild(Node node) {
            this.children.add(node);
        }
    }
}
