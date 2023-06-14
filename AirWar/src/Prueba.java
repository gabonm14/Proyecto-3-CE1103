import java.util.Random;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Prueba extends Application {

    private static final double MAP_WIDTH = 1280;
    private static final double MAP_HEIGHT = 720;
    private GraphicsContext gc;
    private StackPane root;
    private Scene scene;
    private Circle selectedBall; // Variable para almacenar la bola negra seleccionada

    @Override
    public void start(Stage primaryStage) {

        Canvas canvas = new Canvas(MAP_WIDTH, MAP_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root = new StackPane(canvas);
        scene = new Scene(root, MAP_WIDTH, MAP_HEIGHT);

        // Agregar EventHandler para capturar eventos de teclado
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE && selectedBall != null) {
                // Eliminar la bola negra seleccionada
                root.getChildren().remove(selectedBall);
                selectedBall = null; // Restablecer la referencia a la bola negra seleccionada
            }
        });

        // Agregar EventHandler para capturar eventos de clic del mouse
        canvas.setOnMouseClicked(event -> {
            double mouseX = event.getX();
            double mouseY = event.getY();

            // Verificar si se hizo clic en una bola negra
            for (Node node : root.getChildren()) {
                if (node instanceof Circle && node.contains(mouseX, mouseY)) {
                    selectedBall = (Circle) node; // Almacenar la referencia a la bola negra seleccionada
                    break;
                }
            }
        });

        primaryStage.setTitle("Map App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Dibujar las bolas negras
        drawBalls();
    }
 public static void main(String[] args) {
        launch(args);
        
    }
    
    public void drawBalls() {
        // Ejemplo: Dibujar 10 bolas negras en posiciones aleatorias
        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            double x = random.nextInt(1280) ;
            double y = random.nextInt( 720);

            Circle ball = new Circle(x, y, 5, Color.BLACK);
            root.getChildren().add(ball);
        }
    }
}
