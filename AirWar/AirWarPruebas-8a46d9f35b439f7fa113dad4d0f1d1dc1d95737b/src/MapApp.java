
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static javafx.application.Application.launch;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

public class MapApp extends Application {

    private static final double MAP_WIDTH = 1280;
    private static final double MAP_HEIGHT = 720;
    private static final int NUM_AIRPORTS = 10;
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

        // Agregar aristas entre todos los aeropuertos
        for (int i = 0; i < NUM_AIRPORTS; i++) {
            for (int j = i + 1; j < NUM_AIRPORTS; j++) {
                double sourceLatitude = graph.getNode(i).getLatitude();
                double sourceLongitude = graph.getNode(i).getLongitude();
                double targetLatitude = graph.getNode(j).getLatitude();
                double targetLongitude = graph.getNode(j).getLongitude();

                int weight = calculateWeight(sourceLatitude, sourceLongitude, targetLatitude, targetLongitude);

                graph.addEdge(i, j, weight);
            }
        }

        // Calcular y trazar las rutas más cortas
        for (int i = 0; i < NUM_AIRPORTS; i++) {
            for (int j = i + 1; j < i + 2 && j < NUM_AIRPORTS; j++) {
                List<Integer> shortestPath = graph.shortestPath(i, j);

                if (!shortestPath.isEmpty()) {
                    for (int k = 0; k < shortestPath.size() - 1; k++) {
                        int sourceNode = shortestPath.get(k);
                        int targetNode = shortestPath.get(k + 1);

                        double sourceX = graph.getNode(sourceNode).getX();
                        double sourceY = graph.getNode(sourceNode).getY();
                        double targetX = graph.getNode(targetNode).getX();
                        double targetY = graph.getNode(targetNode).getY();

                        drawRoute(gc, sourceX, sourceY, targetX, targetY);
                        System.out.println("Mas corto: "+graph.shortestPath(2, 7));
                    }
                }
            }
        }

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
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
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
        private List<List<Edge>> adjacencyList;

        public Graph() {
            nodes = new ArrayList<>();
            adjacencyList = new ArrayList<>();
        }

        public void addNode(double latitude, double longitude) {
            Node node = new Node(latitude, longitude);
            nodes.add(node);
            adjacencyList.add(new ArrayList<>());
        }

        public Node getNode(int index) {
            return nodes.get(index);
        }

        public void addEdge(int source, int target, int weight) {
            Edge edge = new Edge(source, target, weight);
            adjacencyList.get(source).add(edge);
            adjacencyList.get(target).add(edge); // Agregar también la arista inversa
        }

        public List<Integer> shortestPath(int source, int target) {
            List<Integer> shortestPath = new ArrayList<>();
            int[] distances = new int[nodes.size()];
            int[] previous = new int[nodes.size()];
            boolean[] visited = new boolean[nodes.size()];

            for (int i = 0; i < distances.length; i++) {
                distances[i] = Integer.MAX_VALUE;
                previous[i] = -1;
                visited[i] = false;
            }

            distances[source] = 0;

            for (int i = 0; i < nodes.size() - 1; i++) {
                int minIndex = -1;
                int minDistance = Integer.MAX_VALUE;

                for (int j = 0; j < nodes.size(); j++) {
                    if (!visited[j] && distances[j] < minDistance) {
                        minIndex = j;
                        minDistance = distances[j];
                    }
                }

                visited[minIndex] = true;

                for (Edge edge : adjacencyList.get(minIndex)) {
                    int neighbor = edge.getNeighbor(minIndex);
                    int weight = edge.getWeight();

                    int distanceThroughMin = distances[minIndex] + weight;

                    if (distanceThroughMin < distances[neighbor]) {
                        distances[neighbor] = distanceThroughMin;
                        previous[neighbor] = minIndex;
                    }
                }
            }

            int current = target;

            while (current != -1) {
                shortestPath.add(0, current);
                current = previous[current];
            }

            return shortestPath;
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
