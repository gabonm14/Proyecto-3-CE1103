
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.*;

public class Graph {

    private List<Node> nodes;
    private int[][] adjacencyMatrix;

    public Graph() {
        nodes = new ArrayList<>();
    }

    public void addNode(double x, double y) {
        nodes.add(new Node(x, y));
    }

    public void addEdge(int source, int target, int weight) {
        if (adjacencyMatrix == null) {
            adjacencyMatrix = new int[nodes.size()][nodes.size()];
        }
        adjacencyMatrix[source][target] = weight;
        adjacencyMatrix[target][source] = weight;
    }

    public void drawGraph(Pane pane) {
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            Circle circle = new Circle(node.getX(), node.getY(), 10);
            circle.setFill(Color.BLUE);
            pane.getChildren().add(circle);
            node.setCircle(circle);
        }

        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                if (adjacencyMatrix[i][j] > 0) {
                    Node sourceNode = nodes.get(i);
                    Node targetNode = nodes.get(j);
                    Line line = new Line(sourceNode.getX(), sourceNode.getY(), targetNode.getX(), targetNode.getY());
                    line.setStroke(Color.RED);
                    pane.getChildren().add(line);
                }
            }
        }
    }

    public List<Integer> shortestPath(int source, int target) {
        int numNodes = nodes.size();

        int[] distances = new int[numNodes];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        boolean[] visited = new boolean[numNodes];

        int[] previous = new int[numNodes];
        Arrays.fill(previous, -1);

        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        pq.offer(new NodeDistance(source, 0));

        while (!pq.isEmpty()) {
            NodeDistance minNode = pq.poll();
            int node = minNode.getNode();

            if (node == target) {
                break;  // Encontró el nodo destino, termina el algoritmo
            }

            if (visited[node]) {
                continue;  // Nodo ya visitado, pasa al siguiente
            }

            visited[node] = true;

            for (int neighbor = 0; neighbor < numNodes; neighbor++) {
                if (adjacencyMatrix[node][neighbor] > 0) {
                    int distance = distances[node] + adjacencyMatrix[node][neighbor];

                    if (distance < distances[neighbor]) {
                        distances[neighbor] = distance;
                        previous[neighbor] = node;
                        pq.offer(new NodeDistance(neighbor, distance));
                    }
                }
            }
        }

        // Reconstruye la ruta desde el nodo objetivo hasta el nodo fuente
        List<Integer> path = new ArrayList<>();
        int current = target;

        while (current != -1) {
            path.add(0, current);
            current = previous[current];
        }

        return path;
    }

    public Node getNode(int index) {
        if (index >= 0 && index < nodes.size()) {
            return nodes.get(index);
        }
        return null;
    }

    class Node {

        private double x;
        private double y;
        private Circle circle;

        public Node(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public Circle getCircle() {
            return circle;
        }

        public void setCircle(Circle circle) {
            this.circle = circle;
        }
    }

    private class NodeDistance implements Comparable<NodeDistance> {

        private int node;
        private int distance;

        public NodeDistance(int node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        public int getNode() {
            return node;
        }

        public int getDistance() {
            return distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Integer.compare(distance, other.distance);
        }
    }
}
