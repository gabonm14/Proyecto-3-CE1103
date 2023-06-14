public class Avion {
    private String nombre;
    private int velocidad;
    private int eficiencia;
    private int fortaleza;
    private String ubicacionActual;
    private EstadoAvion estado;
    private int combustible;

    public Avion(String nombre, int velocidad, int eficiencia, int fortaleza) {
        this.nombre = nombre;
        this.velocidad = velocidad;
        this.eficiencia = eficiencia;
        this.fortaleza = fortaleza;
        this.estado = EstadoAvion.EN_ESPERA;
        this.combustible = 0;
    }

    public void despegar(String destino) {
        ubicacionActual = destino;
        estado = EstadoAvion.EN_VUELO;
        System.out.println("Avión " + nombre + " ha despegado hacia " + destino);
    }

    public void aterrizar() {
        estado = EstadoAvion.EN_ESPERA;
        System.out.println("Avión " + nombre + " ha aterrizado en " + ubicacionActual);
    }

    public void actualizarUbicacion(String nuevaUbicacion) {
        ubicacionActual = nuevaUbicacion;
    }

    public void gestionarCombustible(int cantidad) {
        combustible += cantidad;
        System.out.println("Avión " + nombre + " ha recargado " + cantidad + " unidades de combustible");
    }

    // Otros métodos y getters/setters según sea necesario

    private enum EstadoAvion {
        EN_ESPERA,
        EN_VUELO
    }
}
