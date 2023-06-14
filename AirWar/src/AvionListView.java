
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import static javafx.application.Application.launch;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

public class AvionListView extends Application {

    private ListView<Avion> listView;
    private ObservableList<Avion> aviones;
    private List<Lugar> lugares;
    private ComboBox<Lugar> dropdown;
    private ComboBox<String> filtroDropdown; // Nuevo ComboBox para el filtro

    private Lugar opcionSeleccionada;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Crear la lista de aviones
        aviones = FXCollections.observableArrayList();
        cargarAvionesDesdeArchivo("src/archivos/aviones.txt");
        System.out.println(aviones);

        // Crear el ListView
        listView = new ListView<>(aviones);
        listView.setPrefWidth(400);
        listView.setPrefHeight(300);
        listView.setCellFactory(new Callback<ListView<Avion>, ListCell<Avion>>() {
            @Override
            public ListCell<Avion> call(ListView<Avion> param) {
                return new ListCell<Avion>() {
                    @Override
                    protected void updateItem(Avion avion, boolean empty) {
                        super.updateItem(avion, empty);
                        if (empty || avion == null) {
                            setText(null);
                        } else {
                            String itemText = String.format("%s, %d, %.0f, %d",
                                    avion.getNombre(), avion.getVelocidad(), avion.getEficiencia(), avion.getFortaleza());
                            setText(itemText);
                        }
                    }
                };
            }
        });
        Label lugarLabel = new Label("Selecciona un Lugar");
        lugares = MapApp.graph.nodes;
        dropdown = new ComboBox<>();
        dropdown.getItems().addAll(lugares);
        dropdown.setCellFactory(new Callback<ListView<Lugar>, ListCell<Lugar>>() {
            @Override
            public ListCell<Lugar> call(ListView<Lugar> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Lugar lugar, boolean empty) {
                        super.updateItem(lugar, empty);
                        if (lugar != null) {
                            setText(lugar.getNombre()); // Mostrar solo el nombre del lugar en el dropdown
                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
        dropdown.setOnAction(event -> {
            Lugar lugarSeleccionado = dropdown.getValue();
            if (lugarSeleccionado != null) {
                opcionSeleccionada = lugarSeleccionado;
                // Realiza cualquier acción adicional que desees con el lugar seleccionado
            }
        });

        filtroDropdown = new ComboBox<>();

        filtroDropdown.getItems().addAll("Eficiencia", "Velocidad");
        filtroDropdown.setOnAction(event -> filtrarAviones(filtroDropdown.getValue()));
        HBox hboxFiltro = new HBox();
        hboxFiltro.setAlignment(Pos.CENTER_RIGHT);

// Otros códigos relacionados con el ComboBox
        hboxFiltro.getChildren().add(filtroDropdown);

        Button seleccionarBtn = new Button("Seleccionar");
        seleccionarBtn.setAlignment(Pos.CENTER_RIGHT);
        seleccionarBtn.setOnAction(event -> {
            Avion avionSeleccionado = new Avion(
                    listView.getSelectionModel().getSelectedItem().getNombre(),
                    listView.getSelectionModel().getSelectedItem().getVelocidad(),
                    listView.getSelectionModel().getSelectedItem().getEficiencia(),
                    listView.getSelectionModel().getSelectedItem().getFortaleza());
            if (avionSeleccionado != null) {
                // Llamar al método de creación del objeto en OtraClase
                MapApp.graph.recibirAvion(opcionSeleccionada, avionSeleccionado);
            }
        });

        // Crear el campo de búsqueda
        TextField buscarField = new TextField();
        Button buscarBtn = new Button("Buscar");
        buscarBtn.setOnAction(event -> buscarAvionPorNombre(buscarField.getText()));

        HBox buscarContainer = new HBox(10);
        buscarContainer.getChildren().addAll(buscarField, buscarBtn, hboxFiltro);
        // Crear el contenedor principal
        VBox root = new VBox(10);
        root.getChildren().addAll(lugarLabel, dropdown, buscarContainer, listView, seleccionarBtn);

        // Configurar la escena y mostrar la ventana
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lista de Aviones");
        primaryStage.show();
    }

    private void filtrarAviones(String filtro) {
        Comparator<Avion> comparador;
        if (filtro.equalsIgnoreCase("Eficiencia")) {
            comparador = Comparator.comparing(Avion::getEficiencia);
        } else if (filtro.equalsIgnoreCase("Velocidad")) {
            comparador = Comparator.comparing(Avion::getVelocidad);
        } else {
            // Si no se selecciona un filtro válido, no se aplica ningún filtro
            return;
        }

        List<Avion> avionesList = new ArrayList<>(aviones);
        avionesList.sort(comparador);
        aviones.setAll(avionesList);
    }

    private void ordenarPorEficiencia() {
        List<Avion> avionesList = new ArrayList<>(aviones);
        shellSort(avionesList);
        aviones.setAll(avionesList);
    }

    private void ordenarPorVelocidad() {
        List<Avion> avionesList = new ArrayList<>(aviones);
        insertionSort(avionesList);
        aviones.setAll(avionesList);
    }

    private void buscarAvionPorNombre(String nombre) {
        for (Avion avion : aviones) {
            if (avion.getNombre().equalsIgnoreCase(nombre)) {
                listView.getSelectionModel().select(avion);
                listView.scrollTo(avion);
                return;
            }
        }
        listView.getSelectionModel().clearSelection();
    }

    private void cargarAvionesDesdeArchivo(String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 4) {
                    String nombre = partes[0];
                    int velocidad = Integer.parseInt(partes[1]);
                    double eficiencia = Double.parseDouble(partes[2]);
                    int fortaleza = Integer.parseInt(partes[3]);
                    Avion avion = new Avion(nombre, velocidad, eficiencia, fortaleza);
                    System.out.println("Nombre: " + nombre);
                    aviones.add(avion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void shellSort(List<Avion> lista) {
        int n = lista.size();
        int intervalo = 1;
        while (intervalo < n / 3) {
            intervalo = 3 * intervalo + 1;
        }

        while (intervalo > 0) {
            for (int i = intervalo; i < n; i++) {
                Avion temp = lista.get(i);
                int j = i;
                while (j > intervalo - 1 && lista.get(j - intervalo).getEficiencia() > temp.getEficiencia()) {
                    lista.set(j, lista.get(j - intervalo));
                    j -= intervalo;
                }
                lista.set(j, temp);
            }
            intervalo = (intervalo - 1) / 3;
        }
    }

    private void insertionSort(List<Avion> lista) {
        int n = lista.size();
        for (int i = 1; i < n; ++i) {
            Avion key = lista.get(i);
            int j = i - 1;
            while (j >= 0 && lista.get(j).getVelocidad() > key.getVelocidad()) {
                lista.set(j + 1, lista.get(j));
                j = j - 1;
            }
            lista.set(j + 1, key);
        }
    }
}
