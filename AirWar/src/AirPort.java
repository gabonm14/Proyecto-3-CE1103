
import java.util.ArrayList;
import java.util.List;

public class AirPort {
    private String ubicacion;
    private int capacidadHangares;
    private List<Avion> avionesEsperando;
    private int combustibleDisponible;
    private double latitude;
    private double longitude;

    public AirPort(String ubicacion, int capacidadHangares) {
        this.ubicacion = ubicacion;
        this.capacidadHangares = capacidadHangares;
        this.avionesEsperando = new ArrayList<>();
        this.combustibleDisponible = 0;
    }

    public void recibirAvion(Avion avion) {
        if (avionesEsperando.size() < capacidadHangares) {
            avionesEsperando.add(avion);
            System.out.println("Avión " + avion + " recibido en el aeropuerto " + ubicacion);
        } else {
            System.out.println("Aeropuerto " + ubicacion + " sin espacio en los hangares. No se puede recibir el avión " + avion);
        }
    }

    public void despacharAvion() {
        if (!avionesEsperando.isEmpty()) {
            Avion avionDespachado = avionesEsperando.remove(0);
            System.out.println("Avión " + avionDespachado + " despachado desde el aeropuerto " + ubicacion);
        } else {
            System.out.println("No hay aviones esperando en el aeropuerto " + ubicacion);
        }
    }

    // Otros métodos y getters/setters según sea necesario

    public double getLatitude() {
        return latitude;
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
