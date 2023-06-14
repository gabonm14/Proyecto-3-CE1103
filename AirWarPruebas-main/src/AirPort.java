import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * La clase AirPort representa un aeropuerto y hereda de la clase Lugar.
 */
public class AirPort extends Lugar {

    private String nombre;
    private int capacidadHangares;
    private List<Avion> avionesEsperando;
    private int combustibleDisponible;

    /**
     * Crea una instancia de la clase AirPort.
     *
     * @param nombre             El nombre del aeropuerto.
     * @param capacidadHangares  La capacidad de hangares del aeropuerto.
     * @param latitude           La latitud del aeropuerto.
     * @param longitude          La longitud del aeropuerto.
     */
    public AirPort(String nombre, int capacidadHangares, double latitude, double longitude) {
        super(latitude, longitude);
        this.nombre = nombre;
        this.capacidadHangares = capacidadHangares;
        this.avionesEsperando = new ArrayList<>();
        Random random = new Random();
        this.combustibleDisponible = random.nextInt(120000) + 400000;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void recibirAvion(Avion avion) {
        if (avionesEsperando.size() < capacidadHangares) {
            if (avion.getEstado() != Avion.EstadoAvion.DESTRUIDO) {
                avionesEsperando.add(avion);
                int combustiblePromedio = combustibleDisponible / capacidadHangares;
                int rango = combustiblePromedio / 2;
                Random random = new Random();
                int combustibleAsignado = combustiblePromedio + random.nextInt(rango) - rango / 2;
                avion.gestionarCombustible(combustibleAsignado);
                System.out.println("Avión " + avion + " recibido en el aeropuerto " + nombre);
            } else {
                System.out.print("El avion no llegó al Airport");
            }
        } else {
            System.out.println("Aeropuerto " + nombre + " sin espacio en los hangares. No se puede recibir el avión " + avion);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Avion despacharAvion(Avion avion) {
        if (!avionesEsperando.isEmpty()) {
            boolean removed = avionesEsperando.remove(avion);
            if (removed) {
                System.out.println("Avión " + avion + " despachado desde el aeropuerto " + nombre);
                return avion;
            } else {
                System.out.println("El avión " + avion + " no está esperando en el aeropuerto " + nombre);
                return null;
            }
        } else {
            System.out.println("No hay aviones esperando en el aeropuerto " + nombre);
            return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Avion> getAvionesEsperando() {
        return avionesEsperando;
    }

    // Otros métodos y getters/setters según sea necesario

    /**
     * Devuelve la latitud del aeropuerto.
     *
     * @return La latitud del aeropuerto.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Devuelve la capacidad de hangares del aeropuerto.
     *
     * @return La capacidad de hangares del aeropuerto.
     */
    public double getCapHang() {
        return capacidadHangares;
    }

    /**
     * Devuelve el nombre del aeropuerto.
     *
     * @return El nombre del aeropuerto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece la latitud del aeropuerto.
     *
     * @param latitude La latitud del aeropuerto.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Devuelve la longitud del aeropuerto.
     *
     * @return La longitud del aeropuerto.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Establece la longitud del aeropuerto.
     *
     * @param longitude La longitud del aeropuerto.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
