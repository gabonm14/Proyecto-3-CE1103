import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Esta clase representa una lista enlazada de aviones.
 */
public class LinkedListAvion {

    private Nodo head;
    private int size;

    /**
     * Constructor de la clase LinkedListAvion.
     * Inicializa una lista enlazada vacía.
     */
    public LinkedListAvion() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Carga la lista de aviones desde un archivo.
     * @param nombreArchivo El nombre del archivo desde donde se cargará la lista.
     */
    public void cargarListaAviones(String nombreArchivo) {
        try (BufferedReader lector = new BufferedReader(new FileReader("src/archivos/" + nombreArchivo))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                String[] datos = linea.split(",");
                String nombre = datos[0];
                int velocidad = Integer.parseInt(datos[1]);
                double eficiencia = Double.parseDouble(datos[2]);
                int fortaleza = Integer.parseInt(datos[3]);

                Avion avion = new Avion(nombre, velocidad, eficiencia, fortaleza);
                agregarAvion(avion);
            }
            System.out.println("La lista de aviones se ha cargado desde el archivo " + nombreArchivo + " correctamente.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }

    /**
     * Escribe la lista de aviones en un archivo.
     * @param listaAviones La lista de aviones a escribir.
     * @param nombreArchivo El nombre del archivo donde se escribirá la lista.
     */
    public void escribirListaAviones(LinkedListAvion listaAviones, String nombreArchivo) {
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter("src/archivos/"+nombreArchivo))) {
            LinkedListAvion.Nodo nodoActual = listaAviones.head;
            while (nodoActual != null) {
                Avion avion = nodoActual.getAvion();
                String linea = String.format("%s,%d,%.2f,%d",
                        avion.getNombre(), avion.getVelocidad(), avion.getEficiencia(), avion.getFortaleza());
                escritor.write(linea);
                escritor.newLine();
                nodoActual = nodoActual.getSiguiente();
            }
            System.out.println("La lista de aviones se ha guardado en el archivo " + nombreArchivo + " correctamente.");
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo " + nombreArchivo + ": " + e.getMessage());
        }
    }

    /**
     * Agrega un avión al final de la lista.
     * @param avion El avión a agregar.
     */
    public void agregarAvion(Avion avion) {
        Nodo nuevoNodo = new Nodo(avion);

        if (head == null) {
            head = nuevoNodo;
        } else {
            Nodo nodoActual = head;
            while (nodoActual.getSiguiente() != null) {
                nodoActual = nodoActual.getSiguiente();
            }
            nodoActual.setSiguiente(nuevoNodo);
        }
        size++;
    }

    /**
     * Obtiene el avión en la posición indicada.
     * @param indice El índice del avión a obtener.
     * @return El avión en la posición indicada, o null si el índice está fuera de rango.
     */
    public Avion obtenerAvion(int indice) {
        if (indice < 0 || indice >= size) {
            // El índice está fuera de rango
            return null;
        }

        Nodo nodoActual = head;
        int contador = 0;

        while (contador < indice) {
            nodoActual = nodoActual.getSiguiente();
            contador++;
        }

        return nodoActual.getAvion();
    }

    /**
     * Ordena la lista de aviones utilizando el algoritmo Shell Sort.
     */
    public void shellSort() {
        int n = size;
        int gap = n / 2;

        while (gap > 0) {
            for (int i = gap; i < n; i++) {
                Avion temp = obtenerAvion(i);
                int j = i;

                while (j >= gap && obtenerAvion(j - gap).getEficiencia() > temp.getEficiencia()) {
                    setAvion(j, obtenerAvion(j - gap));
                    j -= gap;
                }

                setAvion(j, temp);
            }

            gap /= 2;
        }
    }

    /**
     * Actualiza el avión en la posición indicada con un nuevo avión.
     * @param indice El índice del avión a actualizar.
     * @param avion El nuevo avión.
     */
    public void setAvion(int indice, Avion avion) {
        if (indice < 0 || indice >= size) {
            // El índice está fuera de rango
            return;
        }

        Nodo nodoActual = head;
        int contador = 0;

        while (contador < indice) {
            nodoActual = nodoActual.getSiguiente();
            contador++;
        }

        nodoActual.setAvion(avion);
    }

    /**
     * Imprime los detalles de todos los aviones en la lista.
     */
    public void imprimirAviones() {
        //shellSort();

        Nodo nodoActual = head;
        while (nodoActual != null) {
            System.out.println("Nombre: " + nodoActual.getAvion().getNombre());
            System.out.println("Velocidad: " + nodoActual.getAvion().getVelocidad());
            System.out.println("Eficiencia: " + nodoActual.getAvion().getEficiencia());
            System.out.println("Fortaleza: " + nodoActual.getAvion().getFortaleza());
            System.out.println("--------------------------");
            nodoActual = nodoActual.getSiguiente();
        }
    }

    /**
     * Clase interna que representa un nodo de la lista enlazada.
     */
    public static class Nodo {

        private Avion avion;
        private Nodo siguiente;

        /**
         * Constructor de la clase Nodo.
         * @param avion El avión asociado al nodo.
         */
        public Nodo(Avion avion) {
            this.avion = avion;
            this.siguiente = null;
        }

        /**
         * Obtiene el avión asociado al nodo.
         * @return El avión asociado al nodo.
         */
        public Avion getAvion() {
            return avion;
        }

        /**
         * Establece el avión asociado al nodo.
         * @param avion El avión a establecer.
         */
        public void setAvion(Avion avion) {
            this.avion = avion;
        }

        /**
         * Obtiene el siguiente nodo en la lista.
         * @return El siguiente nodo en la lista.
         */
        public Nodo getSiguiente() {
            return siguiente;
        }

        /**
         * Establece el siguiente nodo en la lista.
         * @param siguiente El siguiente nodo a establecer.
         */
        public void setSiguiente(Nodo siguiente) {
            this.siguiente = siguiente;
        }
    }
}
