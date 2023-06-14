
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        Random random = new Random();
        this.combustibleDisponible = random.nextInt(80000) + 320000;
    }

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
                System.out.println("Avión " + avion + " recibido en el porta avion " + nombre);
            } else {
                System.out.println("Porta Avion " + nombre + " sin espacio en los hangares. No se puede recibir el avión " + avion);
            }
        }
    }

    public double getCapHang() {
        return capacidadHangares;
    }

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

    // Otros métodos y getters/setters según sea necesario
    public double getLatitude() {
        return latitude;
    }

    @Override
    public List<Avion> getAvionesEsperando() {
        return avionesEsperando;
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
