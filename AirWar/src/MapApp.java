
import static java.lang.System.gc;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import static javafx.application.Application.launch;
import javafx.application.Platform;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

public class MapApp extends Application {
    
    private static final double MAP_WIDTH = 1280;
    private static final double MAP_HEIGHT = 720;
    private static final int NUM_AIRPORTS = 6;
    private int nameAirport = 0;
    private Image mapImage;
    private PixelReader pixelReader;
    private MapApp.Graph graph;
    List<Lugar> ubicaciones = new ArrayList<>();
    
    private GraphicsContext gc;
    private int nRoutes = 100;
    private StackPane root;
    private Scene scene;
    private Canvas animationCanvas = new Canvas(MAP_WIDTH, MAP_HEIGHT);
    
    

    @Override
    public void start(Stage primaryStage) {
        
        mapImage = new Image("file:src/images/map.png");  // Ruta de la imagen del mapa
        pixelReader = mapImage.getPixelReader();
        
        Canvas canvas = new Canvas(MAP_WIDTH, MAP_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root = new StackPane(canvas);
        scene = new Scene(root, MAP_WIDTH, MAP_HEIGHT);
        primaryStage.setTitle("Map App");
        primaryStage.setScene(scene);
        primaryStage.show();
        // Dibujar el mapa como fondo
        drawMap(gc);

        // Generar ubicaciones aleatorias y colocar aeropuertos
        Random random = new Random();
        graph = new MapApp.Graph();
        
        generateRandomAirports(random);
        
        int[] nodeEdgesCount = new int[NUM_AIRPORTS];
        int totalEdges = 0;
        int MAX_EDGES = 100; // Cambia este valor según tus necesidades

        for (int i = 0; i < NUM_AIRPORTS; i++) {
            nodeEdgesCount[i] = 0; // Inicializar el contador en cero para cada nodo

            List<Integer> randomTargets = generateRandomTargets(i, random);
            
            for (int j : randomTargets) {
                if (nodeEdgesCount[i] < nRoutes && nodeEdgesCount[j] < nRoutes && totalEdges < MAX_EDGES) {
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
                    
                    graph.addEdge(graph.getNode(i), graph.getNode(j), 1);
                    
                    nodeEdgesCount[i]++;
                    nodeEdgesCount[j]++;
                    totalEdges++;
                    
                }
            }
        }
        System.out.println(ubicaciones);
        
        graph.printAdjacencyMatrix();
        //System.out.println("Mas corto: " + graph.shortestPath(ubicaciones.get(0), ubicaciones.get(8)));

//        List<Lugar> ruta2 = graph.shortestPath(ubicaciones.get(0), ubicaciones.get(4));
//        
        Thread thread = new Thread(() -> {
            while (true) {
                List<Ruta> ruta = graph.shortestPath(ubicaciones.get((random.nextInt(NUM_AIRPORTS))), ubicaciones.get((random.nextInt(NUM_AIRPORTS))));
                try {
                    if (!ruta.get(0).getSalida().getAvionesEsperando().isEmpty()) {
                        Avion avionn = (Avion) ruta.get(0).getSalida().getAvionesEsperando().get(0);
                        Platform.runLater(() -> drawTravelingBall(ruta, avionn));
                    } else {
                        // La lista avionesEsperando está vacía, manejar este caso adecuadamente.
                        // Por ejemplo, puedes imprimir un mensaje de error o tomar alguna acción alternativa.
                        System.out.println("La lista avionesEsperando está vacía.");
                    }
                } catch (IndexOutOfBoundsException e) {
                    
                }
                try {
                    Thread.sleep(random.nextInt(3) *2300); // Pausa de 2 segundos (2000 milisegundos)
                } catch (InterruptedException e) {
                    
                    e.printStackTrace();
                }
            }
        });
        
        thread.start();
        System.out.println("Combustible Avion 1 de 1: " + ubicaciones.get(1).getAvionesEsperando().get(0).getCombustible());
        //drawTravelingBall(ubicaciones.get(0), ubicaciones.get(4));
//        root = new StackPane(canvas);
//        scene = new Scene(root, MAP_WIDTH, MAP_HEIGHT);
//        primaryStage.setTitle("Map App");
//        primaryStage.setScene(scene);
//        primaryStage.show();
    }
    
    public void drawTravelingBall(List<Ruta> rutas, Avion avionn) {
        if (rutas.isEmpty() || avionn==null) {
            // No hay rutas en la lista
            return;
        }
        
        Ruta ruta = rutas.get(0); // Obtener la primera ruta de la lista
        rutas.remove(0); // Eliminar la primera ruta de la lista
        // Convertir las coordenadas de latitud y longitud a coordenadas cartesianas
        double startX = convertLongitudeToX(ruta.getSalida().getLongitude());
        double startY = convertLatitudeToY(ruta.getSalida().getLatitude());
        double endX = convertLongitudeToX(ruta.getDestino().getLongitude());
        double endY = convertLatitudeToY(ruta.getDestino().getLatitude());
        double distance = Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));
        // Crear un nuevo Stage para la animación
        Stage animationStage = new Stage();
        animationStage.initStyle(StageStyle.TRANSPARENT);
        animationStage.setAlwaysOnTop(true);

        // Crear un nuevo Canvas para la animación
        GraphicsContext animationGC = animationCanvas.getGraphicsContext2D();

        // Configurar el Stage y la escena
        StackPane root2 = new StackPane(animationCanvas);
        root.getChildren().add(root2);
        //Scene scene = new Scene(root, MAP_WIDTH, MAP_HEIGHT);
        scene.setFill(Color.TRANSPARENT);
        //animationStage.setScene(scene);
        animationStage.show();

        // Configurar la animación
        final int framesPerSecond = 60;
        final double duration = ruta.calcularDistancia()*4*ruta.getPeligro() / avionn.getVelocidad(); // Duración de la animación en segundos
        final double totalFrames = framesPerSecond * duration;
        
        double currentX = startX;
        double currentY = startY;
        
        Avion avionDespachado = ruta.getSalida().despacharAvion(avionn);
        avionn.despegar(ruta.getDestino());
        
       
        AnimationTimer animationTimer = new AnimationTimer() {
            
            private double frameCount = 0;
            
            @Override
            public void handle(long now) {
                frameCount++;
                
                if (frameCount <= totalFrames) {
                    // Calcular la posición actual de la bola
                    double t = frameCount / totalFrames;
                    double currentPosX = startX + t * (endX - startX);
                    double currentPosY = startY + t * (endY - startY);
                    double distancia = calculateWeight(startY, startX, currentPosY, currentPosX);
                    // Limpiar el canvas y dibujar el mapa
                    animationGC.clearRect(currentPosX - 10, currentPosY - 10, 20, 20);
                    //drawMap(animationGC);

                    System.out.println("Distancia:  " + ruta.calcularPeso() * (distancia / ((distance))));
// Dibujar la ruta
                    
                    avionn.consumirCombustible((int) (avionn.getEficiencia()/30));
                    //drawRoute(animationGC, startX, startY, endX, endY);
                    // Dibujar la bola en la posición actual
                    animationGC.setFill(Color.BLACK);
                    animationGC.fillOval(currentPosX - 5, currentPosY - 5, 10, 10);
                    if (avionn.getEstado()==Avion.EstadoAvion.DESTRUIDO){
                        animationGC.setFill(Color.ORANGE);
                        stop();
                        graph.editEdge(ubicaciones.get(0), ubicaciones.get(1), 0.5);
                        if (animationStage.isShowing()) {
                        animationStage.close();
                    }
                    }
                    if (avionn.getCombustible() <= 0) {
                        System.out.println("Avion ha explotado");
                        animationGC.setFill(Color.ORANGE);
                        animationGC.fillOval(currentPosX - 5, currentPosY - 5, 10, 10);
                        avionn.gestionarCombustible((int) -(avionn.getEficiencia()));
                        stop();
                        graph.editEdge(ubicaciones.get(0), ubicaciones.get(1), 0.5);
                        if (animationStage.isShowing()) {
                        animationStage.close();
                    }
                    }
                } else {
                    try {
                        // La animación ha terminado, detener el AnimationTimer
                        
                        stop();
                        
                        
                        
                    } catch (Exception ex) {
                        Logger.getLogger(MapApp.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    // Cerrar el Stage de la animación
                    if (animationStage.isShowing()) {
                        animationStage.close();
                    }
                    //avionn.gestionarCombustible((int) -(ruta.calcularPeso()));
                    // Actualizar el mapa principal con la bola en la posición final
                    //gc.clearRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
                    //drawMap(gc);
                    //drawRoute(gc, startX, startY, endX, endY);
                    animationGC.setFill(Color.RED);
                    animationGC.fillOval(endX - 5, endY - 5, 10, 10);
                    ruta.getDestino().recibirAvion(avionDespachado);
                    avionn.aterrizar();
                    System.out.println("Distancia recorrida en total:  " + ruta.calcularPeso());
                    // Llamar recursivamente al método para la siguiente ruta en la lista
                    drawTravelingBall(rutas, avionn);
                    graph.editEdge(ubicaciones.get(0), ubicaciones.get(1), -0.5);
                }
            }
        };

        // Iniciar la animación
        animationTimer.start();
        
    }
    
    private double convertLongitudeToX(double longitude) {
        // Convertir la longitud a la coordenada X correspondiente
        double longitudeRange = 180.0; // Rango de longitudes posibles (-180 a 180)
        return (longitude + longitudeRange / 2.0) / longitudeRange * MAP_WIDTH;
    }
    
    private double convertLatitudeToY(double latitude) {
        // Convertir la latitud a la coordenada Y correspondiente
        double latitudeRange = 90.0; // Rango de latitudes posibles (-90 a 90)
        return (latitude + latitudeRange / 2.0) / latitudeRange * MAP_HEIGHT;
    }
    
    private void generateRandomAirports(Random random) {
        for (int i = 0; i < NUM_AIRPORTS; i++) {
            double x, y;
            boolean isOnLand;
            
            x = random.nextDouble() * MAP_WIDTH;
            y = random.nextDouble() * MAP_HEIGHT;
            
            Color pixelColor = pixelReader.getColor((int) x, (int) y);
            double hue = pixelColor.getHue();
            double saturation = pixelColor.getSaturation();
            double brightness = pixelColor.getBrightness();
            
            boolean isGreen = hue >= 60 && hue <= 180 && saturation >= 0.3 && brightness >= 0.3;
            isOnLand = isGreen; // Si el color no está en el rango de tonos de verde, no está en el mar

            if (isOnLand) {
                // Obtener las coordenadas de latitud y longitud
                double latitude = convertYToLatitude(y);
                double longitude = convertXToLongitude(x);
                
                AirPort airport = new AirPort("Aeropuerto " + i, ((random.nextInt(1)) + 6), latitude, longitude);
                
                int z = 0;
                while (z < airport.getCapHang() - ((random.nextInt(2)) + 3)) {
                    Avion avion = new Avion("Avion", (random.nextInt(40)) + 880, 1200, 3);
                    airport.recibirAvion(avion);
                    z++;
                }
                
                ubicaciones.add(airport);
                System.out.println(airport.getNombre());

                // Dibujar el aeropuerto
                drawAirport(gc, x, y, airport.getNombre());
                
                System.out.println("Aeropuerto " + (i + 1) + ": Latitud = " + latitude + ", Longitud = " + longitude);
                
                graph.addNode(airport);
            }
            
            if (!isOnLand) {
                // Obtener las coordenadas de latitud y longitud
                double latitude = convertYToLatitude(y);
                double longitude = convertXToLongitude(x);
                
                Portaavion portaAviones = new Portaavion(("Portaaviones " + i), ((random.nextInt(1)) + 6), latitude, longitude);
                ubicaciones.add(portaAviones);
                System.out.println(portaAviones.getNombre());
//                Avion avion = new Avion("Avion", (random.nextInt(30)) + 250, 12, 3);
//                portaAviones.recibirAvion(avion);
                // Dibujar el portaaviones
                drawAirport(gc, x, y, portaAviones.getNombre());
                int z = 0;
                while (z < portaAviones.getCapHang() - ((random.nextInt(2)) + 3)) {
                    Avion avionPortaAvion = new Avion("Avion", (random.nextInt(40)) + 880, 1200, 3);
                    portaAviones.recibirAvion(avionPortaAvion);
                    z++;
                }
                System.out.println("Portaaviones " + (i + 1) + ": Latitud = " + latitude + ", Longitud = " + longitude);
                
                graph.addNode(portaAviones);
            }
            
        }
    }
    
    private List<Integer> generateRandomTargets(int sourceIndex, Random random) {
        List<Integer> randomTargets = new ArrayList<>();
        
        for (int i = 0; i < NUM_AIRPORTS; i++) {
            if (i != sourceIndex) {
                randomTargets.add(i);
            }
        }
        
        Collections.shuffle(randomTargets, random);
        return randomTargets.subList(0, 2); // Obtener los primeros 2 elementos de forma aleatoria
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

        // Configurar el trazo punteado
        double dashSize = 5; // Tamaño de cada segmento de línea
        double gapSize = 7; // Tamaño del espacio entre segmentos
        gc.setLineDashes(dashSize, gapSize);

        // Agregar efecto de sombra
        gc.setEffect(new DropShadow(10, Color.BLACK));
        
        gc.strokeLine(startX, startY, endX, endY);

        // Restaurar el trazo sólido por defecto
        gc.setLineDashes(null);
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
    
    public class Graph {
        
        private List<Lugar> nodes;
        private Ruta[][] adjacencyMatrix;
        
        public Graph() {
            nodes = new ArrayList<>();
            adjacencyMatrix = new Ruta[0][0];
        }
        
        public void addNode(Lugar lugar) {
            nodes.add(lugar);
            
            Ruta[][] newMatrix = new Ruta[nodes.size()][nodes.size()];
            for (int i = 0; i < adjacencyMatrix.length; i++) {
                System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, adjacencyMatrix[i].length);
            }
            adjacencyMatrix = newMatrix;
        }
        public void editEdge(Lugar source, Lugar target, double peligro) {
    int sourceIndex = nodes.indexOf(source);
    int targetIndex = nodes.indexOf(target);

    if (sourceIndex >= 0 && sourceIndex < nodes.size() && targetIndex >= 0 && targetIndex < nodes.size()) {
        Ruta ruta = adjacencyMatrix[sourceIndex][targetIndex];
        if (ruta != null) {
            ruta.setPeligro(peligro);
        } else {
            // Si la ruta no existe, puedes agregarla como una nueva ruta
            Ruta newRuta = new Ruta(source, target, peligro);
            adjacencyMatrix[sourceIndex][targetIndex] = newRuta;
        }
    }
}

        public void printAdjacencyMatrix() {
            int numNodes = nodes.size();
            
            System.out.print("     ");
            for (int i = 0; i < numNodes; i++) {
                System.out.printf("%5d", i);
            }
            System.out.println();
            
            for (int i = 0; i < numNodes; i++) {
                System.out.printf("%5d", i);
                for (int j = 0; j < numNodes; j++) {
                    if (adjacencyMatrix[i][j] == null) {
                        System.out.printf("%5s", "0");
                    } else {
                        System.out.printf("%5d", (int) adjacencyMatrix[i][j].calcularPeso());
                    }
                }
                System.out.println();
            }
        }
        
        public Lugar getNode(int index) {
            return nodes.get(index);
        }
        
        public void addEdge(Lugar source, Lugar target, int peligro) {
            int sourceIndex = nodes.indexOf(source);
            int targetIndex = nodes.indexOf(target);
            Ruta ruta = new Ruta(source, target, peligro);
            adjacencyMatrix[sourceIndex][targetIndex] = ruta;
            //adjacencyMatrix[targetIndex][sourceIndex] = ruta;
        }
        
        public List<Ruta> shortestPath(Lugar source, Lugar target) {
            int numNodes = nodes.size();
            int sourceIndex = nodes.indexOf(source);
            int targetIndex = nodes.indexOf(target);
            
            int[] distances = new int[numNodes];
            Arrays.fill(distances, Integer.MAX_VALUE);
            distances[sourceIndex] = 0;
            
            boolean[] visited = new boolean[numNodes];
            
            int[] previous = new int[numNodes];
            Arrays.fill(previous, -1);
            
            PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
            pq.offer(new NodeDistance(sourceIndex, 0));
            
            while (!pq.isEmpty()) {
                NodeDistance minNode = pq.poll();
                int node = minNode.getNode();
                
                if (node == targetIndex) {
                    break;  // Encontró el nodo destino, termina el algoritmo
                }
                
                if (visited[node]) {
                    continue;  // Nodo ya visitado, pasa al siguiente
                }
                
                visited[node] = true;
                
                for (int neighbor = 0; neighbor < numNodes; neighbor++) {
                    if (adjacencyMatrix[node][neighbor] != null) {
                        double distance = (double) (distances[node] + adjacencyMatrix[node][neighbor].calcularPeso());
                        
                        if (distance < distances[neighbor]) {
                            distances[neighbor] = (int) distance;
                            previous[neighbor] = node;
                            pq.offer(new NodeDistance(neighbor, (int) distance));
                        }
                    }
                }
            }

            // Reconstruye la ruta desde el nodo objetivo hasta el nodo fuente
            List<Integer> path = new ArrayList<>();
            int current = targetIndex;
            
            while (current != -1) {
                path.add(0, current);
                current = previous[current];
            }
            
            List<Ruta> pathRutas = new ArrayList<>();
            for (int i = 0; i < path.size() - 1; i++) {
                int fromIndex = path.get(i);
                int toIndex = path.get(i + 1);
                Ruta ruta = adjacencyMatrix[fromIndex][toIndex];
                pathRutas.add(ruta);
            }
            
            return pathRutas;
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
    }
    
}
