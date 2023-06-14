import java.util.ArrayList;
import java.util.List;

public class Aeropuerto {
    private int cantidadHangares;
    private List<Hangar> hangares;
    private double coordenadaX;
    private double coordenadaY;

    public Aeropuerto(int cantidadHangares, double coordenadaX, double coordenadaY) {
        this.cantidadHangares = cantidadHangares;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.hangares = new ArrayList<>();

        for (int i = 0; i < cantidadHangares; i++) {
            Hangar hangar = new Hangar();
            hangares.add(hangar);
        }
    }

    public int getCantidadHangares() {
        return cantidadHangares;
    }

    public void setCantidadHangares(int cantidadHangares) {
        this.cantidadHangares = cantidadHangares;
    }

    public List<Hangar> getHangares() {
        return hangares;
    }

    public void setHangares(List<Hangar> hangares) {
        this.hangares = hangares;
    }

    public double getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(double coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public double getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(double coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public static class Hangar {
        private int cantidadAviones;

        public Hangar() {
            this.cantidadAviones = 0;
        }

        public int getCantidadAviones() {
            return cantidadAviones;
        }

        public void setCantidadAviones(int cantidadAviones) {
            this.cantidadAviones = cantidadAviones;
        }
    }
}
