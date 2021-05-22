package corypgr.project.euler.problems.util;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Helper to calculate the shortest path in a graph. Uses Djikstra's algorithm, specifically we closely follow the
 * Priority Queue version described at https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm#Using_a_priority_queue
 */
public class ShortestPathSolver {
    /**
     * Accepts the start and end node of an already constructed graph, returning the full distance (weight) of the Nodes
     * in the shortest path (by weight) from the start to the end.
     *
     * Throws IllegalStateException if there is not a valid path from the start to end nodes.
     */
    public int findShortestPathDistance(Node start, Node end) {
        buildShortestPath(start, end);

        return end.getTotalDistance();
    }

    /**
     * Accepts the start and end node of an already constructed graph, returning a List of the Nodes in the shortest
     * path (by weight) from the start to the end.
     *
     * Throws IllegalStateException if there is not a valid path from the start to end nodes.
     */
    public List<Node> findShortestPath(Node start, Node end) {
        buildShortestPath(start, end);

        LinkedList<Node> result = new LinkedList<>();
        Stream.iterate(end, Objects::nonNull, Node::getPreviousNode)
                .forEach(result::addFirst);
        return result;
    }

    private void buildShortestPath(Node start, Node end) {
        start.setTotalDistance(start.getWeight());

        // Deviates somewhat from the normal Djikstra algorithm. Here we are only going to have elements in the
        // PriorityQueue after their totalDistance has been updated. This will minimize effort in removing elements
        // from the queue.
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalDistance));
        queue.add(start);

        while (!queue.isEmpty()) {
            Node closest = queue.poll();

            if (closest == end) {
                return;
            }

            for (Node closestChild : closest.getChildren()) {
                int newDistance = closest.getTotalDistance() + closestChild.getWeight();
                if (newDistance < closestChild.getTotalDistance()) {
                    closestChild.setTotalDistance(newDistance);
                    closestChild.setPreviousNode(closest);

                    // Remove and insert to reorder the element in the priority queue.
                    queue.remove(closestChild); // Does nothing if element isn't present.
                    queue.add(closestChild);
                }
            }
        }

        throw new IllegalStateException("No path from start to end nodes.");
    }

    // Explicitly not providing @EqualsAndHashcode because I want Collections containing these nodes to only care about
    // the exact Node reference matching.
    public static final class Node {
        @Getter
        private final int weight;

        @Getter(AccessLevel.PRIVATE)
        private final Set<Node> children;

        @Getter(AccessLevel.PRIVATE)
        @Setter(AccessLevel.PRIVATE)
        private int totalDistance;

        @Getter(AccessLevel.PRIVATE)
        @Setter(AccessLevel.PRIVATE)
        private Node previousNode;

        public Node(int weight) {
            this.weight = weight;
            this.children = new HashSet<>();
            this.totalDistance = Integer.MAX_VALUE;
        }

        public void addChild(Node child) {
            this.children.add(child);
        }
    }
}
