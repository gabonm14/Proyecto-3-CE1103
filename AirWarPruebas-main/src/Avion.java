/**
 * La clase Avion representa un avión con sus atributos y funcionalidades.
 */
public class Avion {

    /**
     * El nombre del avión.
     */
    private String nombre;

    /**
     * La velocidad del avión en unidades por hora.
     */
    private int velocidad;

    /**
     * La eficiencia del avión, representada como un número decimal.
     */
    private double eficiencia;

    /**
     * La fortaleza del avión.
     */
    private int fortaleza;

    /**
     * La ubicación actual del avión.
     */
    private Lugar ubicacionActual;

    /**
     * El estado actual del avión.
     */
    public EstadoAvion estado;

    /**
     * El nivel de combustible del avión.
     */
    private int combustible;

    /**
     * Crea una instancia de la clase Avion con los valores especificados.
     *
     * @param nombre      el nombre del avión
     * @param velocidad   la velocidad del avión
     * @param eficiencia  la eficiencia del avión
     * @param fortaleza   la fortaleza del avión
     */
    public Avion(String nombre, int velocidad, double eficiencia, int fortaleza) {
        this.nombre = nombre;
        this.velocidad = velocidad;
        this.eficiencia = eficiencia;
        this.fortaleza = fortaleza;
        this.estado = EstadoAvion.EN_ESPERA;
        this.combustible = 0;
    }

    /**
     * Realiza el despegue del avión hacia el destino especificado.
     *
     * @param destino el lugar de destino del avión
     */
    public void despegar(Lugar destino) {
        ubicacionActual = destino;
        estado = EstadoAvion.EN_VUELO;
        System.out.println("Avión " + nombre + " ha despegado hacia " + destino);
    }

    /**
     * Realiza el aterrizaje del avión en la ubicación actual.
     */
    public void aterrizar() {
        estado = EstadoAvion.EN_ESPERA;
        System.out.println("Avión " + nombre + " ha aterrizado en " + ubicacionActual);
    }

    /**
     * Devuelve el nombre del avión.
     *
     * @return el nombre del avión
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve la velocidad del avión.
     *
     * @return la velocidad del avión
     */
    public int getVelocidad() {
        return velocidad;
    }

    /**
     * Devuelve la eficiencia del avión.
     *
     * @return la eficiencia del avión
     */
    public double getEficiencia() {
        return eficiencia;
    }

    /**
     * Devuelve el estado actual del avión.
     *
     * @return el estado actual del avión
     */
    public EstadoAvion getEstado() {
        return estado;
    }

    /**
     * Devuelve el nivel de combustible del avión.
     *
     * @return el nivel de combustible del avión
     */
    public int getCombustible() {
        return combustible;
    }

    /**
     * Devuelve la fortaleza del avión.
     *
     * @return la fortaleza del avión
     */
    public int getFortaleza() {
        return fortaleza;
    }

    /**
     * Actualiza la ubicación actual del avión.
     *
     * @param nuevaUbicacion la nueva ubicación del avión
     */
    public void actualizarUbicacion(Lugar nuevaUbicacion) {
        ubicacionActual = nuevaUbicacion;
    }

    /**
     * Gestiona la cantidad de combustible del avión y realiza las operaciones correspondientes.
     *
     * @param cantidad la cantidad de combustible a gestionar
     */
    public void gestionarCombustible(int cantidad) {
        if (combustible < 130000) {
            combustible += cantidad;
            if (combustible > 125000) {
                System.out.println("Almacenamiento de combustible lleno");
                combustible = 125000;
            }
            System.out.println("Avión " + nombre + " ha recargado " + cantidad + " unidades de combustible, combustible total: " + combustible);
        } else {
            System.out.println("Almacenamiento de combustible lleno");
        }
        if (combustible <= 0) {
            estado = EstadoAvion.DESTRUIDO;
            System.out.println("Avión " + nombre + " ha sido destruido");
        }
    }

    /**
     * Realiza el consumo de combustible del avión y devuelve el nuevo nivel de combustible.
     *
     * @param cantidad la cantidad de combustible a consumir
     * @return el nuevo nivel de combustible después del consumo
     */
    public int consumirCombustible(int cantidad) {
        combustible -= cantidad;
        // System.out.println("Avión " + nombre + "combustible total: " + combustible);
        return combustible;
    }

    /**
     * Destruye el avión.
     */
    public void destruir() {
        estado = EstadoAvion.DESTRUIDO;
    }

    /**
     * Los posibles estados de un avión.
     */
    public enum EstadoAvion {
        EN_ESPERA,
        EN_VUELO,
        DESTRUIDO
    }
}
