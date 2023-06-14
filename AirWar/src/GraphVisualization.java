import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

public class GraphVisualization extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        Graph graph = new Graph();
        graph.addNode(100, 100);
        graph.addNode(200, 200);
        graph.addNode(300, 100);
        graph.addNode(400, 200);
        graph.addNode(450, 250);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 18);
        graph.addEdge(0, 3, 1);
        graph.addEdge(3, 2, 12);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 2, 1);

        graph.drawGraph(pane);

        int sourceNode = 0;
        int targetNode = 2;
        List<Integer> shortestPath = graph.shortestPath(sourceNode, targetNode);

        System.out.println("Shortest path from node " + sourceNode + " to node " + targetNode + ":");
        for (int node : shortestPath) {
            System.out.print(node + " ");
        }
        System.out.println();

        primaryStage.setScene(new Scene(pane, 500, 300));
        primaryStage.setTitle("Graph Visualization");
        primaryStage.show();
    }
}
