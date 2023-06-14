
import java.util.ArrayList;
import java.util.List;

public class Portaavion extends Lugar {

    private String nombre;
    private int capacidadHangares;
    private List<Avion> avionesEsperando;
    private int combustibleDisponible;

    public Portaavion(String nombre, int capacidadHangares, double latitude, double longitude) {
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
            System.out.println("Avión " + avion + " recibido en el portaavión " + nombre);
        } else {
            System.out.println("Portaavión " + nombre + " sin espacio en los hangares. No se puede recibir el avión " + avion);
        }
    }
    public double getCapHang() {
        return capacidadHangares;
    }

    @Override
    public Avion despacharAvion() {
        if (!avionesEsperando.isEmpty()) {
            Avion avionDespachado = avionesEsperando.remove(0);
            System.out.println("Avión " + avionDespachado + " despachado desde el portaavión " + nombre);
            return avionDespachado;
        } else {
            System.out.println("No hay aviones esperando en el portaavión " + nombre);
            return null;
        }
    }

    // Otros métodos y getters/setters según sea necesario
    public double getLatitude() {
        return latitude;
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
