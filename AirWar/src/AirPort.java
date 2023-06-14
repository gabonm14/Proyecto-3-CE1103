
import java.util.ArrayList;
import java.util.List;

public class AirPort extends Lugar {

    private String nombre;
    private int capacidadHangares;
    private List<Avion> avionesEsperando;
    private int combustibleDisponible;

    public AirPort(String nombre, int capacidadHangares, double latitude, double longitude) {
        super(latitude, longitude);
        this.nombre = nombre;
        this.capacidadHangares = capacidadHangares;
        this.avionesEsperando = new ArrayList<>();
        this.combustibleDisponible = 0;
    }
@Override
    public void recibirAvion(Avion avion) {
        if (avionesEsperando.size() < capacidadHangares) {
            avionesEsperando.add(avion);
            System.out.println("Avión " + avion + " recibido en el aeropuerto " + nombre);
        } else {
            System.out.println("Aeropuerto " + nombre + " sin espacio en los hangares. No se puede recibir el avión " + avion);
        }
    }

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
    @Override
public List<Avion> getAvionesEsperando() {
        return avionesEsperando;
    }
    // Otros métodos y getters/setters según sea necesario
    public double getLatitude() {
        return latitude;
    }

    public double getCapHang() {
        return capacidadHangares;
    }

    public String getNombre() {
        return nombre;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
