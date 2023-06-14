
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import static javafx.application.Application.launch;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class MapApp extends Application {

    private static final double MAP_WIDTH = 1280;
    private static final double MAP_HEIGHT = 720;
    private static final int NUM_AIRPORTS = 6;
    private int nameAirport = 0;
    private Image mapImage;
    private PixelReader pixelReader;
    private Graph graph;

    @Override
    public void start(Stage primaryStage) {
        mapImage = new Image("file:src/images/map.png");  // Ruta de la imagen del mapa
        pixelReader = mapImage.getPixelReader();

        Canvas canvas = new Canvas(MAP_WIDTH, MAP_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Dibujar el mapa como fondo
        drawMap(gc);

        // Generar ubicaciones aleatorias y colocar aeropuertos
        Random random = new Random();
        graph = new Graph();

        for (int i = 0; i < NUM_AIRPORTS; i++) {
            double x, y;
            boolean isOnLand;

            do {

                x = random.nextDouble() * MAP_WIDTH;
                y = random.nextDouble() * MAP_HEIGHT;

                Color pixelColor = pixelReader.getColor((int) x, (int) y);
                double hue = pixelColor.getHue();
                double saturation = pixelColor.getSaturation();
                double brightness = pixelColor.getBrightness();

                boolean isGreen = hue >= 60 && hue <= 180 && saturation >= 0.3 && brightness >= 0.3;
                isOnLand = isGreen; // Si el color no está en el rango de tonos de verde, no está en el mar
            } while (!isOnLand);

            // Obtener las coordenadas de latitud y longitud
            double latitude = convertYToLatitude(y);
            double longitude = convertXToLongitude(x);

            graph.addNode(latitude, longitude);

            // Dibujar el aeropuerto
            drawAirport(gc, x, y, ("Aeropuerto " + i));

            System.out.println(("Aeropuerto " + nameAirport) + (i + 1) + ": Latitud = " + latitude + ", Longitud = " + longitude);
        }

        int[] nodeEdgesCount = new int[NUM_AIRPORTS];
        Random randomTarget = new Random();
        int totalEdges = 0;
        int MAX_EDGES = 10; // Cambia este valor según tus necesidades

        Arrays.fill(nodeEdgesCount, 0); // Inicializar todos los contadores en cero

        for (int i = 0; i < NUM_AIRPORTS; i++) {
            while (nodeEdgesCount[i] < 2 && totalEdges < MAX_EDGES) {
                int j = randomTarget.nextInt(NUM_AIRPORTS); // Generar un índice aleatorio para el nodo de destino

                if (i != j && nodeEdgesCount[j] < 2) { // Asegurarse de que no sea el mismo nodo y que el nodo de destino tenga menos de 2 aristas
                    double sourceLatitude = graph.getNode(i).getLatitude();
                    double sourceLongitude = graph.getNode(i).getLongitude();
                    double targetLatitude = graph.getNode(j).getLatitude();
                    double targetLongitude = graph.getNode(j).getLongitude();
                    double sourceX = graph.getNode(i).getX();
                    double sourceY = graph.getNode(i).getY();
                    double targetX = graph.getNode(j).getX();
                    double targetY = graph.getNode(j).getY();

                    drawRoute(gc, sourceX, sourceY, targetX, targetY);
                    int weight = calculateWeight(sourceLatitude, sourceLongitude, targetLatitude, targetLongitude);

                    graph.addEdge(i, j, weight);

                    nodeEdgesCount[i]++;
                    nodeEdgesCount[j]++;
                    totalEdges++;
                }
            }
        }

        // Calcular y trazar las rutas más cortas
//        Random randomm = new Random();
//        for (int i = 0; i < NUM_AIRPORTS; i++) {
//            int numRoutes = randomm.nextInt(2) + 1; // Genera un número aleatorio entre 2 y 3
//
//            for (int j = 0; j < numRoutes; j++) {
//                int targetNode = randomm.nextInt(NUM_AIRPORTS);
//
//                // Verificar que el nodo de destino sea diferente al nodo actual
//                while (targetNode == i) {
//                    targetNode = randomm.nextInt(NUM_AIRPORTS);
//                }
//
//                double sourceX = graph.getNode(i).getX();
//                double sourceY = graph.getNode(i).getY();
//                double targetX = graph.getNode(targetNode).getX();
//                double targetY = graph.getNode(targetNode).getY();
//
//                drawRoute(gc, sourceX, sourceY, targetX, targetY);
//
//            }
//        }
        graph.printAdjacencyMatrix();
        System.out.println("Mas corto: " + graph.shortestPath(0, 5));
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, MAP_WIDTH, MAP_HEIGHT);

        primaryStage.setTitle("Map App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void drawMap(GraphicsContext gc) {
        // Dibujar el mapa como fondo
        gc.drawImage(mapImage, 0, 0, MAP_WIDTH, MAP_HEIGHT);

        // Dibujar elementos adicionales del mapa (carreteras, fronteras, etc.)
        // ...
    }

    private void drawAirport(GraphicsContext gc, double x, double y, String location) {
        // Dibujar el aeropuerto
        gc.setFill(Color.RED);
        gc.fillOval(x - 5, y - 5, 10, 10);

        // Agregar la ubicación encima del aeropuerto
        gc.setFill(Color.BLACK);
        gc.fillText(location, x - 35, y - 10);
    }

    private void drawRoute(GraphicsContext gc, double startX, double startY, double endX, double endY) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(2);

        // Agregar efecto de sombra
        gc.setEffect(new DropShadow(10, Color.BLACK));

        gc.strokeLine(startX, startY, endX, endY);
    }

    private double convertYToLatitude(double y) {
        // Convertir la coordenada Y del clic al valor de latitud correspondiente
        double latitudeRange = 90.0; // Rango de latitudes posibles (-90 a 90)
        return (y / MAP_HEIGHT) * latitudeRange - latitudeRange / 2.0;
    }

    private double convertXToLongitude(double x) {
        // Convertir la coordenada X del clic al valor de longitud correspondiente
        double longitudeRange = 180.0; // Rango de longitudes posibles (-180 a 180)
        return (x / MAP_WIDTH) * longitudeRange - longitudeRange / 2.0;
    }

    private int calculateWeight(double lat1, double lon1, double lat2, double lon2) {
        // Calcular el peso (distancia) entre dos coordenadas geográficas
        // Puedes implementar aquí la fórmula de cálculo de distancia entre dos puntos geográficos
        // Por ejemplo, la distancia euclidiana en un plano
        double dx = lon2 - lon1;
        double dy = lat2 - lat1;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return (int) distance;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static class Graph {

        private List<Node> nodes;
        private int[][] adjacencyMatrix;

        public Graph() {
            nodes = new ArrayList<>();
            adjacencyMatrix = new int[0][0];
        }

        public void addNode(double latitude, double longitude) {
            Node node = new Node(latitude, longitude);
            nodes.add(node);

            int[][] newMatrix = new int[nodes.size()][nodes.size()];
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, adjacencyMatrix[i].length);
            }
            adjacencyMatrix = newMatrix;
        }

        public void printAdjacencyMatrix() {
            int numNodes = nodes.size();

            System.out.print("  ");
            for (int i = 0; i < numNodes; i++) {
                System.out.printf("%3d", i);
            }
            System.out.println();

            for (int i = 0; i < numNodes; i++) {
                System.out.printf("%2d ", i);
                for (int j = 0; j < numNodes; j++) {
                    System.out.printf("%3d", adjacencyMatrix[i][j]);
                }
                System.out.println();
            }
        }

        public Node getNode(int index) {
            return nodes.get(index);
        }

        public void addEdge(int source, int target, int weight) {
            adjacencyMatrix[source][target] = weight;
            adjacencyMatrix[target][source] = weight; // Agregar también la arista inversa
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
    }

    private static class NodeDistance implements Comparable<NodeDistance> {

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

    private static class Node {

        private double latitude;
        private double longitude;
        private double x;
        private double y;

        public Node(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.x = convertLongitudeToX(longitude);
            this.y = convertLatitudeToY(latitude);
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        private double convertLatitudeToY(double latitude) {
            double latitudeRange = 90.0; // Rango de latitudes posibles (-90 a 90)
            return (latitude + latitudeRange / 2.0) / latitudeRange * MAP_HEIGHT;
        }

        private double convertLongitudeToX(double longitude) {
            double longitudeRange = 180.0; // Rango de longitudes posibles (-180 a 180)
            return (longitude + longitudeRange / 2.0) / longitudeRange * MAP_WIDTH;
        }
    }

    private static class Edge {

        private int source;
        private int target;
        private int weight;

        public Edge(int source, int target, int weight) {
            this.source = source;
            this.target = target;
            this.weight = weight;
        }

        public int getSource() {
            return source;
        }

        public int getTarget() {
            return target;
        }

        public int getNeighbor(int node) {
            if (node == source) {
                return target;
            } else {
                return source;
            }
        }

        public int getWeight() {
            return weight;
        }
    }
}
