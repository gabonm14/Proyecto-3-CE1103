import java.util.List;

/**
 * La clase abstracta Lugar representa un lugar geográfico con una latitud y longitud.
 */
public abstract class Lugar {

    double latitude;
    double longitude;

    /**
     * Constructor de la clase Lugar.
     *
     * @param latitude  La latitud del lugar.
     * @param longitude La longitud del lugar.
     */
    public Lugar(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Obtiene la latitud del lugar.
     *
     * @return La latitud del lugar.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Establece la latitud del lugar.
     *
     * @param latitude La nueva latitud del lugar.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Obtiene la longitud del lugar.
     *
     * @return La longitud del lugar.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Establece la longitud del lugar.
     *
     * @param longitude La nueva longitud del lugar.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Calcula la coordenada X en un sistema de coordenadas bidimensional, basándose en la longitud del lugar.
     *
     * @return La coordenada X calculada.
     */
    public double getX() {
        double longitudeRange = 180.0;
        return (longitude + longitudeRange / 2.0) / longitudeRange * 1280;
    }

    /**
     * Calcula la coordenada Y en un sistema de coordenadas bidimensional, basándose en la latitud del lugar.
     *
     * @return La coordenada Y calculada.
     */
    public double getY() {
        double latitudeRange = 90.0;
        return (latitude + latitudeRange / 2.0) / latitudeRange * 720;
    }

    /**
     * Método abstracto para recibir un avión en el lugar.
     *
     * @param avion El avión a recibir.
     */
    public abstract void recibirAvion(Avion avion);

    /**
     * Método abstracto para despachar un avión desde el lugar.
     *
     * @param avion El avión a despachar.
     * @return El avión despachado.
     */
    public abstract Avion despacharAvion(Avion avion);

    /**
     * Método abstracto para obtener una lista de aviones esperando en el lugar.
     *
     * @return La lista de aviones esperando.
     */
    public abstract List<Avion> getAvionesEsperando();

    /**
     * Método abstracto para obtener el nombre del lugar.
     *
     * @return El nombre del lugar.
     */
    public abstract String getNombre();
}
