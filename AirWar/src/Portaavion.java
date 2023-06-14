import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Esta clase representa un portaavión, que es un tipo especial de lugar.
 */
public class Portaavion extends Lugar {

    private String nombre; // El nombre del portaavión.
    private int capacidadHangares; // La capacidad de los hangares del portaavión.
    private List<Avion> avionesEsperando; // La lista de aviones esperando en el portaavión.
    private int combustibleDisponible; // El combustible disponible en el portaavión.

    /**
     * Constructor de la clase Portaavion.
     *
     * @param nombre            El nombre del portaavión.
     * @param capacidadHangares La capacidad de los hangares del portaavión.
     * @param latitude          La latitud del portaavión.
     * @param longitude         La longitud del portaavión.
     */
    public Portaavion(String nombre, int capacidadHangares, double latitude, double longitude) {
        super(latitude, longitude);
        this.nombre = nombre;
        this.capacidadHangares = capacidadHangares;
        this.avionesEsperando = new ArrayList<>();
        Random random = new Random();
        this.combustibleDisponible = random.nextInt(120000) + 400000;
    }

    /**
     * Método para recibir un avión en el portaavión.
     *
     * @param avion El avión a recibir.
     */
    @Override
    public void recibirAvion(Avion avion) {
        if (avion == null) {
            return;
        } else {
            if (avionesEsperando.size() < capacidadHangares) {
                avionesEsperando.add(avion);
                int combustiblePromedio = combustibleDisponible / capacidadHangares;
                int rango = combustiblePromedio / 2;
                Random random = new Random();
                int combustibleAsignado = combustiblePromedio + random.nextInt(rango) - rango / 2;
                avion.gestionarCombustible(combustibleAsignado);
                System.out.println("Avión " + avion + " recibido en el portaavion " + nombre);
            } else {
                System.out.println("Porta Avion " + nombre + " sin espacio en los hangares. No se puede recibir el avión " + avion);
            }
        }
    }

    /**
     * Método para obtener la capacidad de los hangares del portaavión.
     *
     * @return La capacidad de los hangares del portaavión.
     */
    public double getCapHang() {
        return capacidadHangares;
    }

    /**
     * Método para despachar un avión desde el portaavión.
     *
     * @param avion El avión a despachar.
     * @return El avión despachado o null si no se puede despachar.
     */
    @Override
    public Avion despacharAvion(Avion avion) {
        if (!avionesEsperando.isEmpty()) {
            boolean removed = avionesEsperando.remove(avion);
            if (removed) {
                System.out.println("Avión " + avion + " despachado desde el portaavión " + nombre);
                return avion;
            } else {
                System.out.println("El avión " + avion + " no está esperando en el portaavión " + nombre);
                return null;
            }
        } else {
            System.out.println("No hay aviones esperando en el portaavión " + nombre);
            return null;
        }
    }

    /**
     * Método para obtener la latitud del portaavión.
     *
     * @return La latitud del portaavión.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Método para obtener la lista de aviones esperando en el portaavión.
     *
     * @return La lista de aviones esperando en el portaavión.
     */
    @Override
    public List<Avion> getAvionesEsperando() {
        return avionesEsperando;
    }

    /**
     * Método para obtener el nombre del portaavión.
     *
     * @return El nombre del portaavión.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer la latitud del portaavión.
     *
     * @param latitude La latitud a establecer.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Método para obtener la longitud del portaavión.
     *
     * @return La longitud del portaavión.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Método para establecer la longitud del portaavión.
     *
     * @param longitude La longitud a establecer.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
